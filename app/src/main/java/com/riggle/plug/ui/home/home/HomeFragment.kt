package com.riggle.plug.ui.home.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.databinding.FragmentHomeBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.home.home.homeInsights.HomeInsightsFragment
import com.riggle.plug.ui.home.home.homeLeaderBoard.HomeLeaderBoardFragment
import com.riggle.plug.ui.home.home.homeSKUs.HomeSKUsFragment
import com.riggle.plug.ui.home.home.orderSummary.HomeOrderSummaryFragment
import com.riggle.plug.ui.networkOrders.ViewPagerAdapter
import com.riggle.plug.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        initViewPager()/* initHomeAdapter() */

        binding.tvUserName.text = sharedPrefManager.getCurrentUser()?.user?.full_name
        binding.tvManufacturerName.text = sharedPrefManager.getCurrentUser()?.user?.company?.name
        if (sharedPrefManager.getCurrentUser()?.user?.image != null) {
            Glide.with(requireContext()).load(sharedPrefManager.getCurrentUser()?.user?.image)
                .placeholder(R.drawable.place_holder_riggle).into(binding.ivHomeUser)
        }

        binding.viewpager.addOnPageChangeListener(object : OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val selectedItem = (position+1).toString()
                Constants.selectedTab = selectedItem
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvFilter -> {

                }

                R.id.tvFilter1 -> {
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToFilterFragment
                    )
                }
            }
        }
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.add(fragList1(), titleList1())
        binding.viewpager.setAdapter(viewPagerAdapter)
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    private fun fragList1(): ArrayList<Fragment> {
        val fragList = ArrayList<Fragment>()
        fragList.add(HomeInsightsFragment())
        fragList.add(HomeOrderSummaryFragment())
        fragList.add(HomeSKUsFragment())
        fragList.add(HomeLeaderBoardFragment())
        fragList.add(SalesPerformanceFragment())
        return fragList
    }

    private val titleList = ArrayList<String>()
    private fun titleList1(): ArrayList<String> {
        titleList.add("OVERVIEW")
        titleList.add("ORDER SUMMARY")
        titleList.add("SKUs")
        titleList.add("LEADERBOARD")
        titleList.add("SALES PERFORMANCE")
        return titleList
    }
}