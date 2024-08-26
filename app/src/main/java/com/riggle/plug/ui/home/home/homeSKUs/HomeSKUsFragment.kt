package com.riggle.plug.ui.home.home.homeSKUs

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
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
import com.riggle.plug.data.model.HomeSKUsItemDetailsResponseModel
import com.riggle.plug.data.model.SellingSku2
import com.riggle.plug.data.model.SubCategory1
import com.riggle.plug.databinding.FragmentHomeSKUsBinding
import com.riggle.plug.databinding.HolderHomeSkusBinding
import com.riggle.plug.databinding.HolderHomeSkusPieBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.chart.CustomMarkerView
import com.riggle.plug.utils.chart.MyValueFormatter
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class HomeSKUsFragment : BaseFragment<FragmentHomeSKUsBinding>() {

    private val viewModel: HomeSKUsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var brandsId = ""
    private var sDate = ""
    private var eDate = ""
    var cpIdValue = ""
    var salesUser = ""

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home_s_k_us
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        tfLight = ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)
        setupPieList()
        initTopAdapter()

        sDate = sharedPrefManager.getStartDate()!!
        eDate = sharedPrefManager.getEndDate()!!

        brandsId = ""
        brandsId = sharedPrefManager.getBrandsId().toString()

        // channel partner filter
        val cpId = sharedPrefManager.getCP()
        if (cpId != null) {
            cpIdValue = cpId
        } else {
            cpIdValue = ""
        }

        // SalesUser Filter
        val salesUserId = sharedPrefManager.getSalesPerson()
        if (salesUserId != null) {
            salesUser = salesUserId
        } else {
            salesUser = ""
        }

        getData(
            brandsId,
            sDate,
            eDate,
            sharedPrefManager.getOrderType()!!,
            salesUser,
            cpIdValue,
            "",
            ""
        )
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private fun initObservers() {

        viewModel.obrSkus.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.subcategory_analysis.order_value != null) {
                            val decim = DecimalFormat("#,###.##")
                            val asdf =
                                decim.format(it.data.subcategory_analysis.order_value.toFloat())
                            binding.tv1.text = "(₹ $asdf)"
                        }
                        it.data.subcategory_analysis.let { it ->
                            it.sub_categories.let {

                                if (it.size > 6) {
                                    val lp = RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT, 500
                                    )
                                    binding.rvPieChartValue.layoutParams = lp
                                }
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
                                if (it != null && it.size != 0) {
                                    binding.iv1.visibility = View.GONE
                                } else {
                                    binding.iv1.visibility = View.VISIBLE
                                }
                            }
                        }
                        if (it.data.selling_skus.isNotEmpty()) {
                            topAdapter.list = it.data.selling_skus
                            binding.llNoDataBeatTopSKUs.visibility = View.GONE
                            binding.llTopSKUs.visibility = View.VISIBLE
                        } else {
                            binding.llNoDataBeatTopSKUs.visibility = View.VISIBLE
                            binding.llTopSKUs.visibility = View.GONE
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

        viewModel.obrSkusItemDetails.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    binding.clHomeRetailers.visibility = View.VISIBLE
                    if (it.data != null) {
                        setUpLineChart(binding.lcHome, it.data)

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

    private fun getData(
        brands: String,
        sDate: String,
        eDate: String,
        orderType: String,
        salesUser: String,
        companyId: String,
        state: String,
        city: String
    ) {
        val query = HashMap<String, String>()
        query["sub_category"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["tab_name"] = "sub_category"
        query["order_type"] = orderType
        query["sales_user"] = salesUser
        query["company"] = companyId
        query["city"] = city
        query["state"] = state
        viewModel.getHomeSKUsData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getItemDetailsData(
        productId: String,
        brands: String,
        sDate: String,
        eDate: String,
        orderType: String,
        salesUser: String,
        companyId: String,
        state: String,
        city: String
    ) {
        val query = HashMap<String, String>()
        query["product_summary"] = "true"
        query["product"] = productId
        query["sub_category"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["tab_name"] = "sub_category"
        query["order_type"] = orderType
        query["sales_user"] = salesUser
        query["company"] = companyId
        query["city"] = city
        query["state"] = state
        viewModel.getHomeSKUsItemDetailsData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    var totalRevenue = 0
    private fun setUpPieChart(graphData: List<SubCategory1>) {
        try {
            binding.pieChartView.clear()

            val pieEntries: ArrayList<PieEntry> = ArrayList()
            val label = ""
            val colors: ArrayList<Int> = ArrayList()
            totalRevenue = 0
            for (type in graphData.withIndex()) {
                graphData[type.index].percentage_revenue?.let { perc ->
                    totalRevenue += perc.toInt()
                    if (perc > 0) {
                        pieEntries.add(PieEntry(perc, ""/*type*/))
                        when (type.index) {
                            0 -> {
                                colors.add(Color.parseColor("#2563EB"))
                            }

                            1 -> {
                                colors.add(Color.parseColor("#C084FC"))
                            }

                            2 -> {
                                colors.add(Color.parseColor("#FC8C4D"))
                            }

                            3 -> {
                                colors.add(Color.parseColor("#22C55E"))
                            }

                            4 -> {
                                colors.add(Color.parseColor("#F3AC23"))
                            }

                            5 -> {
                                colors.add(Color.parseColor("#c1e022"))
                            }

                            else -> {
                                colors.add(Color.parseColor("#F3AC23"))
                            }
                        }
                    }
                }
            }

            /*val decim = DecimalFormat("#,###.##")
            val asdf = decim.format(totalRevenue.toFloat())
            binding.tv1.text =  "(₹ $asdf)"
*/
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

            binding.pieChartView.animateY(1200, Easing.Linear)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var pieAdapter: SimpleRecyclerViewAdapter<SubCategory1, HolderHomeSkusPieBinding>? =
        null

    private fun setupPieList() {
        pieAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_home_skus_pie, BR.bean
        ) { v, m, pos -> }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPieChartValue.layoutManager = layoutManager
        binding.rvPieChartValue.adapter = pieAdapter
    }

    private lateinit var topAdapter: SimpleRecyclerViewAdapter<SellingSku2, HolderHomeSkusBinding>
    private fun initTopAdapter() {
        topAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_home_skus, BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.clItemClick -> {
                    binding.tvPersonName.text = m.product__brand__name
                    binding.tv2.text = m.product__name

                    getItemDetailsData(
                        m.product_id.toString(),
                        brandsId,
                        sDate,
                        eDate,
                        sharedPrefManager.getOrderType()!!,
                        salesUser,
                        cpIdValue,
                        "",
                        ""
                    )
                }
            }
        }
        binding.rvTopSellingSKUs.adapter = topAdapter
    }

    private var tfLight: Typeface? = null
    private fun setUpLineChart(
        barChart: LineChart, targetData: List<HomeSKUsItemDetailsResponseModel>
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

        barChart.setMaxVisibleValueCount(12)
        barChart.setDrawGridBackground(false)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getDayName(targetData))

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = tfLight
        xAxis.setDrawGridLines(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.dark_grey_txt_color_80)
        xAxis.granularity = 1f // only intervals of 1 day

        targetData.let {
            if (it.size > 12) {
                xAxis.labelCount = it.size / 2
            }else{
                xAxis.labelCount = it.size
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

        barChart.extraBottomOffset = 10f
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
        setBarData(barChart, targetData)
    }

    private fun getDayName(primaryData: List<HomeSKUsItemDetailsResponseModel>?): ArrayList<String> {
        val days = ArrayList<String>()
        primaryData?.forEach {
            days.add(ImageBindingAdapter.getMonthName(it.month))
        }
        return days
    }

    private var colorList = ArrayList<Int>()
    private fun setBarData(
        barChart: LineChart, targetData: List<HomeSKUsItemDetailsResponseModel>
    ) {
        colorList = ArrayList()
        val values = ArrayList<Entry>()
        var i = 0
        while (i < targetData.size) {
            var value = 0f
            targetData[i].revenue.let {
                value = it.toFloat()
            }
            values.add(BarEntry(i.toFloat(), value))
            i++
        }

        val set1 = LineDataSet(values, "")
        set1.valueFormatter = MyValueFormatter()
        set1.setDrawIcons(false)
        set1.setDrawValues(false)
        set1.setDrawCircles(true)
        set1.setCircleColor(ContextCompat.getColor(requireContext(), R.color.line_color))
        set1.setDrawCircleHole(true)
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set1.lineWidth = 2f
        set1.circleSize = 4f
        set1.circleRadius = 3f
        set1.formLineWidth = 0f
        set1.setDrawFilled(true)
        set1.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.red_gradient)
        set1.color = ContextCompat.getColor(requireContext(), R.color.line_color)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        val data = LineData(dataSets)
        data.setValueTextSize(8f)
        data.setValueTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.dark_grey_txt_color
            )
        )
        data.setValueTypeface(tfLight)
        barChart.data = data
    }

    private fun getColors(): ArrayList<String> {
        return arrayListOf("#2563EB", "#C084FC", "#FC8C4D", "#22C55E", "#F3AC23", "#c1e022")
    }
}