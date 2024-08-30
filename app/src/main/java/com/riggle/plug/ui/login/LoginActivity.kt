package com.riggle.plug.ui.login

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityLoginBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.projectList.ProjectListActivity
import com.riggle.plug.ui.resetPassword.ResetPasswordActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
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
        observers()
    }

    private fun initView() {
    }


    private fun observers() {
        viewModel.obrLogin.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        it.data.message.let { it1 -> showSuccessToast(it1) }
                        sharedPrefManager.saveUser(it.data.data)
                        sharedPrefManager.saveToken("Bearer ${it.data.data.auth_token}")
                        Log.e("savedData",sharedPrefManager.getToken().toString())
                        startActivity(ProjectListActivity.newIntent(this))
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

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.tvResetPass -> {
                    startActivity(ResetPasswordActivity.newIntent(this))
                }

                R.id.tvLogin -> {
                    val email = binding.etPhone.text.toString()
                    val pass = binding.etPassword.text.toString()
                    if (email == "") {
                        showErrorToast("Please enter email address")
                    } else if (pass == "") {
                        showErrorToast("Please enter password")
                    } else if (!isValidEmail(email)) {
                        showErrorToast("Please enter a valid email address")
                    } else if (pass.length < 8) {
                        showErrorToast("Password must be greater then 8 digits")
                    } else {
                        viewModel.finzaLogin(email, pass)
                    }
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}