package com.riggle.plug.ui.channelPartner.insights.sc

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentSubCategoryBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment : BaseFragment<FragmentSubCategoryBinding>() {

    private val viewModel: SubCategoryFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
    }

    override fun onCreateView(view: View) {
        showToast("Sub Cat")
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sub_category
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