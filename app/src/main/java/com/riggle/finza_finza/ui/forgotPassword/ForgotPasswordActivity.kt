package com.riggle.finza_finza.ui.forgotPassword

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityResetPaswordBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity<ActivityResetPaswordBinding>() {

    private val viewModel: ForgotPasswordActivityVM by viewModels()

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
                    if (binding.etvEmail.text.toString() == "") {
                        showErrorToast("Please enter email address")
                    } else if (!isValidEmail(binding.etvEmail.text.toString())) {
                        showErrorToast("Please enter a valid email address")
                    } else {
                        viewModel.forgotPass(binding.etvEmail.text.toString())
                    }
                }
            }
        }
    }

    private fun initView() {
        viewModel.obrFPass.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        it.data.message.let { it1 -> showSuccessToast(it1) }
                        finish()
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

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}