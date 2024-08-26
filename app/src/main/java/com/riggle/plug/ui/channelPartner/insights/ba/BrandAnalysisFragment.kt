package com.riggle.plug.ui.channelPartner.insights.ba

import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.riggle.plug.R
import com.riggle.plug.data.model.DeliveryRatioResponseModel
import com.riggle.plug.databinding.FragmentBrandAnalysisBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.channelPartner.insights.pa.PerformanceAnalysisFragment
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrandAnalysisFragment : BaseFragment<FragmentBrandAnalysisBinding>() {

    private val viewModel: BrandAnalysisFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var tfLight: Typeface? = null
    private var colorsList = java.util.ArrayList<Int>()
    private var xAxisBarLabelsList = ArrayList<String>()

        companion object {
            var title = ""
            var companyId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_brand_analysis
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        tfLight = ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)

        getCpInsights()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private fun getCpInsights() {
        val query = HashMap<String, String>()
        query["state"] = ""
        query["brand"] = ""
        query["district"] = ""
        query["company"] = companyId
        query["name"] = title
        query["insights"] = "true"
        query["distributor"] = "true"
        query["buyer_type"] = "retailer"

        viewModel.getInsights(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private fun initObservers() {
        viewModel.obrInsights.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            showHideLoader(false)
                        },500)
                        if (it.data != null) {
                            binding.llNoDataBeatGraphOrders.visibility = View.GONE
                            binding.nsvBeatGraph.visibility = View.VISIBLE

                            if (!it.data.average_order_value.isNullOrEmpty()) {
                                xAxisBarLabelsList.clear()
                                barListAOV.clear()
                                for (i in it.data.average_order_value.indices) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthName(it.data.average_order_value[i].month_name))
                                    barListAOV.add(
                                        BarEntry(
                                            i.toFloat(),
                                            it.data.average_order_value[i].revenue.toFloat()
                                        )
                                    )
                                }
                                initAverageOrderChart()
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
                        }

                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataBeatGraphOrders.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }


    private var barListAOV = ArrayList<BarEntry>()
    private var barListTotalOrders = ArrayList<BarEntry>()
    private var xAxisTotalOrdersList = ArrayList<String>()
    private var barListTotalRevenue = ArrayList<BarEntry>()
    private var xAxisTotalRevenuesList = ArrayList<String>()
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
}