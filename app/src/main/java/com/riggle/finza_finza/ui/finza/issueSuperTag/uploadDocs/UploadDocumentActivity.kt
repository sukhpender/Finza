package com.riggle.finza_finza.ui.finza.issueSuperTag.uploadDocs

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityUploadDocumentBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.issueSuperTag.fastagReg.FastagRegistrationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadDocumentActivity : BaseActivity<ActivityUploadDocumentBinding>() {

    private val viewModel: UploadDocumentActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, UploadDocumentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_upload_document
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
                R.id.iv1 -> {
                    finish()
                }

                R.id.tvUpload -> {
                    startActivity(FastagRegistrationActivity.newIntent(this))
                }
            }
        }
    }
}