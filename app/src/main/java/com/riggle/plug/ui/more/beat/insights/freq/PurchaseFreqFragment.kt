package com.riggle.plug.ui.more.beat.insights.freq

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.riggle.plug.R
import com.riggle.plug.data.model.OrderFrequency
import com.riggle.plug.databinding.FragmentPurchaseFreqBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class PurchaseFreqFragment : BaseFragment<FragmentPurchaseFreqBinding>() {

    private val viewModel: PurchaseFreqFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var BEAT_INSIGHTS_FOR = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_purchase_freq
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        tfLight = ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)

        val calendar = binding.calendarView.currentPageDate
        getBeatInsights(
            (calendar.get(Calendar.MONTH) + 1).toString(), calendar.get(Calendar.YEAR).toString()
        )

        Coroutines.main {
            binding.calendarView.setOnPreviousPageChangeListener(object :
                OnCalendarPageChangeListener {
                override fun onChange() {
                    val calendar = binding.calendarView.currentPageDate
                    getBeatInsights(
                        (calendar.get(Calendar.MONTH) + 1).toString(),
                        calendar.get(Calendar.YEAR).toString()
                    )
                }
            })

            binding.calendarView.setOnForwardPageChangeListener(object :
                OnCalendarPageChangeListener {
                override fun onChange() {
                    val calendar = binding.calendarView.currentPageDate
                    getBeatInsights(
                        (calendar.get(Calendar.MONTH) + 1).toString(),
                        calendar.get(Calendar.YEAR).toString()
                    )
                }
            })
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private fun initObservers() {
        viewModel.obrBeatInsights.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.llNoDataBeatFreqOrders.visibility = View.GONE
                        binding.clPurchaseFreq.visibility = View.VISIBLE
                        it.data.order_frequency.let { dataList ->
                            setCalendarView(dataList)
                        }
                        if (it.data.order_frequency != null) {
                            Log.e("Purchase_Freq", it.data.order_frequency.toString())
                        }
                    } else {
                        binding.llNoDataBeatFreqOrders.visibility = View.VISIBLE
                        binding.clPurchaseFreq.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    private fun getBeatInsights(month: String, year: String) {
        val query = HashMap<String, String>()
        query["company"] = BEAT_INSIGHTS_FOR
        query["brand"] = ""
        query["insights"] = "true"
        query["retailers"] = "true"
        query["month"] = month
        query["year"] = year

        viewModel.getCitiesList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private var tfLight: Typeface? = null
    private var frequencyList = ArrayList<OrderFrequency>()
    var selecetdDates = ArrayList<Calendar>()
    private fun setCalendarView(dataList: List<OrderFrequency>) {
        frequencyList = dataList as ArrayList<OrderFrequency>

        val events = ArrayList<EventDay>()
        selecetdDates = ArrayList()
        val disableDates = ArrayList<Calendar>()
        for (index in dataList) {
            index.revenue.let { revenue ->
                if (revenue > 0) {
                    val calendar = Calendar.getInstance() //2023-09-30
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    calendar.time = index.day.let { simpleDateFormat.parse(it) } as Date

                    val drawable = if (index.revenue > 999) {
                        getDrawableText(
                            requireActivity(),
                            ImageBindingAdapter.getFormattedNumber(index.revenue.toLong()),
                            tfLight
                        )
                    } else {
                        getDrawableText(
                            requireActivity(),
                            "₹${DecimalFormat("########").format(index.revenue)}",
                            tfLight
                        )
                    }

                    events.add(
                        EventDay(
                            calendar, drawable
                        )
                    )
                }
            }

            if ((index.revenue != null && index.revenue > 0)) {/*if (index.hours != null && index.hours > 0) {
                    val calendarEnable = Calendar.getInstance() //2023-09-30
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    calendarEnable.time = index.day.let { simpleDateFormat.parse(it) } as Date
                    selecetdDates.add(calendarEnable)
                }*/
            } else {
                val calendarDisable = Calendar.getInstance() //2023-09-30
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                calendarDisable.time = index.day.let { simpleDateFormat.parse(it) } as Date
                disableDates.add(calendarDisable)
            }
        }
        binding.calendarView.setEvents(events)

        //  binding.calendarView.selectedDates = selecetdDates
        binding.calendarView.setDisabledDays(disableDates)
    }

    private fun getDrawableText(
        context: Context, text: String, typeface: Typeface?
    ): Drawable {
        val resources = context.resources
        val bitmap = Bitmap.createBitmap(100, 48, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.typeface = typeface ?: Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = ContextCompat.getColor(context, R.color.line_color)
        val scale = resources.displayMetrics.density
        paint.textSize = (12 * scale).toInt().toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (bitmap.width - bounds.width()) / 2
        val y = (bitmap.height + bounds.height()) / 2
        canvas.drawText(text, x.toFloat(), y.toFloat(), paint)
        return BitmapDrawable(context.resources, bitmap)
    }
}