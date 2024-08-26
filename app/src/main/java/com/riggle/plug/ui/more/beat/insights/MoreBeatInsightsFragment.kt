package com.riggle.plug.ui.more.beat.insights

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentMoreBeatInsightsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.channelPartner.insights.skus.InsightSKUsFragment
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.more.beat.insights.freq.PurchaseFreqFragment
import com.riggle.plug.ui.more.beat.insights.graph.GraphFragment
import com.riggle.plug.ui.more.beat.insights.remark.InsightRemarksFragment
import com.riggle.plug.ui.networkOrders.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreBeatInsightsFragment : BaseFragment<FragmentMoreBeatInsightsBinding>() {

    private val viewModel: MoreBeatInsightsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_more_beat_insights
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerMoreInsights.tvHeaderTitle.text = title
        binding.headerMoreInsights.ivHeaderSearch.visibility = View.GONE

        initViewPager()
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
        viewPagerAdapter.add(fragList1(), titleList1())
        binding.vpInsight.setAdapter(viewPagerAdapter)
        binding.tlInsight.setupWithViewPager(binding.vpInsight)
    }

    private fun fragList1(): ArrayList<Fragment> {
        val fragList = ArrayList<Fragment>()
        fragList.add(GraphFragment())
        fragList.add(PurchaseFreqFragment())
        fragList.add(InsightSKUsFragment())
        fragList.add(InsightRemarksFragment())
        return fragList
    }

    private fun titleList1(): ArrayList<String> {
        val titleList = ArrayList<String>()
        titleList.add("Graphs")
        titleList.add("Purchase Freq")
        titleList.add("SKUs")
        titleList.add("Remark")
        return titleList
    }
}