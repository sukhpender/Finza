package com.riggle.plug.ui.hr

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.calendar_view.DayDecorator
import com.riggle.plug.calendar_view.OnDateSelectListener
import com.riggle.plug.calendar_view.OnMonthChangeListener
import com.riggle.plug.data.model.GetLeaveData
import com.riggle.plug.databinding.FragmentMyHRBinding
import com.riggle.plug.databinding.HolderMyLeavesBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MyHRFragment : BaseFragment<FragmentMyHRBinding>() {

    private val viewModel: MyHRFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    private lateinit var calendarStart: Calendar
    private lateinit var calendarEnd: Calendar
    private lateinit var today: Calendar
    private lateinit var calendar: Calendar
    private var tfLight: Typeface? = null

    companion object{
        var userId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.toolbar.tvHeaderTitle.text = getString(R.string.my_hr)

        calendarStart = Calendar.getInstance()
        calendarEnd = Calendar.getInstance()
        today = Calendar.getInstance()

        if (sharedPrefManager.getCurrentUser()?.user?.is_manager == true) {
          //  binding.tvApprove.visibility = View.VISIBLE
        } else {
          //  binding.tvApprove.visibility = View.GONE
        }

        initRecyclerView()
        observeResponse()
        initCalendar()
    }

    private fun initCalendar() {
        calendar = Calendar.getInstance()
        tfLight = ResourcesCompat.getFont(homeActivity, R.font.poppins_regular)


        binding.calendarView.onMonthChangeListener = OnMonthChangeListener { oldMonth, newMonth ->
            calendar = newMonth
            apiCall(
                userId,
                (calendar.get(Calendar.MONTH) + 1).toString(),
                calendar.get(Calendar.YEAR).toString()
            )
        }


        binding.calendarView.onDateSelectListener = OnDateSelectListener { selectedDate ->
//            val date = String.format("%02d", selectedDate.get(Calendar.DAY_OF_MONTH))
//            val month = selectedDate.getDisplayName(
//                Calendar.MONTH,
//                Calendar.LONG,
//                Locale.getDefault()
//            )
//            val year = selectedDate.get(Calendar.YEAR).toString()
//
//            Toast.makeText(requireContext(), "$month $date, $year", Toast.LENGTH_SHORT).show()
        }

//        binding.calendarView.setOnDayClickListener { eventDay ->
//            val clickedDayCalendar = eventDay.calendar
//            calendar = clickedDayCalendar
//            val simpleDateFormat = SimpleDateFormat("dd", Locale.US)
//            val date = simpleDateFormat.format(clickedDayCalendar.time)
//        }

        apiCall(
            userId,
            (calendar.get(Calendar.MONTH) + 1).toString(), calendar.get(Calendar.YEAR).toString()
        )

    }

    private fun apiCall(userId: String,month: String, year: String) {
        val request = HashMap<String, String>()
        request["expand"] = "user,approved_by,rejected_by"
        request["month"] = month
        request["year"] = year
        request["user"] = userId
        viewModel.getLeaveList(sharedPrefManager.getSessionId()!!, request)

        viewModel.getLeaveCount(sharedPrefManager.getSessionId()!!, month, year)
    }

    private fun observeResponse() {
        /*
        viewModel.obrAddLeave.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    showSuccessToast(it.message.toString())
                    apiCall(
                        (calendar.get(Calendar.MONTH) + 1).toString(),
                        calendar.get(Calendar.YEAR).toString()
                    )
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {
                    showHideLoader(false)
                }
            }
        })
*/

        viewModel.obrLeaveList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)

                    if (it.data != null){
                        it.data.results.let { list ->
                            /* if (list.isNotEmpty()) {
                                 binding.llCount.visibility = View.VISIBLE
                             } else {
                                 binding.llCount.visibility = View.GONE
                             }*/
//                        var count = 0
//                        for (index in list) {
//                            if (index.type.equals("full_day")) {
//                                count++
//                            }
//                        }
//                        binding.tvLeaveCount.text = count.toString()
                            listAdapter?.list = list
                            updateCalendar(list)
                        } ?: kotlin.run {
                           // binding.llCount.visibility = View.GONE
                        }
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {
                    showHideLoader(false)
                }
            }
        }

        viewModel.obrLeaveCount.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    it.data?.let { list ->
                        binding.bean = list
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {
                    showHideLoader(false)
                }
            }
        }
    }

    private var listAdapter: SimpleRecyclerViewAdapter<GetLeaveData, HolderMyLeavesBinding>? = null
    private fun initRecyclerView() {
        listAdapter =
            SimpleRecyclerViewAdapter<GetLeaveData, HolderMyLeavesBinding>(R.layout.holder_my_leaves,
                BR.bean
            ) { v, m, pos ->
                when (v?.id) {
                    R.id.tvReject -> {
                        //  addLeave(m)
                    }

                    R.id.tvApprove -> {
                        m?.let { bean ->
                            //    cancelLeave(bean)
                        }
                    }
                }
            }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvDataList.layoutManager = layoutManager
        binding.rvDataList.adapter = listAdapter
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(
            ColorDrawable(
                getColor(
                    homeActivity, R.color.dark_grey_txt_color15
                )
            )
        )
        binding.rvDataList.addItemDecoration(dividerItemDecoration)
    }


    private fun updateCalendar(list: List<GetLeaveData>) {
        val listdata = ArrayList<DayDecorator>()
        for (index in list) {
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val startDate: Date? = sdf.parse(index.start_date.toString())
                val endDate: Date? = sdf.parse(index.end_date.toString())
                if (startDate != null) {
                    start.time = startDate
                }
                if (endDate != null) {
                    end.time = endDate
                }

                val dates = ImageBindingAdapter.getDaysBetweenDates(start.time, end.time)

                when (index.status) {
                    "pending" -> {
                        for (date in dates) {
                            start.time = date
                            listdata.add(
                                DayDecorator(
                                    start,
                                    getColor(requireContext(), R.color.white),
                                    getColor(requireContext(), R.color.pending_color)
                                )
                            )
                        }
                        listdata.add(
                            DayDecorator(
                                end,
                                getColor(requireContext(), R.color.white),
                                getColor(requireContext(), R.color.pending_color)
                            )
                        )
                    }

                    "approved" -> {
                        for (date in dates) {
                            start.time = date
                            listdata.add(
                                DayDecorator(
                                    start,
                                    getColor(requireContext(), R.color.white),
                                    getColor(requireContext(), R.color.orange)
                                )
                            )
                        }
                        listdata.add(
                            DayDecorator(
                                end,
                                getColor(requireContext(), R.color.white),
                                getColor(requireContext(), R.color.orange)
                            )
                        )
                    }

                    "rejected" -> {
                        for (date in dates) {
                            start.time = date
                            listdata.add(
                                DayDecorator(
                                    start,
                                    getColor(requireContext(), R.color.white),
                                    getColor(requireContext(), R.color.rejected)
                                )
                            )
                        }
                        listdata.add(
                            DayDecorator(
                                end,
                                getColor(requireContext(), R.color.white),
                                getColor(requireContext(), R.color.rejected)
                            )
                        )
                    }
                }
                binding.calendarView.dayDecorators = listdata
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //binding.calendarView.selectedDates = listdata
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.tvApprove -> { // navigateToApproveHRFragment
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToApproveHRFragment
                    )
                }
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_my_h_r
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


}