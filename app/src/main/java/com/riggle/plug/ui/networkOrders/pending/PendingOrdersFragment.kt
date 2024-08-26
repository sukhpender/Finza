package com.riggle.plug.ui.networkOrders.pending

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.NetworkPendingOrdersResponseModel
import com.riggle.plug.databinding.CustomLayoutPendingOrdersBinding
import com.riggle.plug.databinding.FragmentPendingOrdersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.networkOrders.orderDetails.NetworkOrderDetailsFragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingOrdersFragment : BaseFragment<FragmentPendingOrdersBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: PendingOrdersFragmentVM by viewModels()
    private lateinit var mVerticalPagination: VerticalPagination


    override fun onCreateView(view: View) {
        initView()
        initPendingOrdersAdapter()
    }

    private fun initView() {

        getList()
        viewModel.obrPendingOrders.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.size != 0) {
                        if (page == 1) {
                            pendingOrdersAdapter.clearList()
                        }
                        if (it.data?.size!! >= 15) {
                            page++
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                        }
                    }
                    if (it.data.isNotEmpty()) {
                        pendingOrdersAdapter.addToList(it.data)
                        binding.llNoDataFoundPendingOrders.visibility = View.GONE
                        binding.rvPendingOrders.visibility = View.VISIBLE
                    } else {
                        binding.llNoDataFoundPendingOrders.visibility = View.VISIBLE
                        binding.rvPendingOrders.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundPendingOrders.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_pending_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var pendingOrdersAdapter: SimpleRecyclerViewAdapter<NetworkPendingOrdersResponseModel, CustomLayoutPendingOrdersBinding>
    private fun initPendingOrdersAdapter() {
        pendingOrdersAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_pending_orders, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvPendingOrderDetails -> {
                    NetworkOrderDetailsFragment.title = getString(R.string.pending_order)
                    NetworkOrderDetailsFragment.orderId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToNetworkOrderDetailsFragment
                    )
                }
            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvPendingOrders.layoutManager = layoutManager
        binding.rvPendingOrders.setItemViewCacheSize(100)
        binding.rvPendingOrders.adapter = pendingOrdersAdapter

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvPendingOrders.addOnScrollListener(mVerticalPagination)
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["tab_name"] = "network_pending"
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