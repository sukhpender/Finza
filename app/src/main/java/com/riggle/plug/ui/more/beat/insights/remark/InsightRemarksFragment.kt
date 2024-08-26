package com.riggle.plug.ui.more.beat.insights.remark

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.Remarks
import com.riggle.plug.databinding.CustomLayoutInsightRemarksBinding
import com.riggle.plug.databinding.CustomLayoutRemarksBinding
import com.riggle.plug.databinding.FragmentInsightRemarksBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsightRemarksFragment : BaseFragment<FragmentInsightRemarksBinding>() {

    private val viewModel: InsightRemarksFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_insight_remarks
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<Remarks, CustomLayoutInsightRemarksBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_insight_remarks, BR.bean
        ) { v, m, pos ->
            when (v?.id) {

            }
        }
        binding.rvInsightRemarks.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<Remarks> {
        val dataList = ArrayList<Remarks>()
        dataList.add(Remarks("AK Enterprises"))
        dataList.add(Remarks("LMN Grocers"))
        dataList.add(Remarks("KK Store"))
        dataList.add(Remarks("Mumbai Store"))
        dataList.add(Remarks("Bombay Store"))
        return dataList
    }
}