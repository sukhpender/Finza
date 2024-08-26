package com.riggle.plug.ui.orders.outstanding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.NetworkOutstandingOrdersResponseModel
import com.riggle.plug.data.model.NetworkPendingOrders
import com.riggle.plug.databinding.CustomLayoutOutstandingOrdersBinding
import com.riggle.plug.databinding.CustomLayoutOwnOutstandingOrdersBinding
import com.riggle.plug.databinding.FragmentOwnOutstandingOrdersBinding
import com.riggle.plug.databinding.FragmentOwnPendingOrdersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.networkOrders.orderDetails.NetworkOrderDetailsFragment
import com.riggle.plug.ui.orders.details.OwnOrderDetailsFragment
import com.riggle.plug.ui.orders.details.OwnOrderDetailsType2Fragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnOutstandingOrdersFragment : BaseFragment<FragmentOwnOutstandingOrdersBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: OwnOutstandingOrdersFragmentVM by viewModels()
    private lateinit var mVerticalPagination: VerticalPagination

    override fun onCreateView(view: View) {
        initAdapter()

        getList()

        viewModel.obrOutstandingOrdersOrders.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.size != 0) {
                        if (page == 1) {
                            adapter.clearList()
                        }
                        if (it.data?.size!! >= 15) {
                            page++
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                            mVerticalPagination.isLoading = true
                        }
                    }
                    if (it.data.isNotEmpty()) {
                        adapter.addToList(it.data)
                        binding.llNoDataFoundOwnPendingOrders.visibility = View.GONE
                        binding.rvOrders.visibility = View.VISIBLE
                    } else {
                        binding.llNoDataFoundOwnPendingOrders.visibility = View.VISIBLE
                        binding.rvOrders.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundOwnPendingOrders.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_own_outstanding_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<NetworkOutstandingOrdersResponseModel, CustomLayoutOwnOutstandingOrdersBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_own_outstanding_orders, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvOwnOutStandingOrderDetails -> {
                    OwnOrderDetailsType2Fragment.title = "Outstanding Order"
                    OwnOrderDetailsType2Fragment.orderId = m.id.toString()
                    OwnOrderDetailsType2Fragment.orderDetailsFor = "Outstanding"
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToOwnOrderDetailsType2Fragment
                    )
                }
            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvOrders.layoutManager = layoutManager
        binding.rvOrders.setItemViewCacheSize(100)
        binding.rvOrders.adapter = adapter
        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvOrders.addOnScrollListener(mVerticalPagination)
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["tab_name"] = "outstanding"
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = page.toString()
        query["no-pagination"] = "false"
        query["type"] = ""
        query["buyer__type"] = ""
        query["seller"] = ""
        query["cart_user"] = ""
        query["cart_user"] = ""
        query["expand"] = Constants.NETWORK_PENDING_ORDERS_EXPAND
        query["fields"] = Constants.NETWORK_PENDING_ORDERS_FIELDS
        viewModel.getList(sharedPrefManager.getSessionId()!!, query)
    }

    private var page = 1
    override fun onLoadMore() {
        getList()
    }
}