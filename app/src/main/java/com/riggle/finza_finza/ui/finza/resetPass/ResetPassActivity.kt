package com.riggle.finza_finza.ui.finza.resetPass

import android.app.Activity
import android.content.Intent
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityResetPassBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPassActivity : BaseActivity<ActivityResetPassBinding>() {

    private val viewModel: ResetPassActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ResetPassActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_reset_pass
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)

        initView()
        initOnClick()
    }

    private fun initView() {
        viewModel.obrReset.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    it.data?.message?.let { it1 -> showSuccessToast(it1) }
                    finish()

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

                R.id.tvReset -> {
                    val pass = binding.etvNewPass.text.toString()
                    val confirmPass = binding.etvConfirmNewPass.text.toString()

                    if (pass == "") {
                        showErrorToast("Please enter password")
                    } else if (pass.length < 8) {
                        showErrorToast("Password must be greater then 8 digits")
                    } else if (confirmPass == "") {
                        showErrorToast("Please enter confirm password")
                    } else if (confirmPass.length < 8) {
                        showErrorToast("Password must be greater then 8 digits")
                    } else if (pass.toString() != confirmPass.toString()) {
                        showErrorToast("Password and Confirm Password not match")
                    } else {
                        viewModel.resetPass(
                            sharedPrefManager.getCurrentUser()?.id.toString(),
                            pass,
                            confirmPass
                        )
                    }
                }
            }
        }
    }
}