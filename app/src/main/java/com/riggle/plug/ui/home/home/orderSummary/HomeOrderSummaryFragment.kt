package com.riggle.plug.ui.home.home.orderSummary

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.BarChart
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.riggle.plug.R
import com.riggle.plug.data.model.Arpr
import com.riggle.plug.data.model.Count
import com.riggle.plug.data.model.HomeReachAnalysisResponseModel
import com.riggle.plug.data.model.New1
import com.riggle.plug.data.model.Placed
import com.riggle.plug.databinding.FragmentHomeOrderSummaryBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.chart.CustomMarkerView
import com.riggle.plug.utils.chart.CustomXAxisRendererMonthly
import com.riggle.plug.utils.chart.CustomXAxisRendererWeekly
import com.riggle.plug.utils.chart.IntValueFormatter
import com.riggle.plug.utils.chart.IntValueFormatter1
import com.riggle.plug.utils.chart.MyValueFormatter
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@AndroidEntryPoint
class HomeOrderSummaryFragment : BaseFragment<FragmentHomeOrderSummaryBinding>() {

    private val viewModel: HomeOrderSummaryFragmentVM by viewModels()
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

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home_order_summary
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        tfLight = ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold)

        brandsId = ""
        brandsId = sharedPrefManager.getBrandsId().toString()

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

        // SalesUser Filter
        val salesUserId = sharedPrefManager.getSalesPerson()
        var salesUser = ""
        if (salesUserId != null) {
            salesUser = salesUserId
        } else {
            salesUser = ""
        }

        // start and end date filter
        sDate = sharedPrefManager.getStartDate()!!
        eDate = sharedPrefManager.getEndDate()!!

        // channel partner filter
        val cpId = sharedPrefManager.getCP()
        var cpIdValue = ""
        if (cpId != null) {
            cpIdValue = cpId
        } else {
            cpIdValue = ""
        }

        getSnapShotData(brandsId)
        if (sharedPrefManager.getOrderType() == "secondary") {
            binding.tv17.text = "Secondary Orders"
        }else{
            binding.tv17.text = "Primary Orders"
        }
        getInsightsOrder(
            brandsId, sharedPrefManager.getOrderType()!!, sDate, eDate, salesUser, cpIdValue
        )
        getReachAnalysis(
            brandsId, sharedPrefManager.getOrderType()!!, sDate, eDate, salesUser, cpIdValue
        )
        getRemarksData(
            brandsId,
            sDate,
            eDate,
            interval,
            sharedPrefManager.getOrderType()!!,
            salesUser,
            cpIdValue
        )
        getOrdersData(
            brandsId,
            sDate,
            eDate,
            interval,
            sharedPrefManager.getOrderType()!!,
            salesUser,
            cpIdValue
        )
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.iv2 -> { // order placed info click

                }

                R.id.iv3 -> { // order delivered info click

                }

                R.id.tvDaily -> {/*setText(binding.tvMonthly, false)
                    setText(binding.tvDaily, true)
                    setText(binding.tvWeekly, false)
                    interval = "daily"
                    getRemarksData(brandsId, sDate, eDate, interval)
                    getOrdersData(brandsId, sDate, eDate, interval)*/
                }

                R.id.tvWeekly -> {/*setText(binding.tvMonthly, false)
                    setText(binding.tvDaily, false)
                    setText(binding.tvWeekly, true)
                    interval = "weekly"
                    getRemarksData(brandsId, sDate, eDate, interval)
                    getOrdersData(brandsId, sDate, eDate, interval)*/
                }

                R.id.tvMonthly -> {/*setText(binding.tvMonthly, true)
                    setText(binding.tvDaily, false)
                    setText(binding.tvWeekly, false)
                    interval = "monthly"
                    getRemarksData(brandsId, sDate, eDate, interval,sharedPrefManager.getOrderType()!!)
                    getOrdersData(brandsId, sDate, eDate, interval,sharedPrefManager.getOrderType()!!)*/
                }
            }
        }
    }

    private fun getInsightsOrder(
        brandsId: String,
        orderType: String,
        sDate: String,
        eDate: String,
        salesUser: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["self_network_orders"] = "true"
        query["brands"] = brandsId
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["order_type"] = orderType
        query["city"] = ""
        query["state"] = ""
        query["tab_name"] = "orders_summary"
        query["sales_user"] = salesUser
        query["company"] = companyId
        viewModel.getInsightsOrderData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getRemarksData(
        brands: String,
        sDate: String,
        eDate: String,
        interval: String,
        orderType: String,
        salesUser: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["mixed"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["tab_name"] = "orders_summary"
        query["order_type"] = orderType
        query["interval"] = interval
        query["sales_user"] = salesUser
        query["company"] = companyId
        viewModel.getHomeOrderSummaryRemarks(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getOrdersData(
        brands: String,
        sDate: String,
        eDate: String,
        interval: String,
        orderType: String,
        salesUser: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["orders"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["tab_name"] = "orders_summary"
        query["order_type"] = orderType
        query["interval"] = interval
        query["sales_user"] = salesUser
        query["company"] = companyId
        viewModel.getHomeOrderSummaryOrders(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getReachAnalysis(
        brands: String,
        orderType: String,
        sDate: String,
        eDate: String,
        salesUser: String,
        companyId: String
    ) {
        val query = HashMap<String, String>()
        query["cp_reach_analysis"] = "true"
        query["brands"] = brands
        query["start_date"] = sDate
        query["end_date"] = eDate
        query["order_type"] = orderType
        query["city"] = ""
        query["state"] = ""
        query["tab_name"] = "orders_summary"
        query["sales_user"] = salesUser
        query["company"] = companyId
        viewModel.getHomeReachAnalysisData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun getSnapShotData(brands: String) {
        val query = HashMap<String, String>()
        query["snapshot"] = "true"
        query["brands"] = brands
        query["tab_name"] = "orders_summary"
        viewModel.getSnapShotData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun initObservers() {

        viewModel.obrInsightOrders.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.bean = it.data

                        binding.tvPendingPercent.text =
                            it.data.network?.pending?.pending_delivery_ratio?.let { it1 ->
                                Math.round(it1).toString()
                            } + "%"
                        binding.tvOutstandingPercent.text =
                            it.data.network?.delivered?.delivered_delivery_ratio?.let { it1 ->
                                Math.round(it1).toString()
                            } + "%"
                        binding.tvConfirmedPercent.text =
                            it.data.network?.confirmed?.confirmed_delivery_ratio?.let { it1 ->
                                Math.round(it1).toString()
                            } + "%"
                        binding.tvCompletedPercent.text =
                            it.data.network?.completed?.completed_delivery_ratio?.let { it1 ->
                                Math.round(it1).toString()
                            } + "%"
                        binding.tvCancelledPercent.text =
                            it.data.network?.cancelled?.cancelled_delivery_ratio?.let { it1 ->
                                Math.round(it1).toString()
                            } + "%"
                        setProgress(
                            binding.circularProgressBar,
                            it.data.network?.pending?.pending_delivery_ratio
                        )
                        setProgress(
                            binding.cpbOutstanding,
                            it.data.network?.delivered?.delivered_delivery_ratio
                        )
                        setProgress(
                            binding.cpbConfirmed,
                            it.data.network?.confirmed?.confirmed_delivery_ratio
                        )
                        setProgress(
                            binding.cpbCompleted,
                            it.data.network?.completed?.completed_delivery_ratio
                        )
                        setProgress(
                            binding.cpbCancelled,
                            it.data.network?.cancelled?.cancelled_delivery_ratio
                        )
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrReachAnalysis.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.tvReachAnalysis1.text = it.data.ss?.name
                        setUpGroupBarChart(binding.bcSuperStockist, it.data)

                        binding.tvReachAnalysis2.text = it.data.wholesaler?.name
                        initWholeSallerChart(binding.bcWholeSaler, it.data)

                        binding.tvReachAnalysis3.text = it.data.distributor?.name
                        initDistributorChart(binding.bcDistributor, it.data)
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrRemarks.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.remarks != null && it.data.remarks.new != null) {
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.remarks.new.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.remarks.new[i].month))
                                }

                                initNewRemarkChart(
                                    binding.lcNewRemarks, it.data.remarks.new, "NewRemarks"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.remarks.new.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeekly(it.data.remarks.new[i])
                                    )
                                }

                                initNewRemarkChart(
                                    binding.lcNewRemarks, it.data.remarks.new, "NewRemarks"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initNewRemarkChart(
                                    binding.lcNewRemarks, it.data.remarks.new, "NewRemarks"
                                )
                            }
                        }

                        if (it.data.remarks != null && it.data.remarks.existing != null) {
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.remarks.existing.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.remarks.existing[i].month))
                                }

                                initNewRemarkChart(
                                    binding.lcExistingRemarks,
                                    it.data.remarks.existing,
                                    "ExistingRemarks"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.remarks.existing.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeekly(it.data.remarks.existing[i])
                                    )
                                }

                                initNewRemarkChart(
                                    binding.lcExistingRemarks,
                                    it.data.remarks.existing,
                                    "ExistingRemarks"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initNewRemarkChart(
                                    binding.lcExistingRemarks,
                                    it.data.remarks.existing,
                                    "ExistingRemarks"
                                )
                            }
                        }

                        if (it.data.remarks != null && it.data.remarks.total != null) {

                            // initNewRemarkChart(binding.lcTotalRemarks, it.data.remarks.total)

                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.remarks.total.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.remarks.total[i].month))
                                }

                                initNewRemarkChart(
                                    binding.lcTotalRemarks, it.data.remarks.total, "TotalRemarks"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.remarks.total.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeekly(it.data.remarks.total[i])
                                    )
                                }

                                initNewRemarkChart(
                                    binding.lcTotalRemarks, it.data.remarks.total, "TotalRemarks"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initNewRemarkChart(
                                    binding.lcTotalRemarks, it.data.remarks.total, "TotalRemarks"
                                )
                            }
                        }

                        if (it.data.orders != null && it.data.orders.new != null) {
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.new.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.orders.new[i].month))
                                }

                                initNewRemarkChart(
                                    binding.lcNewOrders, it.data.orders.new, "NewOrders"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.new.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeekly(it.data.orders.new[i])
                                    )
                                }

                                initNewRemarkChart(
                                    binding.lcNewOrders, it.data.orders.new, "NewOrders"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initNewRemarkChart(
                                    binding.lcNewOrders, it.data.orders.new, "NewOrders"
                                )
                            }
                        }

                        if (it.data.orders != null && it.data.orders.existing != null) {
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.existing.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.orders.existing[i].month))
                                }

                                initNewRemarkChart(
                                    binding.lcExistingOrders,
                                    it.data.orders.existing,
                                    "ExistingOrders"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.existing.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeekly(it.data.orders.existing[i])
                                    )
                                }

                                initNewRemarkChart(
                                    binding.lcExistingOrders,
                                    it.data.orders.existing,
                                    "ExistingOrders"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initNewRemarkChart(
                                    binding.lcExistingOrders,
                                    it.data.orders.existing,
                                    "ExistingOrders"
                                )
                            }
                        }

                        if (it.data.orders != null && it.data.orders.total != null) {
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.total.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.orders.total[i].month))
                                }

                                initNewRemarkChart(
                                    binding.lcTotalOrders, it.data.orders.total, "TotalOrders"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.total.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeekly(it.data.orders.total[i])
                                    )
                                }

                                initNewRemarkChart(
                                    binding.lcTotalOrders, it.data.orders.total, "TotalOrders"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initNewRemarkChart(
                                    binding.lcTotalOrders, it.data.orders.total, "TotalOrders"
                                )
                            }
                        }

                        if (it.data.extras != null && it.data.extras.arpr != null) {
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.extras.arpr.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.extras.arpr[i].month))
                                }

                                initARPRChart(
                                    binding.lcARPR, it.data.extras.arpr, "ARPR"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.extras.arpr.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeeklyARPR(it.data.extras.arpr[i])
                                    )
                                }

                                initARPRChart(
                                    binding.lcARPR, it.data.extras.arpr, "ARPR"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initARPRChart(
                                    binding.lcARPR, it.data.extras.arpr, "ARPR"
                                )
                            }
                        }

                        if (it.data.extras != null && it.data.extras.aov != null) {
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.extras.aov.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.extras.aov[i].month))
                                }

                                initARPRChart(
                                    binding.lcAOV, it.data.extras.aov, "AOV"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.extras.aov.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeeklyARPR(it.data.extras.aov[i])
                                    )
                                }

                                initARPRChart(
                                    binding.lcAOV, it.data.extras.aov, "AOV"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initARPRChart(
                                    binding.lcAOV, it.data.extras.aov, "AOV"
                                )
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

        viewModel.obrRemarks1.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.orders != null && it.data.orders.placed != null) {
                            //   initOrdersChart(binding.lcPlacedOrders, it.data.orders.placed)
                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.placed.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.orders.placed[i].month))
                                }

                                initOrdersChart(
                                    binding.lcPlacedOrders, it.data.orders.placed, "Placed"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.placed.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeeklyOrderSummary1(it.data.orders.placed[i])
                                    )
                                }

                                initOrdersChart(
                                    binding.lcPlacedOrders, it.data.orders.placed, "Placed"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initOrdersChart(
                                    binding.lcPlacedOrders, it.data.orders.placed, "Placed"
                                )
                            }
                        }


                        if (it.data.orders != null && it.data.orders.delivered != null) {
                            //initOrdersChart(binding.lcDeliveredOrders, it.data.orders.delivered)

                            if (interval == "monthly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.delivered.size) {
                                    xAxisBarLabelsList.add(ImageBindingAdapter.getMonthlyName(it.data.orders.delivered[i].month))
                                }

                                initOrdersChart(
                                    binding.lcDeliveredOrders, it.data.orders.delivered, "Delivered"
                                )
                            }

                            if (interval == "weekly") {
                                xAxisBarLabelsList.clear()
                                for (i in 0 until it.data.orders.delivered.size) {
                                    xAxisBarLabelsList.add(
                                        ImageBindingAdapter.getMonthNameWeeklyOrderSummary1(it.data.orders.delivered[i])
                                    )
                                }

                                initOrdersChart(
                                    binding.lcDeliveredOrders, it.data.orders.delivered, "Delivered"
                                )
                            }

                            if (interval == "daily") {
                                xAxisBarLabelsList.clear()
                                initOrdersChart(
                                    binding.lcDeliveredOrders, it.data.orders.delivered, "Delivered"
                                )
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

        viewModel.obrSnapShotData.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.tvSatesValue.text = it.data.total_states.toString()
                        binding.tvCitiesValue.text = it.data.total_cities.toString()
                        binding.tvBeatsValue.text = it.data.total_beats.toString()
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

    private var tfLight: Typeface? = null
    private var xAxisBarLabelsList = ArrayList<String>()
    private fun initNewRemarkChart(
        barChart: LineChart, targetData: List<New1>, forWhich: String
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

        barChart.setDrawGridBackground(false)

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
            barChart.extraLeftOffset = 10f
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
            xAxisBarLabelsList.let {
                if (it.size > 12) {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size / 2, false)
                } else {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size, false)
                }
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsList)

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

        /*targetData.let {
            xAxis.setLabelCount(it.size / 2)
        }*/

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
        setBarData(barChart, targetData, forWhich)
    }

    private fun initARPRChart(
        barChart: LineChart, targetData: List<Arpr>,forWhich: String
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

        barChart.setDrawGridBackground(false)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getData1(targetData))

        if (interval == "daily") {
            targetData.let {
                if (it.size > 15) {
                    barChart.xAxis.labelCount = it.size/2
                }else{
                    barChart.xAxis.labelCount = it.size
                }
            }

            barChart.extraBottomOffset = 10f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 10f
            barChart.extraRightOffset = 5f
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getData1(targetData))
        }

        if (interval == "weekly" && xAxisBarLabelsList.size != 0) {
            xAxisBarLabelsList.let {
                if (it.size > 8){
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size / 2, false)
                }else{
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size, false)
                }
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsList)

            barChart.extraBottomOffset = 60f
            barChart.offsetLeftAndRight(0)
            barChart.extraTopOffset = 0f
            barChart.extraLeftOffset = 10f
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
            xAxisBarLabelsList.let {
                if (it.size > 12){
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size / 2, false)
                }else{
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size, false)
                }
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsList)

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

        /*targetData.let {
            xAxis.setLabelCount(it.size / 2)
        }*/

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
        setBarData1(barChart, targetData,forWhich)
    }

    private fun initOrdersChart(
        barChart: LineChart, targetData: List<Placed>, forWhich: String
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

        barChart.setDrawGridBackground(false)
        // barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getData2(targetData))


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
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(getData2(targetData))
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
            xAxisBarLabelsList.let {
                if (it.size > 12) {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size / 2, false)
                } else {
                    barChart.xAxis.setLabelCount(xAxisBarLabelsList.size, false)
                }
            }

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisBarLabelsList)

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

        /* targetData.let {
             xAxis.setLabelCount(it.size / 2)
         }*/

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
        setBarData2(barChart, targetData, forWhich)
    }

    private var colorList = ArrayList<Int>()
    private fun setBarData(
        barChart: LineChart, targetData: List<New1>, forWhich: String
    ) {
        colorList = ArrayList()
        val values = ArrayList<Entry>()
        var i = 0
        totalOrderAmountCount = 0
        while (i < targetData.size) {
            var value = 0f
            targetData[i].count.let {
                totalOrderAmountCount += it
            }
            targetData[i].count.let {
                value = it.toFloat()
            }
            values.add(BarEntry(i.toFloat(), value))
            i++
        }

        when (forWhich) {
            "NewRemarks" -> {
                binding.tv31.text = "($totalOrderAmountCount)"
            }

            "NewOrders" -> {
                binding.tv32.text = "($totalOrderAmountCount)"
            }

            "ExistingOrders" -> {
                binding.tv33.text = "($totalOrderAmountCount)"
            }

            "ExistingRemarks" -> {
                binding.tv34.text = "($totalOrderAmountCount)"
            }

            "TotalOrders" -> {
                binding.tv35.text = "($totalOrderAmountCount)"
            }

            "TotalRemarks" -> {
                binding.tv36.text = "($totalOrderAmountCount)"
            }
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

        set1.color = ContextCompat.getColor(requireContext(), R.color.line_color)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

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

    var totalARPRCount = 0
    private var colorList1 = ArrayList<Int>()
    private fun setBarData1(
        barChart: LineChart, targetData: List<Arpr>,forWhich : String
    ) {
        colorList1 = ArrayList()
        val values = ArrayList<Entry>()
        var i = 0
        totalOrderAmountCount = 0
        totalARPRCount = 0
        while (i < targetData.size) {
            var value = 0f
            targetData[i].revenue.let {
                totalOrderAmountCount += it.toInt()
            }
            targetData[i].count.let {
                value = it.toFloat()
                totalARPRCount += it
            }
            values.add(BarEntry(i.toFloat(), value))
            i++
        }

        when(forWhich){
            "ARPR" -> {
                binding.tv57.text = "($totalARPRCount)"
            }
            "AOV" -> {
                val decim = DecimalFormat("#,###.##")
                val asdf = decim.format(totalOrderAmountCount.toFloat())
                binding.tv58.text = "(₹ $asdf)"
            }
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

        set1.color = ContextCompat.getColor(requireContext(), R.color.line_color)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        val data = LineData(dataSets)
        data.setValueTextSize(7f)
        data.setValueTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.dark_grey_txt_color
            )
        )
        data.setValueTypeface(tfLight)
        barChart.data = data
    }

    private var totalOrderAmountCount = 0
    private var colorList2 = ArrayList<Int>()
    private fun setBarData2(
        barChart: LineChart, targetData: List<Placed>, forWhich1: String
    ) {
        colorList2 = ArrayList()
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

        when (forWhich1) {
            "Delivered" -> {
                binding.tv511.let {
                    val decim = DecimalFormat("#,###.##")
                    val asdf = decim.format(totalOrderAmountCount.toFloat())
                    it.text = "(₹ $asdf)"
                }
            }

            "Placed" -> {
                binding.tv51.let {
                    val decim = DecimalFormat("#,###.##")
                    val asdf = decim.format(totalOrderAmountCount.toFloat())
                    it.text = "(₹ $asdf)"
                }
            }
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

        set1.color = ContextCompat.getColor(requireContext(), R.color.line_color)

        /*        for (i in 0 until set1.values.size){
                    Log.e("asdfg",set1.values[i].y.toString())
                    if (set1.values[i].y.toInt() != 0){
                        set1.valueFormatter = IntValueFormatter()
                    }
                }*/

        set1.valueFormatter = IntValueFormatter()
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        val data = LineData(dataSets)
        data.setValueTextSize(6f)
        data.setValueTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.dark_grey_txt_color
            )
        )

        /*   for(i in 0 .. data.dataSets.size-1){
               Log.e("qwerty",data.dataSets[i].valueFormatter.toString())
           }*/
        data.setValueTypeface(tfLight)
        barChart.data = data
    }

    private fun getDataX(targetData: List<New1>): List<String> {
        val xAxis = ArrayList<String>()
        for (index in targetData.withIndex()) {
            xAxis.add((index.index + 1).toString())
        }
        return xAxis//arrayListOf("1", "2", "3", "4", "5", "6")
    }

    private fun getData1(targetData: List<Arpr>): List<String> {
        val xAxis = ArrayList<String>()
        for (index in targetData.withIndex()) {
            xAxis.add((index.index + 1).toString())
        }
        return xAxis//arrayListOf("1", "2", "3", "4", "5", "6")
    }

    private fun getData2(targetData: List<Placed>): List<String> {
        val xAxis = ArrayList<String>()
        for (index in targetData.withIndex()) {
            xAxis.add((index.index + 1).toString())
        }
        return xAxis//arrayListOf("1", "2", "3", "4", "5", "6")
    }

    private fun setText(tv: TextView, isSelected: Boolean) {
        if (isSelected) {
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            tv.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.round_app_bg
                )
            )
        } else {
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_color))
            tv.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.rounded_7dp_border_primary_clr
                )
            )
        }
    }


    private var barDataSet1: BarDataSet? = null
    private var barDataSet2: BarDataSet? = null
    private fun setUpGroupBarChart(
        chart: BarChart, response: HomeReachAnalysisResponseModel
    ) {

        chart.clear()
        val rAxis = chart.axisRight
        rAxis.isEnabled = false

        val yAxis = chart.axisLeft
        yAxis.setDrawAxisLine(false)
        chart.axisLeft.axisMinimum = 0f
        yAxis.enableGridDashedLine(25f, 15f, 2f)

        val xAxis1 = chart.xAxis
        xAxis1.setDrawAxisLine(true)
        xAxis1.enableGridDashedLine(25f, 15f, 2f)
        chart.xAxis.setDrawGridLines(false)
        chart.axisRight.axisMinimum = 0f

        chart.setTouchEnabled(false)
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.isDoubleTapToZoomEnabled = false
        chart.legend.isEnabled = false
        barDataSet1 = BarDataSet(getBarEntriesOne(response.ss?.count), "Total")
        barDataSet1?.color = requireActivity().resources.getColor(R.color.line_color)

        barDataSet2 = BarDataSet(getBarEntriesTwo(response.ss?.count), "Transacting")
        barDataSet2?.color = requireActivity().resources.getColor(R.color.pending)

        val data = BarData(barDataSet1, barDataSet2)
        chart.data = data
        chart.description?.isEnabled = false
        val xAxis = chart.xAxis
        xAxis?.valueFormatter = IndexAxisValueFormatter(getDayName(response.ss?.count))
        xAxis?.setCenterAxisLabels(true)
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.granularity = 1f
        xAxis?.isGranularityEnabled = true
        chart.isDragEnabled = false
        chart.description.isEnabled = false
        val barSpace = 0.0f
        val groupSpace = 0.5f
        data.barWidth = 0.25f
        data.setValueFormatter(IntValueFormatter1())
        chart.xAxis?.axisMinimum = 0f
        chart.animate()
        chart.groupBars(0f, groupSpace, barSpace)
        chart.invalidate()

    }

    private var barDataSet3: BarDataSet? = null
    private var barDataSet4: BarDataSet? = null
    private fun initWholeSallerChart(
        chart: BarChart, response: HomeReachAnalysisResponseModel
    ) {

        chart.clear()
        val rAxis = chart.axisRight
        rAxis.isEnabled = false

        val yAxis = chart.axisLeft
        yAxis.setDrawAxisLine(false)
        chart.axisLeft.axisMinimum = 0f
        yAxis.enableGridDashedLine(25f, 15f, 2f)

        val xAxis1 = chart.xAxis
        xAxis1.setDrawAxisLine(true)
        xAxis1.enableGridDashedLine(25f, 15f, 2f)
        chart.xAxis.setDrawGridLines(false)
        chart.axisRight.axisMinimum = 0f

        chart.axisLeft.axisMinimum = 0f
        chart.axisRight.axisMinimum = 0f

        chart.setTouchEnabled(false)
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.isDoubleTapToZoomEnabled = false
        chart.legend.isEnabled = false
        barDataSet3 = BarDataSet(getBarEntriesOne(response.wholesaler?.count), "Total")
        barDataSet3?.color = requireActivity().resources.getColor(R.color.line_color)

        barDataSet4 = BarDataSet(getBarEntriesTwo(response.wholesaler?.count), "Transacting")
        barDataSet4?.color = requireActivity().resources.getColor(R.color.pending)

        val data = BarData(barDataSet3, barDataSet4)
        chart.data = data
        chart.description?.isEnabled = false
        val xAxis = chart.xAxis
        xAxis?.valueFormatter = IndexAxisValueFormatter(getDayName(response.wholesaler?.count))
        xAxis?.setCenterAxisLabels(true)
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.granularity = 1f
        xAxis?.isGranularityEnabled = true
        chart.isDragEnabled = false
        chart.description.isEnabled = false
        val barSpace = 0.0f
        val groupSpace = 0.5f
        data.barWidth = 0.25f
        data.setValueFormatter(IntValueFormatter1())

        chart.xAxis?.axisMinimum = 0f
        chart.animate()
        chart.groupBars(0f, groupSpace, barSpace)
        chart.invalidate()

    }

    private var barDataSet5: BarDataSet? = null
    private var barDataSet6: BarDataSet? = null
    private fun initDistributorChart(
        chart: BarChart, response: HomeReachAnalysisResponseModel
    ) {
        chart.clear()
        val rAxis = chart.axisRight
        rAxis.isEnabled = false

        val yAxis = chart.axisLeft
        yAxis.setDrawAxisLine(false)
        chart.axisLeft.axisMinimum = 0f
        yAxis.enableGridDashedLine(25f, 15f, 2f)

        val xAxis1 = chart.xAxis
        xAxis1.setDrawAxisLine(true)
        xAxis1.enableGridDashedLine(25f, 15f, 2f)
        chart.xAxis.setDrawGridLines(false)
        chart.axisRight.axisMinimum = 0f
        chart.axisLeft.axisMinimum = 0f
        chart.axisRight.axisMinimum = 0f

        chart.setTouchEnabled(false)
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.isDoubleTapToZoomEnabled = false
        chart.legend.isEnabled = false
        barDataSet5 = BarDataSet(getBarEntriesOne(response.distributor?.count), "Total")
        barDataSet5?.color = requireActivity().resources.getColor(R.color.line_color)

        barDataSet6 = BarDataSet(getBarEntriesTwo(response.distributor?.count), "Transacting")
        barDataSet6?.color = requireActivity().resources.getColor(R.color.pending)

        val data = BarData(barDataSet5, barDataSet6)
        chart.data = data
        chart.description?.isEnabled = false
        val xAxis = chart.xAxis
        xAxis?.valueFormatter = IndexAxisValueFormatter(getDayName(response.distributor?.count))
        xAxis?.setCenterAxisLabels(true)
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.granularity = 1f
        xAxis?.isGranularityEnabled = true
        chart.isDragEnabled = false
        chart.description.isEnabled = false
        val barSpace = 0.0f
        val groupSpace = 0.5f
        data.barWidth = 0.25f
        data.setValueFormatter(IntValueFormatter1())
        chart.xAxis?.axisMinimum = 0f
        chart.animate()
        chart.groupBars(0f, groupSpace, barSpace)
        chart.invalidate()

    }

    private val df = DecimalFormat("#.#")
    private fun getBarEntriesOne(data: List<Count>?): ArrayList<BarEntry> {
        val barEntries = ArrayList<BarEntry>()
        data?.let { list ->
            for (index in list.withIndex()) {
                list[index.index].count.let { value ->
                    val abc = value.toFloat()
                    barEntries.add(BarEntry((index.index + 1).toFloat(), df.format(abc).toFloat()))
                }
            }
        }
        return barEntries
    }

    private fun getBarEntriesTwo(data: List<Count>?): ArrayList<BarEntry> {
        val barEntries = ArrayList<BarEntry>()
        data?.let { list ->
            for (index in list.withIndex()) {
                list[index.index].transacting_count.let { value ->
                    val abc = value.toFloat()
                    barEntries.add(BarEntry((index.index + 1).toFloat(), abc))
                }
            }
        }
        return barEntries
    }

    private fun getDayName(primaryData: List<Count>?): ArrayList<String> {
        val days = ArrayList<String>()
        primaryData?.forEach {
            days.add(ImageBindingAdapter.getMonthName(it.month))
        }
        return days
    }

    private fun setProgress(
        pv: com.mikhaellopez.circularprogressbar.CircularProgressBar, percentage: Double?
    ) {
        if (percentage != null) {
            pv.progress = percentage.toFloat()
        } else {
            pv.progress = 0f
        }
    }
}