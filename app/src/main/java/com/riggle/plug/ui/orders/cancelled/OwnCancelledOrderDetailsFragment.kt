package com.riggle.plug.ui.orders.cancelled

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.ProductOwnOrderDetails
import com.riggle.plug.databinding.CustomLayoutCancelledOrderDetailsBinding
import com.riggle.plug.databinding.CustomLayoutDetailsType2Binding
import com.riggle.plug.databinding.FragmentOwnCancelledOrderDetailsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.orders.details.OwnOrderDetailsType2Fragment
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnCancelledOrderDetailsFragment : BaseFragment<FragmentOwnCancelledOrderDetailsBinding>() {

    private val viewModel: OwnCancelledOrderDetailsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object{
        var orderId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_own_cancelled_order_details
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerOrderDetails.tvHeaderTitle.text = "Cancelled Order"
        binding.headerOrderDetails.ivHeaderSearch.visibility = View.GONE

        initItemsAdapter()
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


    private fun getList() {
        val query = HashMap<String, String>()
        query["expand"] = Constants.NETWORK_ORDER_DETAILS_EXPAND
        viewModel.getList(sharedPrefManager.getSessionId()!!, query, orderId.toInt())
    }


    private lateinit var adapter: SimpleRecyclerViewAdapter<ProductOwnOrderDetails, CustomLayoutCancelledOrderDetailsBinding>
    private fun initItemsAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_cancelled__order_details, BR.bean
        ) { v, m, pos ->

        }

        binding.rvOwnOrderDetailsType2.adapter = adapter
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
}