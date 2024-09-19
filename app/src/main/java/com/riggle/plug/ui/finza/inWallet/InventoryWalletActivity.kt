package com.riggle.plug.ui.finza.inWallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.WalletTransactionsData
import com.riggle.plug.databinding.ActivityInventoryWalletBinding
import com.riggle.plug.databinding.HolderWalletTransactionsBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.wallet.WalletActivity
import com.riggle.plug.ui.finza.wallet.WalletActivityVM
import com.riggle.plug.ui.finza.wallet.addMoney.AddMoneyActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryWalletActivity : BaseActivity<ActivityInventoryWalletBinding>() {

    private val viewModel: InventoryWalletActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, InventoryWalletActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_inventory_wallet
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        sharedPrefManager.getToken()?.let {
            viewModel.getWallet(it)
        }
        initView()
        initOnClick()
    }

    private fun initView() {

        viewModel.obrWallet.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                        showHideLoader(true)
                }

                Status.SUCCESS -> {
                      showHideLoader(false)
                   // showSuccessToast(it.message.toString())
                    if (it.data != null) {
                        binding.tv3.text = "₹ ${it.data.inventory_wallet}"
                        binding.tv31.text = "₹ ${it.data.hold_amount}"
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
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }
            }
        }
    }
}