package com.riggle.plug.ui.home.sales.salesInsights.insights

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.SalesTitle
import com.riggle.plug.databinding.FragmentSalesInsightsHeadBinding
import com.riggle.plug.databinding.LaySalesInsightsTitlesBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.home.sales.salesInsights.saleLocations.SalesLocationsFragment
import com.riggle.plug.ui.home.sales.salesInsights.salesBeat.SalesBeatFragment
import com.riggle.plug.ui.networkOrders.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesInsightsHeadFragment : BaseFragment<FragmentSalesInsightsHeadBinding>() {

    private val viewModel: SalesInsightsHeadFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var userId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        initAdapter()
        titleList1()
        initViewPager()
        SalesmanInsightsFragment.loadFor.postValue("Daily Analysis")
        SalesmanInsightsFragment.userID = userId
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivSalesInsightsBack -> {
                    homeActivity.onBackPressed()
                }
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_insights_head
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.add(fragList1(), titleList)
        binding.viewpager.setAdapter(viewPagerAdapter)
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    private fun fragList1(): ArrayList<Fragment> {
        val fragList = ArrayList<Fragment>()
        fragList.add(SalesmanInsightsFragment())
       // fragList.add(SalesLocationsFragment())
       // fragList.add(SalesBeatFragment())
        return fragList
    }

    private val titleList = ArrayList<String>()
    private fun titleList1(): ArrayList<String> {
        titleList.add("Insights")
       // titleList.add("Location")
    //    titleList.add("Beats")
        return titleList
    }

    private lateinit var titleAdapter: SimpleRecyclerViewAdapter<SalesTitle, LaySalesInsightsTitlesBinding>
    private fun initAdapter() {
        titleAdapter = SimpleRecyclerViewAdapter(
            R.layout.lay_sales_insights_titles, BR.bean
        ) { v, m, pos ->
            for (i in 0 until titleAdapter.list.size) {
                titleAdapter.list[i].isSelected = pos == i
            }
            titleAdapter.notifyDataSetChanged()
            when (m.title) {
                "Daily Analysis" -> {
                    SalesmanInsightsFragment.loadFor.postValue("Daily Analysis")
                    SalesmanInsightsFragment.userID = userId
                }

                "DRR Trend" -> {
                    SalesmanInsightsFragment.loadFor.postValue("DRR Trend")
                    SalesmanInsightsFragment.userID = userId
                }

                "Reach Analysis" -> {
                    SalesmanInsightsFragment.loadFor.postValue( "Reach Analysis")
                    SalesmanInsightsFragment.userID = userId
                }

                "Brand Analysis" -> {
                    SalesmanInsightsFragment.loadFor.postValue("Brand Analysis")
                    SalesmanInsightsFragment.userID = userId
                }

                "SKUs" -> {
                    SalesmanInsightsFragment.loadFor.postValue("SKUs")
                    SalesmanInsightsFragment.userID = userId
                }
            }

        }
        binding.rvTitleSales.adapter = titleAdapter
        titleAdapter.list = titleList()
    }

    private fun titleList(): ArrayList<SalesTitle> {
        val list = ArrayList<SalesTitle>()
        list.add(SalesTitle("Daily Analysis", true))
        list.add(SalesTitle("DRR Trend", false))
        list.add(SalesTitle("Reach Analysis", false))
        list.add(SalesTitle("Brand Analysis", false))
        list.add(SalesTitle("SKUs", false))
        return list
    }
}