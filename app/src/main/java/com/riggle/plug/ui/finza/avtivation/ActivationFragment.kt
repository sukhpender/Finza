package com.riggle.plug.ui.finza.avtivation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentActivationBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.FinzaHomeActivityVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivationFragment : BaseFragment<FragmentActivationBinding>() {

    private val viewModel: ActivationFragmentVM by viewModels()

    override fun onCreateView(view: View) {

    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_activation
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

}