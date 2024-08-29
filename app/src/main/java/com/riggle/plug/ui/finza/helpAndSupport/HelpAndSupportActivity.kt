package com.riggle.plug.ui.finza.helpAndSupport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityHelpAndSupportBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.projectList.ProjectListActivity
import com.riggle.plug.ui.login.LoginActivity
import com.riggle.plug.ui.login.LoginActivityVM
import com.riggle.plug.ui.resetPassword.ResetPasswordActivity
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpAndSupportActivity : BaseActivity<ActivityHelpAndSupportBinding>() {

    private val viewModel: HelpAndSupportActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, HelpAndSupportActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_help_and_support
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
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
                R.id.iv1 -> {
                    finish()
                }
                R.id.etvReplacement -> {

                }
                R.id.etvVRN -> {

                }
            }
        }
    }
}