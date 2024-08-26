package com.riggle.plug.ui.networkOrders.confirmed

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.NetworkConfirmedResponseModel
import com.riggle.plug.databinding.CustomLayoutConfirmedOrdersBinding
import com.riggle.plug.databinding.FragmentConfirmedOrdersBinding
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
class ConfirmedOrdersFragment : BaseFragment<FragmentConfirmedOrdersBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: ConfirmedOrdersFragmentVM by viewModels()
    private lateinit var mVerticalPagination: VerticalPagination

    override fun onCreateView(view: View) {
        initView()
    }

    private fun initView() {

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
                        if (it.data?.size!! >= 50) {
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                            mVerticalPagination.isLoading = true
                        }
                    }
                    if (it.data.isNotEmpty()) {
                        adapter.addToList(it.data)
                        binding.llNoDataFoundConfirmedOrders.visibility = View.GONE
                        binding.rvConfirmedOrders.visibility = View.VISIBLE
                    } else {
                        binding.llNoDataFoundConfirmedOrders.visibility = View.VISIBLE
                        binding.rvConfirmedOrders.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundConfirmedOrders.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_confirmed_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<NetworkConfirmedResponseModel, CustomLayoutConfirmedOrdersBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_confirmed_orders, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvConfirmedOrderDetails -> {
                    NetworkOrderDetailsFragment.orderId = m.id.toString()
                    NetworkOrderDetailsFragment.title = "Confirmed Order"
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToNetworkOrderDetailsFragment
                    )
                }
            }
        }
        val layoutManager = LinearLayoutManager(context)
        binding.rvConfirmedOrders.layoutManager = layoutManager
        binding.rvConfirmedOrders.setItemViewCacheSize(100)
        binding.rvConfirmedOrders.adapter = adapter
        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvConfirmedOrders.addOnScrollListener(mVerticalPagination)
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["tab_name"] = "network_confirmed"
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