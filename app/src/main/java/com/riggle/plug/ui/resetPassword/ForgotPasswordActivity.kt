package com.riggle.plug.ui.resetPassword

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityResetPaswordBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity<ActivityResetPaswordBinding>() {

    private val viewModel: ResetPaswordActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ForgotPasswordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_reset_pasword
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.ivBackResetPassword -> {
                    finish()
                }
                R.id.tvContinue -> {
                    finish()
                }
            }
        }
    }

    private fun initView() {

    }
}