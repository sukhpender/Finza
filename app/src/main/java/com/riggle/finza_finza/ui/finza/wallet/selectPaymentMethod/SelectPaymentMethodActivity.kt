package com.riggle.finza_finza.ui.finza.wallet.selectPaymentMethod

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivitySelectPaymentMethodBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPaymentMethodActivity : BaseActivity<ActivitySelectPaymentMethodBinding>() {

    private val viewModel: SelectPaymentMethodActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, SelectPaymentMethodActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_select_payment_method
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
                R.id.iv1 -> {
                    finish()
                }
            }
        }
    }
}