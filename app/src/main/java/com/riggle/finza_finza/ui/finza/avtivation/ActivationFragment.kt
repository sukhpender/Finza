package com.riggle.finza_finza.ui.finza.avtivation

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.FragmentActivationBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.inventory.InventoryActivity
import com.riggle.finza_finza.ui.finza.issuance.IssuanceActivity
import com.riggle.finza_finza.ui.finza.issueSuperTag.IssueSuperTagActivity
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.event.SingleLiveEvent
import com.riggle.finza_finza.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivationFragment : BaseFragment<FragmentActivationBinding>() {

    private val viewModel: ActivationFragmentVM by viewModels()

    companion object{
        var isUpdatesAvailable = SingleLiveEvent<Boolean>()
    }

    override fun onCreateView(view: View) {

        viewModel.getHomeInventoryCount(sharedPrefManager.getToken().toString())

        isUpdatesAvailable.observe(viewLifecycleOwner){ it ->
            if (it){
                viewModel.getHomeInventoryCount(sharedPrefManager.getToken().toString())
            }
        }
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
                  //  showSuccessToast(it.message.toString())
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