package com.riggle.plug.ui.finza.avtivation

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.data.model.ProjectDetail
import com.riggle.plug.data.model.ProjectDetail2
import com.riggle.plug.databinding.FragmentActivationBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.FinzaHomeActivity
import com.riggle.plug.ui.finza.inventory.InventoryActivity
import com.riggle.plug.ui.finza.issuance.IssuanceActivity
import com.riggle.plug.ui.finza.issueSuperTag.IssueSuperTagActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivationFragment : BaseFragment<FragmentActivationBinding>() {

    private val viewModel: ActivationFragmentVM by viewModels()

    override fun onCreateView(view: View) {

        viewModel.getHomeInventoryCount(sharedPrefManager.getToken().toString())

        viewModel.obrHomeInventory.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.tv4.text = it.data.data.incomingInventory.toString()
                        binding.tv6.text = it.data.data.inHandInventory.toString()
                        binding.tv8.text = it.data.data.old_inventory.toString()
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrIssueTagCheckWallet.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                   if (it.data != null){
                       startActivity(IssueSuperTagActivity.newIntent(requireActivity()))
                   }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvIssueSuperTag -> {
                    viewModel.issueTagCheckWallet(sharedPrefManager.getToken().toString())
                    //startActivity(IssueSuperTagActivity.newIntent(requireActivity()))
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