package com.riggle.plug.ui.orders

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentOrdersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.networkOrders.ViewPagerAdapter
import com.riggle.plug.ui.orders.cancelled.OwnCancelledOrdersFragment
import com.riggle.plug.ui.orders.completed.OwnCompletedOrdersFragment
import com.riggle.plug.ui.orders.confirmed.OwnConfirmedOrdersFragment
import com.riggle.plug.ui.orders.outstanding.OwnOutstandingOrdersFragment
import com.riggle.plug.ui.orders.pending.OwnPendingOrdersFragment
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>() {

    private val viewModel: OrdersFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_orders
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerOrders.ivHeaderSearch.visibility = View.GONE
        binding.headerOrders.tvHeaderTitle.text = resources.getString(R.string.orders)

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
                            it.data.pending_orders,
                            it.data.outstanding_orders,
                            it.data.confirmed_orders,
                            it.data.completed_orders,
                            it.data.cancelled_orders
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
        fragList.add(OwnPendingOrdersFragment())
        fragList.add(OwnOutstandingOrdersFragment())
        fragList.add(OwnConfirmedOrdersFragment())
        fragList.add(OwnCompletedOrdersFragment())
        fragList.add(OwnCancelledOrdersFragment())
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