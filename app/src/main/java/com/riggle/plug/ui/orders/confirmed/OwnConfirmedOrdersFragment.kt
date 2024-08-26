package com.riggle.plug.ui.orders.confirmed

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.NetworkConfirmedResponseModel
import com.riggle.plug.data.model.NetworkPendingOrders
import com.riggle.plug.databinding.CustomLayoutOwnConfirmedOrdersBinding
import com.riggle.plug.databinding.CustomLayoutOwnOutstandingOrdersBinding
import com.riggle.plug.databinding.FragmentOwnConfirmedOrdersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.orders.details.OwnOrderDetailsFragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnConfirmedOrdersFragment : BaseFragment<FragmentOwnConfirmedOrdersBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: OwnConfirmedOrdersFragmentVM by viewModels()
    private lateinit var mVerticalPagination: VerticalPagination

    override fun onCreateView(view: View) {
        initAdapter()

        getList()

        viewModel.obrConfirmedOrdersOrders.observe(viewLifecycleOwner) {
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
                        binding.llNoDataFoundOwnConfirmedOrders.visibility = View.GONE
                        binding.rvOrders.visibility = View.VISIBLE
                    } else {
                        binding.llNoDataFoundOwnConfirmedOrders.visibility = View.VISIBLE
                        binding.rvOrders.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundOwnConfirmedOrders.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_own_confirmed_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<NetworkConfirmedResponseModel, CustomLayoutOwnConfirmedOrdersBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_own_confirmed_orders, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvOwnConfirmedOrderDetails -> {
                    OwnOrderDetailsFragment.title = "Confirmed Order"
                    OwnOrderDetailsFragment.orderDetailsFor = "Confirmed"
                    OwnOrderDetailsFragment.orderId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToOwnOrderDetailsFragment
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
        query["tab_name"] = "confirmed"
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