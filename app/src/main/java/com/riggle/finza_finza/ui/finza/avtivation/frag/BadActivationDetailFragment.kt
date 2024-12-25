package com.riggle.finza_finza.ui.finza.avtivation.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.FragmentBadActivationDetailBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadActivationDetailFragment : BaseFragment<FragmentBadActivationDetailBinding>() {

    private val viewModel: BadActivationDetailFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    private fun initObservers() {

    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_bad_activation_detail
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }
}