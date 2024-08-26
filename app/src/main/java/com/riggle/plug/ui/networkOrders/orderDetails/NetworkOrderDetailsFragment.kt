package com.riggle.plug.ui.networkOrders.orderDetails

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.ProductNetworkOrderDetails
import com.riggle.plug.data.model.ProductOwnOrderDetails
import com.riggle.plug.databinding.CustomLayoutDetailsNetworkBinding
import com.riggle.plug.databinding.CustomLayoutDetailsType2Binding
import com.riggle.plug.databinding.FragmentNetworkOrderDetailsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetworkOrderDetailsFragment : BaseFragment<FragmentNetworkOrderDetailsBinding>() {

    private val viewModel: NetworkOrderDetailsFragmentVM by viewModels()
    lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
        var orderId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initItemsAdapter()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_network_order_details
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerNetworkOrderDetails.tvHeaderTitle.text = title
        binding.headerNetworkOrderDetails.ivHeaderSearch.visibility = View.GONE

        if (title == "Cancelled Order") {
            binding.tvCancelReason.visibility = View.VISIBLE
        } else {
            binding.tvCancelReason.visibility = View.GONE
        }

        getList()

        viewModel.obrOrderDetails.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                   binding.bean = it.data
                    adapter.list = it.data?.products
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }
            }
        }
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<ProductNetworkOrderDetails, CustomLayoutDetailsNetworkBinding>
    private fun initItemsAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_details_network, BR.bean
        ) { v, m, pos ->

        }

        binding.rvNetworkDetails.adapter = adapter
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["expand"] = Constants.NETWORK_ORDER_DETAILS_EXPAND1
        viewModel.getList(sharedPrefManager.getSessionId()!!, query, orderId.toInt())
    }
}