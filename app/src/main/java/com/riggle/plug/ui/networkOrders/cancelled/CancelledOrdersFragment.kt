package com.riggle.plug.ui.networkOrders.cancelled

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.NetworkCancelledOrdersResponseModel
import com.riggle.plug.databinding.CustomLayoutCancelledOrdersBinding
import com.riggle.plug.databinding.FragmentCancelledOrdersBinding
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
class CancelledOrdersFragment : BaseFragment<FragmentCancelledOrdersBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: CancelledOrdersFragmentVM by viewModels()
    private lateinit var mVerticalPagination: VerticalPagination

    override fun onCreateView(view: View) {
        initView()
    }

    private fun initView() {
        initAdapter()
        getList()

        viewModel.obrOrders.observe(viewLifecycleOwner) {
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
                            //mVerticalPagination.isLoading = true
                        }
                    }
                    if (it.data.isNotEmpty()) {
                        adapter.addToList(it.data)
                        binding.llNoDataFoundCancelledOrders.visibility = View.GONE
                        binding.rvCancelledOrders.visibility = View.VISIBLE
                    } else {
                        binding.llNoDataFoundCancelledOrders.visibility = View.VISIBLE
                        binding.rvCancelledOrders.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundCancelledOrders.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_cancelled_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<NetworkCancelledOrdersResponseModel, CustomLayoutCancelledOrdersBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_cancelled_orders, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvCancelledDetails -> {
                    NetworkOrderDetailsFragment.title = getString(R.string.cancelled_order)
                    NetworkOrderDetailsFragment.orderId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToNetworkOrderDetailsFragment
                    )
                }
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvCancelledOrders.layoutManager = layoutManager

        binding.rvCancelledOrders.adapter = adapter
        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvCancelledOrders.addOnScrollListener(mVerticalPagination)
        mVerticalPagination.isLoading = true

    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["tab_name"] = "network_cancelled"
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