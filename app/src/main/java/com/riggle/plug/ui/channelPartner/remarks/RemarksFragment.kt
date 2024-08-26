package com.riggle.plug.ui.channelPartner.remarks

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.ResultCpRemarks
import com.riggle.plug.databinding.CustomLayoutRemarksBinding
import com.riggle.plug.databinding.FragmentRemarksBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemarksFragment : BaseFragment<FragmentRemarksBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: RemarksFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var mVerticalPagination: VerticalPagination

    companion object {
        var companyId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    private fun initObservers() {
        viewModel.obrRemarksCP.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (page == 1) {
                        remarksAdapter.clearList()
                    }
                    if (it.data?.results?.size!! >= 15) {
                        page++
                        mVerticalPagination.isLoading = false
                    } else {
                        page = 1
                        mVerticalPagination.isLoading = true
                    }
                    if (it.data.results.isNotEmpty()) {
                        binding.llNoDataFoundRemarksCP.visibility = View.GONE
                        binding.rvRemarks.visibility = View.VISIBLE

                        remarksAdapter.list = it.data.results
                    } else {
                        binding.llNoDataFoundRemarksCP.visibility = View.VISIBLE
                        binding.rvRemarks.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundRemarksCP.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_remarks
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerRemarks.tvHeaderTitle.text = getString(R.string.remarks)
        binding.headerRemarks.ivHeaderSearch.visibility = View.GONE

        if (companyId != "") {
            viewModel.getRemarks(sharedPrefManager.getSessionId()!!, companyId.toInt())
        }
        initRemarksAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.tvSelectDateRemark -> {

                }
            }
        }
    }

    private lateinit var remarksAdapter: SimpleRecyclerViewAdapter<ResultCpRemarks, CustomLayoutRemarksBinding>
    private fun initRemarksAdapter() {
        remarksAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_remarks, BR.bean
        ) { v, m, pos ->
            when (v?.id) {

            }
        }
        val layoutManager = LinearLayoutManager(context)
        binding.rvRemarks.layoutManager = layoutManager
        binding.rvRemarks.setItemViewCacheSize(100)
        binding.rvRemarks.adapter = remarksAdapter

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvRemarks.addOnScrollListener(mVerticalPagination)
    }


    private var page = 1
    override fun onLoadMore() {
        viewModel.getRemarks(sharedPrefManager.getSessionId()!!, companyId.toInt())
    }

}