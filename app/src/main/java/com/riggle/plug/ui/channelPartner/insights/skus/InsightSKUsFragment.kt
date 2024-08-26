package com.riggle.plug.ui.channelPartner.insights.skus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentInsightSKUs2Binding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.channelPartner.insights.sc.SubCategoryFragmentVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsightSKUsFragment : BaseFragment<FragmentInsightSKUs2Binding>() {

    private val viewModel: InsightSKUsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_insight_s_k_us2
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }
}