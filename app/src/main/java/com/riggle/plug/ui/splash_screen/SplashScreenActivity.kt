package com.riggle.plug.ui.splash_screen

import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivitySplashScreenBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.FinzaHomeActivity
import com.riggle.plug.ui.finza.projectList.ProjectListActivity
import com.riggle.plug.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    private val viewModel: SplashScreenActivityVm by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.activity_splash_screen
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        setStatusTextColor()
        initHandler()

    }


    private fun initHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPrefManager.getCurrentUser() != null) {
                if (sharedPrefManager.getProjectId() != null) {
                    startActivity(FinzaHomeActivity.newIntent(this))
                    finish()
                }else{
                    startActivity(ProjectListActivity.newIntent(this))
                    finish()
                }
            } else {
                startActivity(LoginActivity.newIntent(this))
                finish()
            }
        }, 2500)
    }

    private fun setStatusTextColor() {
        window.decorView.systemUiVisibility = 0
    }
}