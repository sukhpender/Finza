package com.riggle.plug.ui.finza.issueSuperTag.verify

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityVerifyTagBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.issueSuperTag.verifyOtp.VerifyOtpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyTagActivity : BaseActivity<ActivityVerifyTagBinding>() {

    private val viewModel: VerifyTagActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, VerifyTagActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_verify_tag
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initView() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.requestType)
        )
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }

                R.id.tvVerifyNumber -> {
                    startActivity(VerifyOtpActivity.newIntent(this))
                }
            }
        }
    }

}