package com.riggle.plug.ui.networkOrders

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentNetworkOrdersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.networkOrders.cancelled.CancelledOrdersFragment
import com.riggle.plug.ui.networkOrders.completed.CompletedOrdersFragment
import com.riggle.plug.ui.networkOrders.confirmed.ConfirmedOrdersFragment
import com.riggle.plug.ui.networkOrders.outstanding.OutstandingOrdersFragment
import com.riggle.plug.ui.networkOrders.pending.PendingOrdersFragment
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.event.SingleLiveEvent
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetworkOrdersFragment : BaseFragment<FragmentNetworkOrdersBinding>() {

    private val viewModel: NetworkOrdersFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        val pendingOrdersCount = SingleLiveEvent<Int>()
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_network_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerNetworkOrders.ivHeaderSearch.visibility = View.GONE
        binding.headerNetworkOrders.tvHeaderTitle.text =
            resources.getString(R.string.network_orders)

        val query = HashMap<String, String>()
        query["orders"] = "true"
        query["buyer"] = ""
        query["seller"] = ""
        query["created_at"] = ""
        query["delivery_date"] = ""
        query["order_date"] = ""
        query["revisit_date"] = ""
        query["cancelled_date"] = ""
        query["cart_user"] = ""
        query["search"] = ""
        viewModel.getCount(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id.toString(),
            query
        )

        viewModel.obrNetworkCount.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        titleList1(
                            it.data.network_pending_orders,
                            it.data.network_outstanding_orders,
                            it.data.network_confirmed_orders,
                            it.data.network_completed_orders,
                            it.data.network_cancelled_orders
                        )
                        initViewPager()
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

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }
            }
        }
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.add(fragList1(), titleList)
        binding.viewpager.setAdapter(viewPagerAdapter)
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    private fun fragList1(): ArrayList<Fragment> {
        val fragList = ArrayList<Fragment>()
        fragList.add(PendingOrdersFragment())
        fragList.add(OutstandingOrdersFragment())
        fragList.add(ConfirmedOrdersFragment())
        fragList.add(CompletedOrdersFragment())
        fragList.add(CancelledOrdersFragment())
        return fragList
    }

    private val titleList = ArrayList<String>()
    private fun titleList1(
        networkPendingOrders: Int,
        networkOutstandingOrders: Int,
        networkConfirmedOrders: Int,
        networkCompletedOrders: Int,
        networkCancelledOrders: Int
    ): ArrayList<String> {
        titleList.add("Pending ($networkPendingOrders)")
        titleList.add("Outstanding ($networkOutstandingOrders)")
        titleList.add("Confirmed ($networkConfirmedOrders)")
        titleList.add("Completed ($networkCompletedOrders)")
        titleList.add("Cancelled ($networkCancelledOrders)")
        return titleList
    }
}