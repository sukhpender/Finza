package com.riggle.plug.ui.home.home.homeInsights

import android.graphics.Color
import android.graphics.Typeface
import android.icu.util.Calendar
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.HomeInsightsLastDaysResponseModel
import com.riggle.plug.data.model.Placed2
import com.riggle.plug.data.model.Retailer
import com.riggle.plug.data.model.SubCategory2
import com.riggle.plug.databinding.FragmentHomeInsightsBinding
import com.riggle.plug.databinding.HolderHomeInsightPieBinding
import com.riggle.plug.databinding.HolderLastDaysBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.sales.SalesPersonLiveOfflineFragment
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.chart.CustomMarkerView
import com.riggle.plug.utils.chart.CustomXAxisRendererMonthly
import com.riggle.plug.utils.chart.CustomXAxisRendererWeekly
import com.riggle.plug.utils.chart.IntValueFormatter
import com.riggle.plug.utils.chart.MyValueFormatter
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@AndroidEntryPoint
class HomeInsightsFragment : BaseFragment<FragmentHomeInsightsBinding>() {

    private val viewModel: HomeInsightsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var brandsId = ""
    private var sDate = ""
    private var eDate = ""
    private var interval = ""

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        tfLight = ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold)

        brandsId = ""
        sDate = sharedPrefManager.getStartDate()!!
        eDate = sharedPrefManager.getEndDate()!!

        setupPieList()
        initLastDaysAdapter()
        getSalesCountData()
        //  getFiltersData(brandsId)
        //getSalesPersons()

        // brand ids filter
        brandsId = sharedPrefManager.getBrandsId().toString()

        // SalesUser Filter
        val salesUserId = sharedPrefManager.getSalesPerson()
        var salesUser = ""
        if (salesUserId != null) {
            salesUser = salesUserId
        } else {
            salesUser = ""
        }

        // interval filter
        val startDate = SimpleDateFormat("yyyy-MM-dd").parse(sharedPrefManager.getStartDate())
        val endDate = SimpleDateFormat("yyyy-MM-dd").parse(sharedPrefManager.getEndDate())
        val diff = endDate.time - startDate.time
        val numOfDays = (diff / (1000 * 60 * 60 * 24)).toInt()
        if (numOfDays <= 31) {
            interval = "daily"
        } else if (numOfDays in 32..45) {
            interval = "weekly"
        } else {
            interval = "monthly"
        }
        Log.e("interval1234", interval)

        // channel partner filter
        val cpId = sharedPrefManager.getCP()
        var cpIdValue = ""
        if (cpId != null) {
            cpIdValue = cpId
        } else {
            cpIdValue = ""
        }

        getLastDaysData(
            brandsId,
            sharedPrefManager.getOrderType()!!,
            sDate,
            eDate,
            salesUser,
            cpIdValue
        )
        getSubCat(brandsId, sharedPrefManager.getOrderType()!!, sDate, eDate, salesUser, cpIdValue)
        getRetailers(
            brandsId,
            sharedPrefManager.getOrderType()!!,
            sDate,
            eDate,
            salesUser,
            interval,
            cpIdValue
        )
        getPlacedOrders(
            brandsId,
            sharedPrefManager.getOrderType()!!,
            sDate,
            eDate,
            salesUser,
            interval,
            cpIdValue
        )
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvDaily -> {

                }

                R.id.tvWeekly -> {

                }

                R.id.tvMonthly -> {

                }

                R.id.llChannelPartners -> {
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMyBrandProductChannelPartnerFragment
                    )
                }

                R.id.llSalesPerson -> {
                    Constants.SALES_MANAGER = 0
                    Constants.SALES_MANAGER_NAME = ""
                    Constants.SALES_DESIGNATION = 0
                    Constants.SALES_DESIGNATION_Name = ""
                    Constants.SALES_VAN_USER = false
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMyBrandProductSalesFragment
                    )
                }

                R.id.tvLive -> {
                    SalesPersonLiveOfflineFragment.type = "Live Users"
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSalesLiveOfflineFragment
                    )
                }

                R.id.tvOffline -> {
                    SalesPersonLiveOfflineFragment.type = "Offline Users"
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSalesLiveOfflineFragment
                    )
                }
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home_insights
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


    private lateinit var lastDaysAdapter: SimpleRecyclerViewAdapter<HomeInsightsLastDaysResponseModel, HolderLastDaysBinding>
    private fun initLastDaysAdapter() {
        lastDaysAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_last_days, BR.bean
        ) { v, m, pos ->

        }
        binding.rvLAstDays.adapter = lastDaysAdapter
    }

    private fun getSalesCountData() {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val sDate = format.format(cal.time)

        val query = HashMap<String, String>()
        query["date"] = sDate
        viewModel.getHomeSalesCountData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getFiltersData(brands: String) {
        val query = HashMap<String, String>()
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val sDate = format.format(cal.time)
        val eDate = format.format(cal.time)
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["brands"] = brands
        query["state"] = ""
        query["city"] = ""
        query["order_type"] = sharedPrefManager.getOrderType()!!
        query["filters"] = "true"
        viewModel.getFiltersData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getSalesPersons() {
        val query = HashMap<String, String>()
        query["is_manager"] = ""
        query["fields"] = "id,full_name"
        viewModel.getHomeSalesPersonsListData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getRetailers(
        brands: String,
        orderType: String,
        sDate: String,
        eDate: String,
        salesUser: String,
        interval: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["retailers_count"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["order_type"] = orderType
        query["city"] = ""
        query["state"] = ""
        query["tab_name"] = "insights"
        query["interval"] = interval
        query["sales_user"] = salesUser
        query["company"] = companyId
        viewModel.getHomeRetailersData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getSubCat(
        brands: String,
        orderType: String,
        sDate: String,
        eDate: String,
        salesUser: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["sub_category_analysis"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["order_type"] = orderType
        query["city"] = ""
        query["state"] = ""
        query["tab_name"] = "insights"
        query["sales_user"] = salesUser
        query["company"] = companyId
        viewModel.getHomeSubCatData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getPlacedOrders(
        brands: String,
        orderType: String,
        sDate: String,
        eDate: String,
        salesUser: String,
        interval: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["placed_orders_overview"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["order_type"] = orderType
        query["city"] = ""
        query["state"] = ""
        query["tab_name"] = "insights"
        query["sales_user"] = salesUser
        query["interval"] = interval
        query["company"] = companyId
        viewModel.getHomePlacedOrdersData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getLastDaysData(
        brands: String,
        orderType: String,
        sDate: String,
        eDate: String,
        salesUser: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["past_week_summary"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["order_type"] = orderType
        query["city"] = ""
        query["state"] = ""
        query["tab_name"] = "insights"
        query["sales_user"] = salesUser
        query["company"] = companyId
        viewModel.getHomeInsightsLastDays(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun initObservers() {

        viewModel.obrLastDays.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        lastDaysAdapter.list = it.data
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrFiltersData.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {

                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrSalesCountData.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.tvSalesCount.text = "(" + it.data.total_user.toString() + ")"
                        binding.tvOnlineValue.text = "(" + it.data.active_users.toString() + ")"
                        binding.tvOfflineValue.text = "(" + it.data.inactive_users.toString() + ")"
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrSalesPersonsList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {

                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrHomeRetailers.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        xAxisBarLabelsList.clear()
                        if (interval == "monthly") {
                            xAxisBarLabelsListMonth.clear()
                            for (i in 0 until it.data.retailers.size) {
                                xAxisBarLabelsListMonth.add(ImageBindingAdapter.getMonthlyName(it.data.retailers[i].month))
                            }

                            setUpLineChart(binding.lcHome, it.data.retailers)
                        }

                        if (interval == "weekly") {
                            xAxisBarLabelsList.clear()
                            for (i in 0 until it.data.retailers.size) {
                                xAxisBarLabelsList.add(
                                    ImageBindingAdapter.getMonthNameWeeklyInsightRetailers(it.data.retailers[i])
                                )
                            }

                            setUpLineChart(binding.lcHome, it.data.retailers)
                        }

                        if (interval == "daily") {
                            xAxisBarLabelsList.clear()
                            setUpLineChart(binding.lcHome, it.data.retailers)
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

        viewModel.obrHomePlacedOrders.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        // setUpLineChartOrdersPlaced(binding.lcHomeOrders, it.data.orders.placed)

                        if (interval == "monthly") {
                            xAxisBarLabelsListMonth.clear()
                            for (i in 0 until it.data.orders.placed.size) {
                                xAxisBarLabelsListMonth.add(ImageBindingAdapter.getMonthlyName(it.data.orders.placed[i].month))
                            }

                            setUpLineChartOrdersPlaced(binding.lcHomeOrders, it.data.orders.placed)
                        }

                        if (interval == "weekly") {
                            xAxisBarLabelsList.clear()
                            for (i in 0 until it.data.orders.placed.size) {
                                xAxisBarLabelsList.add(
                                    ImageBindingAdapter.getMonthNameWeeklyInsightPlacedOrders(it.data.orders.placed[i])
                                )
                            }

                            setUpLineChartOrdersPlaced(binding.lcHomeOrders, it.data.orders.placed)
                        }

                        if (interval == "daily") {
                            xAxisBarLabelsList.clear()
                            setUpLineChartOrdersPlaced(binding.lcHomeOrders, it.data.orders.placed)
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

        viewModel.obrSubCat.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.tv7.text = "(₹ ${it.data.subcategory_analysis.order_value})"
                        it.data.subcategory_analysis.let { it ->
                            it.sub_categories.let {
                                try {
                                    for (index in it.withIndex()) {
                                        if (getColors().size >= index.index) {
                                            it[index.index].color = getColors()[index.index]
                                        } else {
                                            it[index.index].color = "#E5E4E2"
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                if (pieAdapter?.list?.isNotEmpty() == true) {
                                    pieAdapter?.clearList()
                                }
                                pieAdapter?.list = it
                                setUpPieChart(it)

                                if (it.isNotEmpty()) {
                                    binding.iv6.visibility = View.GONE
                                } else {
                                    binding.iv6.visibility = View.VISIBLE
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
    }

    private fun setUpLineChartOrdersPlaced(
        barChart: LineChart, targetData: List<Placed2>
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
        barChart.setTouchEnabled(false)

        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false

        barChart.setMaxVisibleValueCount(31)
        barChart.setDrawGridBackground(false)
        // barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getDataPlacedOrders(targetData))

        if (interval == "daily") {
            targetData.let {
                if (it.size > 15) {
                    barChart.xAxis.labelCount = it.size / 2
                } else {
                    barChart.xAxis.labelCount = it.size
                }
            }

            barChart.extraBottomOffset = 10f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 10f
            barChart.extraRightOffset = 5f
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getDataPlacedOrders(targetData))
        }

        if (interval == "weekly" && xAxisBarLabelsList.size != 0) {
            xAxisBarLabelsList.let {
                if (it.size > 8) {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size / 2, false)
                } else {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size, false)
                }
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsList)

            barChart.extraBottomOffset = 65f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 0f
            barChart.extraRightOffset = 20f
            barChart.setXAxisRenderer(
                CustomXAxisRendererWeekly(
                    barChart.viewPortHandler,
                    barChart.xAxis,
                    barChart.getTransformer(YAxis.AxisDependency.RIGHT)
                )
            )
        }

        if (interval == "monthly") {
            xAxisBarLabelsListMonth.let {
                barChart.xAxis.setLabelCount(xAxisBarLabelsListMonth.size, false)/*if (it.size > 8){
                    barChart.xAxis.setLabelCount(xAxisBarLabelsListMonth.size / 2, false)
                }else{
                    barChart.xAxis.setLabelCount(xAxisBarLabelsListMonth.size, false)
                }*/
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsListMonth)

            barChart.extraBottomOffset = 40f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 10f
            barChart.extraRightOffset = 5f
            barChart.setXAxisRenderer(
                CustomXAxisRendererMonthly(
                    barChart.viewPortHandler,
                    barChart.xAxis,
                    barChart.getTransformer(YAxis.AxisDependency.RIGHT)
                )
            )
        }


        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = tfLight
        xAxis.setDrawGridLines(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color)
        xAxis.granularity = 1f // only intervals of 1 day

        targetData.let {
            //xAxis.labelCount = it.size / 2
            if (it.size > 15) {
                barChart.xAxis.labelCount = it.size / 2
            } else {
                barChart.xAxis.labelCount = it.size
            }
        }

        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.typeface = tfLight
        leftAxis.setLabelCount(6, true)

        leftAxis.textColor =
            ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color_80)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 10f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.typeface = tfLight
        rightAxis.setLabelCount(8, false)

        val yAxis1 = barChart.axisLeft
        yAxis1.setDrawAxisLine(false)
        yAxis1.setDrawZeroLine(true)
        yAxis1.enableGridDashedLine(25f, 15f, 2f)

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

        barChart.animateY(600)
        setBarDataOrders(barChart, targetData)
    }

    private var totalOrderAmountCount = 0
    private var colorList1 = ArrayList<Int>()
    private fun setBarDataOrders(
        barChart: LineChart, targetData: List<Placed2>
    ) {
        colorList1 = ArrayList()
        val values = ArrayList<Entry>()
        var i = 0
        totalOrderAmountCount = 0
        while (i < targetData.size) {
            var value = 0f
            targetData[i].revenue.let {
                totalOrderAmountCount += it.toInt()
            }
            targetData[i].revenue.let {
                value = it.toFloat()
            }
            values.add(BarEntry(i.toFloat(), value))
            i++
        }

        ///binding.tv5.text = "(₹ $totalOrderAmountCount)"
        binding.tv5.let {
            val decim = DecimalFormat("#,###.##")
            val asdf = decim.format(totalOrderAmountCount.toFloat())
            it.text = "(₹ $asdf)"
        }

        val set1 = LineDataSet(values, "")
        set1.valueFormatter = MyValueFormatter()
        set1.setDrawIcons(false)
        set1.setDrawValues(false)
        set1.setDrawCircles(true)
        set1.setCircleColor(ContextCompat.getColor(requireContext(), R.color.line_color))
        set1.setDrawCircleHole(false)
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set1.lineWidth = 2f
        set1.circleSize = 4f
        set1.circleRadius = 3f
        set1.formLineWidth = 0f
        set1.setDrawFilled(false)
        // set1.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.red_gradient)
        set1.color = ContextCompat.getColor(requireContext(), R.color.line_color)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        set1.valueFormatter = IntValueFormatter()

        val data = LineData(dataSets)
        data.setValueTextSize(6f)
        data.setValueTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.dark_grey_txt_color
            )
        )
        data.setValueTypeface(tfLight)
        barChart.data = data
    }

    private var xAxisBarLabelsList = ArrayList<String>()
    private var xAxisBarLabelsListMonth = ArrayList<String>()
    private var tfLight: Typeface? = null
    private fun setUpLineChart(
        barChart: LineChart, targetData: List<Retailer>
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
        barChart.setTouchEnabled(false)

        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false

        barChart.setMaxVisibleValueCount(31)
        barChart.setDrawGridBackground(false)
        // barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getDataX(targetData))

        if (interval == "daily") {
            targetData.let {
                if (it.size > 15) {
                    barChart.xAxis.labelCount = it.size / 2
                } else {
                    barChart.xAxis.labelCount = it.size
                }
            }

            barChart.extraBottomOffset = 10f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 10f
            barChart.extraRightOffset = 5f
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getDataX(targetData))
        }

        if (interval == "weekly" && xAxisBarLabelsList.size != 0) {
            xAxisBarLabelsList.let {
                if (it.size > 8) {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size / 2, false)
                } else {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size, false)
                }
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsList)

            barChart.extraBottomOffset = 65f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 0f
            barChart.extraRightOffset = 20f
            barChart.setXAxisRenderer(
                CustomXAxisRendererWeekly(
                    barChart.viewPortHandler,
                    barChart.xAxis,
                    barChart.getTransformer(YAxis.AxisDependency.RIGHT)
                )
            )
        }

        if (interval == "monthly") {
            xAxisBarLabelsListMonth.let {
                if (it.size > 12) {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsListMonth.size / 2, false)
                } else {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsListMonth.size, false)
                }
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsListMonth)

            barChart.extraBottomOffset = 40f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 10f
            barChart.extraRightOffset = 5f
            barChart.setXAxisRenderer(
                CustomXAxisRendererMonthly(
                    barChart.viewPortHandler,
                    barChart.xAxis,
                    barChart.getTransformer(YAxis.AxisDependency.RIGHT)
                )
            )
        }


        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = tfLight
        xAxis.setDrawGridLines(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color_80)
        xAxis.granularity = 1f // only intervals of 1 day


        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.typeface = tfLight
        leftAxis.setLabelCount(6, true)

        leftAxis.textColor =
            ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color_80)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 10f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val yAxis1 = barChart.axisLeft
        yAxis1.setDrawAxisLine(false)
        yAxis1.setDrawZeroLine(true)
        yAxis1.enableGridDashedLine(25f, 15f, 2f)

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

        barChart.animateY(600)
        setBarData(barChart, targetData)
    }

    private var totalRetailerCount = 0
    private var colorList = ArrayList<Int>()
    private fun setBarData(
        barChart: LineChart, targetData: List<Retailer>
    ) {
        colorList = ArrayList()
        val values = ArrayList<Entry>()
        var i = 0
        totalRetailerCount = 0
        while (i < targetData.size) {
            var value = 0f
            targetData[i].transacting_count.let {
                totalRetailerCount += it
            }
            targetData[i].transacting_count.let {
                value = it.toFloat()
            }
            values.add(BarEntry(i.toFloat(), value))
            i++
        }

        binding.tv3.text = "($totalRetailerCount)"

        val set1 = LineDataSet(values, "")
        set1.valueFormatter = MyValueFormatter()
        set1.setDrawIcons(false)
        set1.setDrawValues(false)
        set1.setDrawCircles(true)
        set1.setCircleColor(ContextCompat.getColor(requireContext(), R.color.pending_color))
        set1.setDrawCircleHole(false)
        set1.mode = LineDataSet.Mode.LINEAR
        set1.lineWidth = 2f
        set1.circleSize = 4f
        set1.circleRadius = 3f
        set1.formLineWidth = 0f
        // set1.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.blue_gradient)
        // set1.setDrawFilled(true)
        set1.lineWidth = 2f
        set1.color = ContextCompat.getColor(requireContext(), R.color.pending_color)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        set1.valueFormatter = IntValueFormatter()

        val data = LineData(dataSets)
        data.setValueTextSize(6f)
        data.setValueTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.dark_grey_txt_color
            )
        )
        data.setValueTypeface(tfLight)
        barChart.data = data
    }

    private fun getDataX(targetData: List<Retailer>): List<String> {
        val xAxis = ArrayList<String>()
        for (index in targetData.withIndex()) {
            xAxis.add((index.index + 1).toString())
        }
        return xAxis//arrayListOf("1", "2", "3", "4", "5", "6")
    }

    private fun getDataPlacedOrders(targetData: List<Placed2>): List<String> {
        val xAxis = ArrayList<String>()
        for (index in targetData.withIndex()) {
            xAxis.add((index.index + 1).toString())
        }
        return xAxis//arrayListOf("1", "2", "3", "4", "5", "6")
    }

    private var pieAdapter: SimpleRecyclerViewAdapter<SubCategory2, HolderHomeInsightPieBinding>? =
        null

    private fun setupPieList() {
        pieAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_home_insight_pie, BR.bean
        ) { v, m, pos -> }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPieChartValue.layoutManager = layoutManager
        binding.rvPieChartValue.adapter = pieAdapter
    }

    private fun setUpPieChart(graphData: List<SubCategory2>) {
        try {
            binding.pieChartView.clear()

            val pieEntries: ArrayList<PieEntry> = ArrayList()
            val label = ""
            val colors: ArrayList<Int> = ArrayList()
            for (type in graphData.withIndex()) {
                graphData[type.index].percentage_revenue.let { perc ->
                    if (perc > 0) {
                        pieEntries.add(PieEntry(perc, ""/*type*/))
                        when (type.index) {
                            0 -> {
                                colors.add(Color.parseColor("#e34dfc"))
                            }

                            1 -> {
                                colors.add(Color.parseColor("#f3ec39"))
                            }

                            2 -> {
                                colors.add(Color.parseColor("#fc8c4d"))
                            }

                            3 -> {
                                colors.add(Color.parseColor("#4d93fc"))
                            }

                            4 -> {
                                colors.add(Color.parseColor("#9a4dfc"))
                            }

                            5 -> {
                                colors.add(Color.parseColor("#8aed5b"))
                            }

                            else -> {
                                colors.add(Color.parseColor("#F3AC23"))
                            }
                        }
                    }
                }
            }
            val pieDataSet = PieDataSet(pieEntries, label)
            pieDataSet.valueTextSize = 12f
            pieDataSet.colors = colors
            pieDataSet.sliceSpace = 1.5f
            val pieData = PieData(pieDataSet)
            pieData.setDrawValues(false)

            binding.pieChartView.holeRadius = 0f
            binding.pieChartView.transparentCircleRadius = 0f
            binding.pieChartView.isDrawHoleEnabled = true
            binding.pieChartView.setDrawSlicesUnderHole(true)
            binding.pieChartView.minAngleForSlices = 10f
            binding.pieChartView.legend.isEnabled = false
            binding.pieChartView.description.isEnabled = false
            binding.pieChartView.isRotationEnabled = false
            binding.pieChartView.setTouchEnabled(false)
            binding.pieChartView.data = pieData
            binding.pieChartView.animateY(1200, Easing.Linear)

            binding.pieChartView.invalidate()
            //          binding.pieChartView.renderer = RoundedSlicesPieChartRenderer1(binding.pieChartView,binding.pieChartView.animator,binding.pieChartView.viewPortHandler)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getColors(): ArrayList<String> {
        return arrayListOf("#e34dfc", "#f3ec39", "#fc8c4d", "#4d93fc", "#9a4dfc", "#8aed5b")
    }
}