package com.riggle.finza_finza.ui.finza.wallet

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.WalletTransactionsData
import com.riggle.finza_finza.databinding.ActivityWalletBinding
import com.riggle.finza_finza.databinding.HolderWalletTransactionsBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.ui.finza.wallet.addMoney.AddMoneyActivity
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.event.SingleLiveEvent
import com.riggle.finza_finza.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletActivity : BaseActivity<ActivityWalletBinding>() {

    private val viewModel: WalletActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, WalletActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

        val isMoneyAdded = SingleLiveEvent<Boolean>()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_wallet
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        sharedPrefManager.getToken()?.let {
            viewModel.getWallet(it)
        }
        sharedPrefManager.getToken()?.let {
            viewModel.getTransactionHistory(it,3)
        }

        initView()
        initOnClick()
    }

    private fun initView() {
        initAdapter()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.transactions)
        )
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String
                when(selectedItem){
//                    Status key
//                    0 for credited
//                    1 for debited
//                    2 for refunded
//                    3 for all
                    "All"-> { // send status 3 for all
                        sharedPrefManager.getToken()?.let {
                            viewModel.getTransactionHistory(it,3)
                        }
                    }
                    "Credited" -> {
                        sharedPrefManager.getToken()?.let {
                            viewModel.getTransactionHistory(it,0)
                        }
                    }
                    "Debited" -> {
                        sharedPrefManager.getToken()?.let {
                            viewModel.getTransactionHistory(it,1)
                        }
                    }
                    "Refunded" -> {
                        sharedPrefManager.getToken()?.let {
                            viewModel.getTransactionHistory(it,2)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        isMoneyAdded.observe(this){ it ->
            if (it) {
                sharedPrefManager.getToken()?.let {
                    viewModel.getWallet(it)
                }
                sharedPrefManager.getToken()?.let {
                    viewModel.getTransactionHistory(it,3)
                }
            }
        }
        viewModel.obrWallet.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                        showHideLoader(true)
                }

                Status.SUCCESS -> {
                    //  showHideLoader(false)
                   // showSuccessToast(it.data?.message.toString())
                    if (it.data != null) {
                        binding.tv3.text = "₹ ${it.data.wallet}"
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

        viewModel.obrTransactionHistory.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.data.size != 0) {
                            binding.ivNoData.visibility = View.GONE
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            adapter1.list = it.data.data
                        } else {
                            binding.ivNoData.visibility = View.VISIBLE
                            binding.rvHomeDrawer.visibility = View.GONE
                        }
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
                R.id.tvAddMoneyWallet -> {
                    startActivity(AddMoneyActivity.newIntent(this))
                }

                R.id.iv1 -> {
                    finish()
                }

            }
        }
    }

    private lateinit var adapter1: SimpleRecyclerViewAdapter<WalletTransactionsData, HolderWalletTransactionsBinding>
    private fun initAdapter() {
        adapter1 = SimpleRecyclerViewAdapter(
            R.layout.holder_wallet_transactions, BR.bean
        ) { v, m, pos ->

        }
        binding.rvHomeDrawer.adapter = adapter1
    }
}