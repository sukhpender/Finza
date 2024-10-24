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

    companion object {
        var isUpdatesAvailable = SingleLiveEvent<Boolean>()
    }

    override fun onCreateView(view: View) {
        viewModel.getHomeInventoryCount(sharedPrefManager.getToken().toString())

        isUpdatesAvailable.observe(viewLifecycleOwner) { it ->
            if (it) {
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
                        binding.tvTFCount.text = it.data.data.today_performance.toString()
                        binding.tvBadTagCount.text = it.data.data.badfast_tag_count.toString()
                        binding.tvUrtCount.text = it.data.data.wrong_urt_count.toString()
                        binding.tvTFCount.text = it.data.data.today_performance.toString()
                        binding.tvCurrentValue.text = it.data.data.this_month_performance.toString()
                        binding.tvLastValue.text = it.data.data.last_month_performance.toString()

                        binding.tvUrtAmount.text = "₹ " + it.data.data.wrong_urt_sum.toString()
                        binding.tvBadTagAmount.text = "₹ " + it.data.data.badfast_tag_sum.toString()
                        binding.tvTFCountAmount.text = "₹ " + it.data.data.today_income.toString()
                        binding.tvLMAmount.text = "₹ " + it.data.data.last_month_income.toString()
                        binding.tvCMAmount.text = "₹ " + it.data.data.this_month_income
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
                    if (it.data != null) {
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

    fun formatNumberDynamic(value: Double): String {
        val units =
            listOf("K", "Lakh", "Crore", "B", "T")  // Units for thousands, lakh, crore, etc.
        var number = value
        var index = 0

        // While loop to scale down the number and increase the unit index
        while (number >= 1000 && index < units.size - 1) {
            number /= 1000
            index++
        }

        // Format the number with 2 decimal places and append the unit
        return String.format("%.2f %s", number, units[index])
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