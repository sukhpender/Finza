package com.riggle.plug.ui.resetPassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityLoginBinding
import com.riggle.plug.databinding.ActivityResetPaswordBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.login.LoginActivity
import com.riggle.plug.ui.splash_screen.SplashScreenActivityVm
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity<ActivityResetPaswordBinding>() {

    private val viewModel: ResetPaswordActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ResetPasswordActivity::class.java)
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
                R.id.tvSave -> {
                    showSuccessToast("Reset Successfully")
                    finish()
                }

            }
        }
    }

    private fun initView() {

    }


}