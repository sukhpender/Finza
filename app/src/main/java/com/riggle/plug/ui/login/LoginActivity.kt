package com.riggle.plug.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.databinding.ActivityLoginBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.FinzaHomeActivity
import com.riggle.plug.ui.finza.projectList.ProjectListActivity
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.resetPassword.ResetPasswordActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.preventClick
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showInfoToast
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

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.tvResetPass -> {
                    startActivity(ResetPasswordActivity.newIntent(this))
                }
                R.id.tvLogin -> {
                    showSuccessToast("Login Successfully")
                    startActivity(ProjectListActivity.newIntent(this))
                    finish()
                }
            }
        }
    }
}