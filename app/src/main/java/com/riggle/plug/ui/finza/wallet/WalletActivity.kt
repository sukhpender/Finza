package com.riggle.plug.ui.finza.wallet

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityWalletBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.wallet.addMoney.AddMoneyActivity
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
        initView()
        initOnClick()
    }

    private fun initView() {

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

                R.id.tvReceivedWallet -> {
                    setColor1(3)
                }

                R.id.tvSpentWallet -> {
                    setColor1(2)
                }

                R.id.tvAllWallet -> {
                    setColor1(1)
                }
            }
        }
    }

    private fun setColor1(type: Int){
        when(type) {
            1 -> {
                binding.tvAllWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.line_color
                    )
                )
                binding.tvSpentWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.fsm_text_color
                    )
                )
                binding.tvReceivedWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.fsm_text_color
                    )
                )
            }
            2 -> {
                binding.tvSpentWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.line_color
                    )
                )
                binding.tvAllWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.fsm_text_color
                    )
                )
                binding.tvReceivedWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.fsm_text_color
                    )
                )
            }
            3 -> {
                binding.tvReceivedWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.line_color
                    )
                )
                binding.tvSpentWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.fsm_text_color
                    )
                )
                binding.tvAllWallet.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.fsm_text_color
                    )
                )
            }
        }

    }
}