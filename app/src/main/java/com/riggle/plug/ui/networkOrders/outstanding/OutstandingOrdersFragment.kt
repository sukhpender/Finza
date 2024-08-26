package com.riggle.plug.ui.networkOrders.outstanding

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.NetworkOutstandingOrdersResponseModel
import com.riggle.plug.databinding.CustomLayoutOutstandingOrdersBinding
import com.riggle.plug.databinding.FragmentOutstandingOrdersBinding
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
class OutstandingOrdersFragment : BaseFragment<FragmentOutstandingOrdersBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: OutstandingOrdersFragmentVM by viewModels()
    private lateinit var mVerticalPagination: VerticalPagination

    override fun onCreateView(view: View) {
        initView()
    }

    private fun initView() {
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
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                            mVerticalPagination.isLoading = true
                        }
                    }
                    if (it.data.isNotEmpty()) {
                        adapter.addToList(it.data)
                        binding.llNoDataFoundOutstandingOrders.visibility = View.GONE
                        binding.rvOutstandingOrders.visibility = View.VISIBLE
                    } else {
                        binding.llNoDataFoundOutstandingOrders.visibility = View.VISIBLE
                        binding.rvOutstandingOrders.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundOutstandingOrders.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_outstanding_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<NetworkOutstandingOrdersResponseModel, CustomLayoutOutstandingOrdersBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_outstanding_orders, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvOutstandingOrderDetails -> {
                    NetworkOrderDetailsFragment.title = getString(R.string.outstanding_order)
                    NetworkOrderDetailsFragment.orderId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToNetworkOrderDetailsFragment
                    )
                }
            }
        }
        val layoutManager = LinearLayoutManager(context)
        binding.rvOutstandingOrders.layoutManager = layoutManager
        binding.rvOutstandingOrders.setItemViewCacheSize(100)
        binding.rvOutstandingOrders.adapter = adapter
        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvOutstandingOrders.addOnScrollListener(mVerticalPagination)
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["tab_name"] = "network_outstanding"
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
        page++
        getList()
    }
}