package com.riggle.plug.ui.channelPartner.insights

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentCpInsightsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.channelPartner.insights.ba.BrandAnalysisFragment
import com.riggle.plug.ui.channelPartner.insights.pa.PerformanceAnalysisFragment
import com.riggle.plug.ui.channelPartner.insights.sc.SubCategoryFragment
import com.riggle.plug.ui.channelPartner.insights.skus.InsightSKUsFragment
import com.riggle.plug.ui.networkOrders.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CpInsightsFragment : BaseFragment<FragmentCpInsightsBinding>() {

    private val viewModel: CpInsightsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
        var companyId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_cp_insights
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerCpInsights.tvHeaderTitle.text = title

        initViewPager()
        PerformanceAnalysisFragment.title = title
        PerformanceAnalysisFragment.companyId = companyId
        BrandAnalysisFragment.title = title
        BrandAnalysisFragment.companyId = companyId
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
        binding.vpInsights.setAdapter(viewPagerAdapter)
        binding.tlCpInsights.setupWithViewPager(binding.vpInsights)
    }

    private fun fragList1(): ArrayList<Fragment> {
        val fragList = ArrayList<Fragment>()
        fragList.add(PerformanceAnalysisFragment())
        fragList.add(BrandAnalysisFragment())
        fragList.add(SubCategoryFragment())
        fragList.add(InsightSKUsFragment())
        return fragList
    }

    private fun titleList1(): ArrayList<String> {
        val titleList = ArrayList<String>()
        titleList.add("Performance Analysis")
        titleList.add("Brand Analysis")
        titleList.add("Sub Category")
        titleList.add("SKUs")
        return titleList
    }
}