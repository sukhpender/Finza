package com.riggle.plug.ui.channelPartner.profile

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.data.model.ResultLeadCP
import com.riggle.plug.databinding.FragmentProfileBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object{
        var model: ResultLeadCP? = null
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_profile
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerProfile.tvHeaderTitle.text = getString(R.string.profile)
        binding.headerProfile.ivHeaderSearch.visibility = View.GONE

        if (model != null){
            binding.bean = model
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }
                R.id.ivCaptureVisitingCard -> {

                }
            }
        }
    }
}