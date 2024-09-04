package com.riggle.plug.ui.finza.avtivation

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentActivationBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.inventory.InventoryActivity
import com.riggle.plug.ui.finza.issuance.IssuanceActivity
import com.riggle.plug.ui.finza.issueSuperTag.IssueSuperTagActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivationFragment : BaseFragment<FragmentActivationBinding>() {

    private val viewModel: ActivationFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvIssueSuperTag -> {
                    startActivity(IssueSuperTagActivity.newIntent(requireActivity()))
                }

                R.id.tv15 -> {
                    startActivity(IssuanceActivity.newIntent(requireActivity()))
                }

                R.id.tv16 -> {
                    startActivity(InventoryActivity.newIntent(requireActivity()))
                }
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_activation
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

}