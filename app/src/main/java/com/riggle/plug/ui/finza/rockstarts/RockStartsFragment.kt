package com.riggle.plug.ui.finza.rockstarts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentRockStartsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.avtivation.ActivationFragmentVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RockStartsFragment : BaseFragment<FragmentRockStartsBinding>() {

    private val viewModel: RockStartsFragmentVM by viewModels()

    override fun onCreateView(view: View) {

    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_rock_starts
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }
}