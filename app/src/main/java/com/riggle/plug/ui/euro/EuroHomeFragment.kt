package com.riggle.plug.ui.euro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentEuroHomeBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EuroHomeFragment : BaseFragment<FragmentEuroHomeBinding>() {

    private val viewModel: EuroHomeFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_euro_home
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.tvFilter2 -> { // today filter for login snapshot

                }
                R.id.tvFilter1 -> { // top right corner filter OVERALL

                }

            }
        }
    }

    private fun initObservers() {

    }
}