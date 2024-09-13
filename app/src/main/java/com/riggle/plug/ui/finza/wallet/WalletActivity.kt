package com.riggle.plug.ui.finza.wallet

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.Drawer
import com.riggle.plug.databinding.ActivityWalletBinding
import com.riggle.plug.databinding.HolderWalletTransactionsBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.wallet.addMoney.AddMoneyActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
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
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        viewModel.obrWallet.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.tv3.text  =  "₹ ${it.data.wallet}"
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

    private lateinit var adapter: SimpleRecyclerViewAdapter<Drawer, HolderWalletTransactionsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_wallet_transactions, BR.bean
        ) { v, m, pos ->

        }
        binding.rvHomeDrawer.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<Drawer> {
        val list = ArrayList<Drawer>()
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))
        list.add(Drawer(R.drawable.wallet, "25 Dec 2022"))

        return list
    }
}