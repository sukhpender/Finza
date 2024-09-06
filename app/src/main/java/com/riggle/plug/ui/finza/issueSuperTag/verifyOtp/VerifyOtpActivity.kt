package com.riggle.plug.ui.finza.issueSuperTag.verifyOtp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityVerifyOtpBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.issueSuperTag.userDetails.UserDetailsActivity
import com.riggle.plug.ui.finza.issueSuperTag.verify.VerifyTagActivity
import com.riggle.plug.ui.finza.issueSuperTag.verify.VerifyTagActivityVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpActivity : BaseActivity<ActivityVerifyOtpBinding>() {

    private val viewModel: VerifyOtpActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, VerifyOtpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_verify_otp
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
                R.id.iv1 ->{
                    finish()
                }
                R.id.tvSubmit -> {
                    startActivity(UserDetailsActivity.newIntent(this))
                }
            }
        }
    }
}