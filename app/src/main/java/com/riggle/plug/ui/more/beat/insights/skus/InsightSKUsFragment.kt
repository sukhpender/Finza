package com.riggle.plug.ui.more.beat.insights.skus

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.LowSellingSku
import com.riggle.plug.data.model.TopSellingSku
import com.riggle.plug.databinding.CustomLayoutLowSkusBinding
import com.riggle.plug.databinding.CustomLayoutTopSkusBinding
import com.riggle.plug.databinding.FragmentInsightSKUsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class InsightSKUsFragment : BaseFragment<FragmentInsightSKUsBinding>() {

    private val viewModel: InsightSKUsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var BEAT_INSIGHTS_FOR = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_insight_s_k_us
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        initTopAdapter()
        initLowAdapter()

        val calendar = Calendar.getInstance()
        getBeatInsights(
            (calendar.get(Calendar.MONTH) + 1).toString(), calendar.get(Calendar.YEAR).toString()
        )
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private fun initObservers() {
        viewModel.obrBeatInsights.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.nsvSKUs.visibility = View.VISIBLE

                        if (it.data.top_selling_sku.isNotEmpty()) {
                            topAdapter.list = it.data.top_selling_sku
                            binding.llNoDataBeatTopSKUs.visibility = View.GONE
                            binding.llTopSKUs.visibility = View.VISIBLE
                        } else {
                            binding.llNoDataBeatTopSKUs.visibility = View.VISIBLE
                            binding.llTopSKUs.visibility = View.GONE
                        }
                        if (it.data.low_selling_sku.isNotEmpty()) {
                            lowAdapter.list = it.data.low_selling_sku
                            binding.llNoDataBeatLowSKUs.visibility = View.GONE
                            binding.llLowSKUs.visibility = View.VISIBLE
                        } else {
                            binding.llNoDataBeatLowSKUs.visibility = View.VISIBLE
                            binding.llLowSKUs.visibility = View.GONE
                        }
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

    private fun getBeatInsights(month: String, year: String) {
        val query = HashMap<String, String>()
        query["company"] = BEAT_INSIGHTS_FOR
        query["brand"] = ""
        query["insights"] = "true"
        query["retailers"] = "true"
        query["month"] = month
        query["year"] = year

        viewModel.getCitiesList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private lateinit var topAdapter: SimpleRecyclerViewAdapter<TopSellingSku, CustomLayoutTopSkusBinding>
    private fun initTopAdapter() {
        topAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_top_skus, BR.bean
        ) { v, m, pos ->

        }
        binding.rvTopSellingSKUs.adapter = topAdapter
    }

    private lateinit var lowAdapter: SimpleRecyclerViewAdapter<LowSellingSku, CustomLayoutLowSkusBinding>
    private fun initLowAdapter() {
        lowAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_low_skus, BR.bean
        ) { v, m, pos ->

        }
        binding.rvLowSellingSKUs.adapter = lowAdapter
    }
}