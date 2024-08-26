package com.riggle.plug.ui.home.sales.salesInsights.insights

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.DailyAnalysis
import com.riggle.plug.data.model.OrderFrequency
import com.riggle.plug.data.model.SalesTargetAnalysisResponseModel
import com.riggle.plug.data.model.SellingSku1
import com.riggle.plug.data.model.TargetProgressData
import com.riggle.plug.databinding.CustomLayoutTopSalesSkusBinding
import com.riggle.plug.databinding.FragmentSalesmanInsightsBinding
import com.riggle.plug.databinding.LaySalesDailyAnalysisItemBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.chart.CustomMarkerView
import com.riggle.plug.utils.chart.MyValueFormatter
import com.riggle.plug.utils.event.SingleLiveEvent
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class SalesmanInsightsFragment : BaseFragment<FragmentSalesmanInsightsBinding>() {

    private val viewModel: SalesmanInsightsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var tfLight: Typeface? = null
    private var loadForUpdated = ""

    companion object {
        var userID = ""
        var loadFor = SingleLiveEvent<String>()
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_salesman_insights
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        tfLight = ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)

        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.format(cal.time)
        binding.tv11.text = date

        loadFor.observe(viewLifecycleOwner) {
            loadForUpdated = it

            when (it) {
                "Daily Analysis" -> {
                    binding.svDailyAnalysis.visibility = View.VISIBLE
                    binding.clDrrTrend.visibility = View.GONE
                    binding.svReachAnalysis.visibility = View.GONE
                    binding.llBrandAnaylysis.visibility = View.GONE
                    binding.llSKUs.visibility = View.GONE
                    initDailyAnalysisAdapter()
                    val cal = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val date = dateFormat.format(cal.time)
                    getDailyAnalysis(date)

                    val monthFormat = SimpleDateFormat("MM")
                    val yearFormat = SimpleDateFormat("yyyy")
                    val month = monthFormat.format(cal.time)
                    val year = yearFormat.format(cal.time)
                    getDailyCalenderInsights(month, year, date)
                }

                "DRR Trend" -> {
                    binding.clDrrTrend.visibility = View.VISIBLE
                    binding.svDailyAnalysis.visibility = View.GONE
                    binding.svReachAnalysis.visibility = View.GONE
                    binding.llBrandAnaylysis.visibility = View.GONE
                    binding.llSKUs.visibility = View.GONE
                    getSalesTargetAnalysisList()
                }

                "Reach Analysis" -> {
                    binding.clDrrTrend.visibility = View.GONE
                    binding.svDailyAnalysis.visibility = View.GONE
                    binding.llBrandAnaylysis.visibility = View.GONE
                    binding.svReachAnalysis.visibility = View.VISIBLE
                    binding.llSKUs.visibility = View.GONE
                    getBrandAnalysisInsights()
                    getSalesTargetAnalysisList()
                }

                "Brand Analysis" -> {
                    binding.llBrandAnaylysis.visibility = View.VISIBLE
                    binding.clDrrTrend.visibility = View.GONE
                    binding.svDailyAnalysis.visibility = View.GONE
                    binding.svReachAnalysis.visibility = View.GONE
                    binding.llSKUs.visibility = View.GONE
                    getBrandAnalysisInsights()
                }

                "SKUs" -> {
                    binding.svReachAnalysis.visibility = View.GONE
                    binding.llBrandAnaylysis.visibility = View.GONE
                    binding.clDrrTrend.visibility = View.GONE
                    binding.llSKUs.visibility = View.VISIBLE
                    binding.svDailyAnalysis.visibility = View.GONE
                    initTopSKUsAdapter()
                    getSalesSKUsList()
                }
            }
        }

        when (loadForUpdated) {
            "Daily Analysis" -> {
                binding.svDailyAnalysis.visibility = View.VISIBLE
                binding.clDrrTrend.visibility = View.GONE
                binding.svReachAnalysis.visibility = View.GONE
                binding.llBrandAnaylysis.visibility = View.GONE
                binding.llSKUs.visibility = View.GONE
                initDailyAnalysisAdapter()

                val cal = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val date = dateFormat.format(cal.time)
                getDailyAnalysis(date)

                val monthFormat = SimpleDateFormat("MM")
                val yearFormat = SimpleDateFormat("yyyy")
                val month = monthFormat.format(cal.time)
                val year = yearFormat.format(cal.time)
                getDailyCalenderInsights(month, year, date)
            }

            "DRR Trend" -> {
                binding.clDrrTrend.visibility = View.VISIBLE
                binding.svReachAnalysis.visibility = View.GONE
                binding.llBrandAnaylysis.visibility = View.GONE
                binding.llSKUs.visibility = View.GONE
                binding.svDailyAnalysis.visibility = View.GONE
                getSalesTargetAnalysisList()
            }

            "Reach Analysis" -> {
                binding.clDrrTrend.visibility = View.GONE
                binding.llBrandAnaylysis.visibility = View.GONE
                binding.svReachAnalysis.visibility = View.VISIBLE
                binding.svDailyAnalysis.visibility = View.GONE
                binding.llSKUs.visibility = View.GONE
                getBrandAnalysisInsights()
                getSalesTargetAnalysisList()
            }

            "Brand Analysis" -> {
                binding.llBrandAnaylysis.visibility = View.VISIBLE
                binding.clDrrTrend.visibility = View.GONE
                binding.svDailyAnalysis.visibility = View.GONE
                binding.svReachAnalysis.visibility = View.GONE
                binding.llSKUs.visibility = View.GONE
                getBrandAnalysisInsights()
            }

            "SKUs" -> {
                binding.svReachAnalysis.visibility = View.GONE
                binding.svDailyAnalysis.visibility = View.GONE
                binding.llBrandAnaylysis.visibility = View.GONE
                binding.clDrrTrend.visibility = View.GONE
                binding.llSKUs.visibility = View.VISIBLE
                initTopSKUsAdapter()
                getSalesSKUsList()
            }
        }

        binding.calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val clickedDayCalendar = calendarDay.calendar
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val monthFormat = SimpleDateFormat("MM")
                val yearFormat = SimpleDateFormat("yyyy")
                val date = dateFormat.format(clickedDayCalendar.time)
                val month = monthFormat.format(clickedDayCalendar.time)
                val year = yearFormat.format(clickedDayCalendar.time)
                getDailyAnalysis(date)
                binding.tv11.text = date

                getDailyCalenderInsights(month, year, date)
            }
        })
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private fun getDailyCalenderInsights(month: String, year: String, date: String) {
        val query = HashMap<String, String>()
        query["user"] = userID
        query["brand"] = ""
        query["salesman_insights"] = "true"
        query["frequency"] = "true"
        query["date"] = date
        query["month"] = month
        query["year"] = year
        query["type"] = "retailer"

        viewModel.getDailyCalenderAnalysisInsights(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private fun getSalesTargetAnalysisList() {
        val c = Calendar.getInstance()
        val currentYear = c[Calendar.YEAR]
        val query = HashMap<String, String>()
        query["user"] = userID

        val dateFormat = SimpleDateFormat("MM")
        val date1 = Date()
        val currentMonth = dateFormat.format(date1)
        query["month"] = currentMonth
        query["year"] = currentYear.toString()

        viewModel.getSalesTargetList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company!!.id,
            query
        )
    }

    private fun getBrandAnalysisInsights() {
        val c = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM")
        val date1 = Date()
        val currentMonth = dateFormat.format(date1)

        val currentYear = c[Calendar.YEAR]
        val query = HashMap<String, String>()
        query["user"] = userID
        query["brand"] = ""
        query["salesman_insights"] = "true"
        query["mixed_graph"] = "true"
        query["retailers"] = "true"
        query["month"] = currentMonth.toString()
        query["year"] = currentYear.toString()
        query["type"] = "retailer"
        viewModel.getSalesBrandAnalysisInsights(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id.toString(),
            query
        )
    }

    private fun getSalesSKUsList() {
        val query = HashMap<String, String>()
        query["user"] = userID
        query["salesman_insights"] = "true"
        query["skus"] = "true"
        viewModel.getSalesSKUsList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private fun getDailyAnalysis(date: String) {
        val query = HashMap<String, String>()
        query["user"] = userID
        query["brand"] = ""
        query["salesman_insights"] = "true"
        query["date"] = date
        query["type"] = "retailer"
        query["remarks_details"] = "true"
        viewModel.getDailyAnalysis(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private var xAxisBarLabelsList = ArrayList<String>()
    private var barListAOV1 = ArrayList<BarEntry>()
    private var barListTotalOrders = ArrayList<BarEntry>()
    private var xAxisTotalOrdersList = ArrayList<String>()
    private var barListTotalRevenue = ArrayList<BarEntry>()
    private var xAxisTotalRevenuesList = ArrayList<String>()
    private fun initObservers() {

        viewModel.obrSalesTargetList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)

                    when (loadForUpdated) {
                        "DRR Trend" -> {
                            if (!it.data.isNullOrEmpty()) {
                                setGraph(it.data)
                            }
                        }

                        "Reach Analysis" -> {
                            if (!it.data.isNullOrEmpty()) {
                                for (index in it.data) {
                                    if (!index.active_accounts_target_data.isNullOrEmpty()) {
                                        setUpLineChart(
                                            binding.barChartThree,
                                            index.active_accounts_target_value.toFloat(),
                                            index.active_accounts_target_data
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrBrandAnalysisInsights.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.llNoDataBeatGraphOrders.visibility = View.GONE
                        binding.nsvBrandAnalysis.visibility = View.VISIBLE

                        if (!it.data.number_of_retailers.isNullOrEmpty()) {
                            xAxisBarLabelsList.clear()
                            barListAOV1.clear()
                            for (i in it.data.number_of_retailers.indices) {
                                xAxisBarLabelsList.add(ImageBindingAdapter.getMonthName(it.data.number_of_retailers[i].month_name))
                                barListAOV.add(
                                    BarEntry(
                                        i.toFloat(),
                                        it.data.number_of_retailers[i].number_of_retailers.toFloat()
                                    )
                                )
                            }
                            initAverageOrderChart()
                        }

                        if (!it.data.average_order_value.isNullOrEmpty()) {
                            xAxisBarLabelsList.clear()
                            barListAOV1.clear()
                            for (i in it.data.average_order_value.indices) {
                                xAxisBarLabelsList.add(ImageBindingAdapter.getMonthName(it.data.average_order_value[i].month_name))
                                barListAOV1.add(
                                    BarEntry(
                                        i.toFloat(),
                                        it.data.average_order_value[i].revenue.toFloat()
                                    )
                                )
                            }
                            initAverageOrderChart1()
                        }

                        if (!it.data.total_orders_count.isNullOrEmpty()) {
                            xAxisTotalOrdersList.clear()
                            barListTotalOrders.clear()
                            for (i in it.data.total_orders_count.indices) {
                                xAxisTotalOrdersList.add(ImageBindingAdapter.getMonthName(it.data.total_orders_count[i].month_name))
                                barListTotalOrders.add(
                                    BarEntry(
                                        i.toFloat(),
                                        it.data.total_orders_count[i].orders_count.toFloat()
                                    )
                                )
                            }
                            initTotalOrderChart()
                        }

                        if (!it.data.total_revenues.isNullOrEmpty()) {
                            xAxisTotalRevenuesList.clear()
                            barListTotalRevenue.clear()
                            for (i in it.data.total_revenues.indices) {
                                xAxisTotalRevenuesList.add(ImageBindingAdapter.getMonthName(it.data.total_revenues[i].month_name))
                                barListTotalRevenue.add(
                                    BarEntry(
                                        i.toFloat(), it.data.total_revenues[i].revenue.toFloat()
                                    )
                                )
                            }
                            initTotalRevenueChart()
                        }
                    } else {
                        binding.llNoDataBeatGraphOrders.visibility = View.VISIBLE
                        binding.nsvBrandAnalysis.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrSalesSKUstList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (!it.data?.selling_skus.isNullOrEmpty()) {
                        if (it.data != null) {
                            binding.llSKUs.visibility = View.VISIBLE

                            if (it.data.selling_skus.isNotEmpty()) {
                                topAdapter.list = it.data.selling_skus
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrSalesDailyAnalysis.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    val list = ArrayList<DailyAnalysis>()
                    if (it.data != null) {
                        list.add(
                            DailyAnalysis(
                                "New Party",
                                "0",
                                it.data.new_remarks.toString(),
                                it.data.new_orders_count.toString(),
                                it.data.new_orders_values.toString()
                            )
                        )
                        list.add(
                            DailyAnalysis(
                                "Existing Party",
                                "0",
                                it.data.existing_remarks.toString(),
                                it.data.existing_orders_count.toString(),
                                it.data.existing_orders_values.toString()
                            )
                        )
                        list.add(
                            DailyAnalysis(
                                "Total Party",
                                "0",
                                it.data.total_remarks.toString(),
                                it.data.total_orders.toString(),
                                it.data.total_placed_orders_value.toString()
                            )
                        )
                        dailyAnalysisAdapter.list = list
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrDailyCalenderAnalysisInsights.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        it.data.order_frequency.let { dataList ->
                            setCalendarView(dataList)
                        }
                        if (it.data.order_frequency != null) {
                            Log.e("Purchase_Freq", it.data.order_frequency.toString())
                        }
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

    private var frequencyList = ArrayList<OrderFrequency>()
    private var selecetdDates = ArrayList<java.util.Calendar>()
    private fun setCalendarView(dataList: List<OrderFrequency>) {
        frequencyList = dataList as ArrayList<OrderFrequency>

        val events = ArrayList<EventDay>()
        selecetdDates = ArrayList()
        val disableDates = ArrayList<java.util.Calendar>()
        for (index in dataList) {
            index.revenue.let { revenue ->
                if (revenue > 0) {
                    val calendar = java.util.Calendar.getInstance() //2023-09-30
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
                val calendarDisable = java.util.Calendar.getInstance() //2023-09-30
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


    private lateinit var dailyAnalysisAdapter: SimpleRecyclerViewAdapter<DailyAnalysis, LaySalesDailyAnalysisItemBinding>
    private fun initDailyAnalysisAdapter() {
        dailyAnalysisAdapter = SimpleRecyclerViewAdapter(
            R.layout.lay_sales_daily_analysis_item, BR.bean
        ) { v, m, pos ->

        }
        binding.rvDailyAnalysis.adapter = dailyAnalysisAdapter
    }

    private lateinit var topAdapter: SimpleRecyclerViewAdapter<SellingSku1, CustomLayoutTopSalesSkusBinding>
    private fun initTopSKUsAdapter() {
        topAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_top_sales_skus, BR.bean
        ) { v, m, pos ->

        }
        binding.rvTopSellingSKUs.adapter = topAdapter
    }

    private fun initAverageOrderChart1() {
        binding.bcAverageOrderValue1.invalidate()

        binding.bcAverageOrderValue1.setDrawBarShadow(false)
        binding.bcAverageOrderValue1.setDrawValueAboveBar(false)
        binding.bcAverageOrderValue1.description.isEnabled = false
        binding.bcAverageOrderValue1.setPinchZoom(false)
        binding.bcAverageOrderValue1.isDoubleTapToZoomEnabled = false
        binding.bcAverageOrderValue1.isAutoScaleMinMaxEnabled = true
        binding.bcAverageOrderValue1.setDrawGridBackground(false)
        binding.bcAverageOrderValue1.isDragEnabled = false
        binding.bcAverageOrderValue1.setScaleEnabled(false)
        binding.bcAverageOrderValue1.xAxis.setLabelCount(xAxisBarLabelsList.size, false)

        binding.bcAverageOrderValue1.xAxis.valueFormatter =
            IndexAxisValueFormatter(xAxisBarLabelsList)

        binding.bcAverageOrderValue1.xAxis.setDrawGridLines(false)
        binding.bcAverageOrderValue1.axisLeft.setDrawGridLines(true)
        binding.bcAverageOrderValue1.axisRight.setDrawGridLines(false)

        binding.bcAverageOrderValue1.axisLeft.isEnabled = true
        binding.bcAverageOrderValue1.axisRight.isEnabled = false
        binding.bcAverageOrderValue1.xAxis.axisLineColor =
            homeActivity.resources.getColor(R.color.dark_grey_txt_color2)
        binding.bcAverageOrderValue1.xAxis.setDrawLabels(true)
        binding.bcAverageOrderValue1.xAxis.textSize = 11f
        binding.bcAverageOrderValue1.legend.isEnabled = false
        binding.bcAverageOrderValue1.xAxis.typeface = tfLight

        binding.bcAverageOrderValue1.setViewPortOffsets(85f, 60f, 0f, 50f)
        binding.bcAverageOrderValue1.setTouchEnabled(true)
        binding.bcAverageOrderValue1.isScaleXEnabled = false
        binding.bcAverageOrderValue1.isScaleYEnabled = false
        binding.bcAverageOrderValue1.setPinchZoom(false)

        val yAxis = binding.bcAverageOrderValue1.axisLeft
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawZeroLine(true)
        yAxis.enableGridDashedLine(25f, 15f, 2f)


        binding.bcAverageOrderValue1.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.bcAverageOrderValue1.axisLeft.axisMinimum = 0f
        binding.bcAverageOrderValue1.axisRight.axisMinimum = 0f
        setData1()

    }

    private fun setData1() {
        colorsList.clear()
        colorsList.add(ContextCompat.getColor(homeActivity, R.color.line_color))
        val set1 = BarDataSet(barListAOV1, "")
        set1.setDrawIcons(false)
        set1.colors = colorsList
        set1.setDrawValues(false)
        set1.highLightColor = Color.TRANSPARENT
        set1.highLightAlpha = 0
        set1.valueTypeface = tfLight
        val dataSets = java.util.ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        if (set1.values.size < 2) {
            data.barWidth = 0.1f
        } else {
            data.barWidth = 0.6f
        }

        binding.bcAverageOrderValue1.data = data
        binding.bcAverageOrderValue1.notifyDataSetChanged()

        binding.bcAverageOrderValue1.animateY(1200)
        binding.bcAverageOrderValue1.invalidate()
    }

    private fun initTotalOrderChart() {
        binding.bcTotalOrders.invalidate()

        binding.bcTotalOrders.setDrawBarShadow(false)
        binding.bcTotalOrders.setDrawValueAboveBar(false)
        binding.bcTotalOrders.description.isEnabled = false
        binding.bcTotalOrders.setPinchZoom(false)
        binding.bcTotalOrders.isDoubleTapToZoomEnabled = false
        binding.bcTotalOrders.isAutoScaleMinMaxEnabled = true
        binding.bcTotalOrders.setDrawGridBackground(false)
        binding.bcTotalOrders.isDragEnabled = false
        binding.bcTotalOrders.setScaleEnabled(false)
        binding.bcTotalOrders.xAxis.setLabelCount(xAxisTotalOrdersList.size, false)

        binding.bcTotalOrders.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisTotalOrdersList)

        binding.bcTotalOrders.xAxis.setDrawGridLines(false)
        binding.bcTotalOrders.axisLeft.setDrawGridLines(true)
        binding.bcTotalOrders.axisRight.setDrawGridLines(false)

        binding.bcTotalOrders.axisLeft.isEnabled = true
        binding.bcTotalOrders.axisRight.isEnabled = false
        binding.bcTotalOrders.xAxis.axisLineColor =
            homeActivity.resources.getColor(R.color.dark_grey_txt_color2)
        binding.bcTotalOrders.xAxis.setDrawLabels(true)
        binding.bcTotalOrders.xAxis.textSize = 11f
        binding.bcTotalOrders.legend.isEnabled = false
        binding.bcTotalOrders.xAxis.typeface = tfLight

        binding.bcTotalOrders.setViewPortOffsets(85f, 60f, 0f, 50f)
        binding.bcTotalOrders.setTouchEnabled(true)
        binding.bcTotalOrders.isScaleXEnabled = false
        binding.bcTotalOrders.isScaleYEnabled = false
        binding.bcTotalOrders.setPinchZoom(false)

        val yAxis = binding.bcTotalOrders.axisLeft
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawZeroLine(true)
        yAxis.enableGridDashedLine(25f, 15f, 2f)


        binding.bcTotalOrders.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.bcTotalOrders.axisLeft.axisMinimum = 0f
        binding.bcTotalOrders.axisRight.axisMinimum = 0f
        setTotalOrdersData()
    }

    private fun setTotalOrdersData() {
        colorsList.clear()
        colorsList.add(ContextCompat.getColor(homeActivity, R.color.line_color))
        val set1 = BarDataSet(barListTotalOrders, "")
        set1.setDrawIcons(false)
        set1.colors = colorsList
        set1.setDrawValues(false)
        set1.highLightColor = Color.TRANSPARENT
        set1.highLightAlpha = 0
        set1.valueTypeface = tfLight
        val dataSets = java.util.ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        if (set1.values.size < 2) {
            data.barWidth = 0.1f
        } else {
            data.barWidth = 0.6f
        }

        binding.bcTotalOrders.data = data
        binding.bcTotalOrders.notifyDataSetChanged()

        binding.bcTotalOrders.animateY(0)
        binding.bcTotalOrders.invalidate()

    }

    private fun initTotalRevenueChart() {
        binding.bcTotalRevenue.invalidate()

        binding.bcTotalRevenue.setDrawBarShadow(false)
        binding.bcTotalRevenue.setDrawValueAboveBar(false)
        binding.bcTotalRevenue.description.isEnabled = false
        binding.bcTotalRevenue.setPinchZoom(false)
        binding.bcTotalRevenue.isDoubleTapToZoomEnabled = false
        binding.bcTotalRevenue.isAutoScaleMinMaxEnabled = true
        binding.bcTotalRevenue.setDrawGridBackground(false)
        binding.bcTotalRevenue.isDragEnabled = false
        binding.bcTotalRevenue.setScaleEnabled(false)
        binding.bcTotalRevenue.xAxis.setLabelCount(xAxisTotalRevenuesList.size, false)

        binding.bcTotalRevenue.xAxis.valueFormatter =
            IndexAxisValueFormatter(xAxisTotalRevenuesList)

        binding.bcTotalRevenue.xAxis.setDrawGridLines(false)
        binding.bcTotalRevenue.axisLeft.setDrawGridLines(true)
        binding.bcTotalRevenue.axisRight.setDrawGridLines(false)

        binding.bcTotalRevenue.axisLeft.isEnabled = true
        binding.bcTotalRevenue.axisRight.isEnabled = false
        binding.bcTotalRevenue.xAxis.axisLineColor =
            homeActivity.resources.getColor(R.color.dark_grey_txt_color2)
        binding.bcTotalRevenue.xAxis.setDrawLabels(true)
        binding.bcTotalRevenue.xAxis.textSize = 11f
        binding.bcTotalRevenue.legend.isEnabled = false
        binding.bcTotalRevenue.xAxis.typeface = tfLight

        binding.bcTotalRevenue.setViewPortOffsets(100f, 60f, 0f, 50f)
        binding.bcTotalRevenue.setTouchEnabled(true)
        binding.bcTotalRevenue.isScaleXEnabled = false
        binding.bcTotalRevenue.isScaleYEnabled = false
        binding.bcTotalRevenue.setPinchZoom(false)

        val yAxis = binding.bcTotalRevenue.axisLeft
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawZeroLine(true)
        yAxis.enableGridDashedLine(25f, 15f, 2f)


        binding.bcTotalRevenue.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.bcTotalRevenue.axisLeft.axisMinimum = 0f
        binding.bcTotalRevenue.axisRight.axisMinimum = 0f
        setTotalRevenueData()
    }

    private fun setTotalRevenueData() {
        colorsList.clear()
        colorsList.add(ContextCompat.getColor(homeActivity, R.color.line_color))
        val set1 = BarDataSet(barListTotalRevenue, "")
        set1.setDrawIcons(false)
        set1.colors = colorsList
        set1.setDrawValues(false)
        set1.highLightColor = Color.TRANSPARENT
        set1.highLightAlpha = 0
        set1.valueTypeface = tfLight
        val dataSets = java.util.ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        if (set1.values.size < 2) {
            data.barWidth = 0.1f
        } else {
            data.barWidth = 0.6f
        }

        binding.bcTotalRevenue.data = data
        binding.bcTotalRevenue.notifyDataSetChanged()

        binding.bcTotalRevenue.animateY(0)
        binding.bcTotalRevenue.invalidate()

    }

    private fun setGraph(list: List<SalesTargetAnalysisResponseModel>) {
        //setUpBarChartRadius(binding.barChart)
        for (index in list) {
            if (!index.productive_count_target_data.isNullOrEmpty()) {
                setUpLineChart(
                    binding.barChart,
                    index.productive_count_target_value.toFloat(),
                    index.productive_count_target_data
                )
            }
            if (!index.total_count_target_data.isNullOrEmpty()) {
                setUpLineChart(
                    binding.barChartOne,
                    index.total_count_target_value.toFloat(),
                    index.total_count_target_data
                )
            }
            if (!index.order_amount_target_data.isNullOrEmpty()) {
                setUpLineChart(
                    binding.barChartTwo,
                    index.order_amount_target_value.toFloat(),
                    index.order_amount_target_data
                )
            }/*if (!index.active_accounts_target_data.isNullOrEmpty()) {
                setUpLineChart(
                    binding.barChartThree,
                    index.active_accounts_target_value.toFloat(),
                    index.active_accounts_target_data
                )
            }*/
        }
    }

    private fun setUpLineChart(
        barChart: LineChart, targetValue: Float?, targetData: List<TargetProgressData>
    ) {

        val markerView = CustomMarkerView(requireContext(), R.layout.custom_marker_view_layout)
        barChart.marker = markerView

        barChart.description.isEnabled = false

        barChart.axisLeft.setDrawLabels(true)
        barChart.axisRight.setDrawLabels(false)
        barChart.xAxis.setDrawLabels(true)

        barChart.axisLeft.isEnabled = true
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false

        barChart.xAxis.axisLineColor = ContextCompat.getColor(requireContext(), R.color.trans)

        barChart.setTouchEnabled(true)

        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(65)

        barChart.setDrawGridBackground(false)
        // chart.setDrawYLabels(false);

//            val data = ArrayList<String>()
//            for (index in it) {
//                data.add(UtilMethods.graphDate(index.month_name.toString()))
//            }
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getDataX(targetData))

//        val xAxisLabels = listOf("May", "Jun", "Jul", "Aug", "Sept", "Oct")
//        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = tfLight
        xAxis.setDrawGridLines(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color_80)
        xAxis.granularity = 1f // only intervals of 1 day

        targetData.let {
            xAxis.labelCount = it.size / 2
        }

        //xAxis.labelCount = 6
        //xAxis.valueFormatter = xAxisFormatter as ValueFormatter?

        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.typeface = tfLight
        leftAxis.setLabelCount(8, false)
        //leftAxis.setValueFormatter(custom as ValueFormatter?)
        leftAxis.textColor =
            ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color_80)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 10f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.typeface = tfLight
        rightAxis.setLabelCount(8, false)
        //rightAxis.setValueFormatter(custom)
        rightAxis.textColor =
            ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color_80)
        rightAxis.spaceTop = 10f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val yAxis = barChart.axisLeft
        yAxis.setDrawAxisLine(false)

        val l: Legend = barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.LINE
        l.formSize = 9f
        l.textSize = 8f
        l.xEntrySpace = 0f

        barChart.animateY(100)
        //setBarData(getData(), barChart)
        setBarData(barChart, targetValue, targetData)
    }


    private var colorList = ArrayList<Int>()
    private fun setBarData(
        barChart: LineChart, targetValue: Float?, targetData: List<TargetProgressData>
    ) {
        colorList = ArrayList()
        val values = ArrayList<Entry>()
        val values1 = ArrayList<Entry>()
        var i = 0
        while (i < targetData.size) {
            var value = 0f
            var value1 = 0f
//            if (type == 1) {
            targetData[i].key?.let {
                value = it
            }
            targetValue?.let {
                value1 = it
            }
//            } else {
//                getData()[i].let {
//                    value = it
//                }
//            }
            values.add(BarEntry(i.toFloat(), value))
            values1.add(BarEntry(i.toFloat(), value1))
            i++
        }

        val set1 = LineDataSet(values, "")
        set1.valueFormatter = MyValueFormatter()
        set1.setDrawIcons(false)
        set1.setDrawValues(false)
        set1.setDrawCircles(true)
        set1.setCircleColor(ContextCompat.getColor(requireContext(), R.color.line_color))
        set1.setDrawCircleHole(false)
        set1.mode = LineDataSet.Mode.LINEAR
        set1.lineWidth = 2f
        set1.color = ContextCompat.getColor(requireContext(), R.color.line_color)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

//        if (type == 1) {
        val set2 = LineDataSet(values1, "")
        set2.setDrawIcons(false)
        set2.setDrawValues(false)
        set2.setDrawCircles(false)
        set2.circleRadius = 1f
        set2.setCircleColor(ContextCompat.getColor(requireContext(), R.color.black))
        set2.lineWidth = 2f
        set2.color = ContextCompat.getColor(requireContext(), R.color.green_color_text)
        set2.mode = LineDataSet.Mode.LINEAR
        dataSets.add(set2)
//        }

        val data = LineData(dataSets)
        data.setValueTextSize(8f)
        data.setValueTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.dark_grey_txt_color_80
            )
        )
        data.setValueTypeface(tfLight)
        barChart.data = data
    }

    private fun getDataX(targetData: List<TargetProgressData>): List<String> {
        val xAxis = ArrayList<String>()
        for (index in targetData.withIndex()) {
            xAxis.add((index.index + 1).toString())
        }
        return xAxis//arrayListOf("1", "2", "3", "4", "5", "6")
    }

    private fun initAverageOrderChart() {
        binding.bcAverageOrderValue.invalidate()

        binding.bcAverageOrderValue.setDrawBarShadow(false)
        binding.bcAverageOrderValue.setDrawValueAboveBar(false)
        binding.bcAverageOrderValue.description.isEnabled = false
        binding.bcAverageOrderValue.setPinchZoom(false)
        binding.bcAverageOrderValue.isDoubleTapToZoomEnabled = false
        binding.bcAverageOrderValue.isAutoScaleMinMaxEnabled = true
        binding.bcAverageOrderValue.setDrawGridBackground(false)
        binding.bcAverageOrderValue.isDragEnabled = false
        binding.bcAverageOrderValue.setScaleEnabled(false)
        binding.bcAverageOrderValue.xAxis.setLabelCount(xAxisBarLabelsList.size, false)

        binding.bcAverageOrderValue.xAxis.valueFormatter =
            IndexAxisValueFormatter(xAxisBarLabelsList)

        binding.bcAverageOrderValue.xAxis.setDrawGridLines(false)
        binding.bcAverageOrderValue.axisLeft.setDrawGridLines(true)
        binding.bcAverageOrderValue.axisRight.setDrawGridLines(false)

        binding.bcAverageOrderValue.axisLeft.isEnabled = true
        binding.bcAverageOrderValue.axisRight.isEnabled = false
        binding.bcAverageOrderValue.xAxis.axisLineColor =
            homeActivity.resources.getColor(R.color.dark_grey_txt_color2)
        binding.bcAverageOrderValue.xAxis.setDrawLabels(true)
        binding.bcAverageOrderValue.xAxis.textSize = 11f
        binding.bcAverageOrderValue.legend.isEnabled = false
        binding.bcAverageOrderValue.xAxis.typeface = tfLight

        binding.bcAverageOrderValue.setViewPortOffsets(85f, 60f, 0f, 50f)
        binding.bcAverageOrderValue.setTouchEnabled(true)
        binding.bcAverageOrderValue.isScaleXEnabled = false
        binding.bcAverageOrderValue.isScaleYEnabled = false
        binding.bcAverageOrderValue.setPinchZoom(false)

        val yAxis = binding.bcAverageOrderValue.axisLeft
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawZeroLine(true)
        yAxis.enableGridDashedLine(25f, 15f, 2f)


        binding.bcAverageOrderValue.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.bcAverageOrderValue.axisLeft.axisMinimum = 0f
        binding.bcAverageOrderValue.axisRight.axisMinimum = 0f
        setData()
    }

    private var colorsList = java.util.ArrayList<Int>()
    private var barListAOV = ArrayList<BarEntry>()
    private fun setData() {
        colorsList.clear()
        colorsList.add(ContextCompat.getColor(homeActivity, R.color.line_color))
        val set1 = BarDataSet(barListAOV, "")
        set1.setDrawIcons(false)
        set1.colors = colorsList
        set1.setDrawValues(false)
        set1.highLightColor = Color.TRANSPARENT
        set1.highLightAlpha = 0
        set1.valueTypeface = tfLight
        val dataSets = java.util.ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        if (set1.values.size < 2) {
            data.barWidth = 0.1f
        } else {
            data.barWidth = 0.6f
        }

        binding.bcAverageOrderValue.data = data
        binding.bcAverageOrderValue.notifyDataSetChanged()

        binding.bcAverageOrderValue.animateY(0)
        binding.bcAverageOrderValue.invalidate()
    }
}