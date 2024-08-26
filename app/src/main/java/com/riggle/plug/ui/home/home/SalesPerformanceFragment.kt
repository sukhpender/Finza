package com.riggle.plug.ui.home.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.SalesPerformanceResponseModel
import com.riggle.plug.databinding.FragmentSalesPerformanceBinding
import com.riggle.plug.databinding.HolderSalesPerformanceBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesPerformanceFragment : BaseFragment<FragmentSalesPerformanceBinding>() {

    private lateinit var homeActivity: HomeActivity
    private val viewModel: SalesPerformanceFragmentVM by viewModels()
    private var brandsId = ""
    private var sDate = ""
    private var eDate = ""

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_performance
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        initAdapter()

        brandsId = ""
        brandsId = sharedPrefManager.getBrandsId().toString()

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

        val queryMap = java.util.HashMap<String, String>()
        queryMap["order_type"] = sharedPrefManager.getOrderType()!!
        queryMap["current_date"] = sDate
        queryMap["tab_name"] = "daily_sales_report"
        queryMap["brands"] = brandsId
        if (sharedPrefManager.getSalesPerson() != null) {
            queryMap["salesman"] = sharedPrefManager.getSalesPerson()!!
        } else {
            queryMap["salesman"] = ""
        }

        if (sharedPrefManager.getCP() != null) {
            queryMap["channel_partner"] = sharedPrefManager.getCP()!!
        } else {
            queryMap["channel_partner"] = ""
        }
        queryMap["state"] = ""
        queryMap["city"] = ""


        viewModel.getSalesPerformanceList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.id.toString(),
            queryMap
        )

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private fun initObservers() {
        viewModel.obrSalesPerformance.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    it.data.let { it1 ->
                        adapter.list = it1
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                }

                else -> {
                    showHideLoader(false)
                }
            }
        })

    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<SalesPerformanceResponseModel, HolderSalesPerformanceBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_sales_performance, BR.bean
        ) { v, m, pos ->

        }
        binding.rvSalesPerformace.adapter = adapter
    }
}