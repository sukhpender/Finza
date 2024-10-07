package com.riggle.finza_finza.ui.finza.issueSuperTag.userDetails

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityUserDetailsBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.issueSuperTag.uploadDocs.UploadDocumentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : BaseActivity<ActivityUserDetailsBinding>() {

    private val viewModel: UserDetailsActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, UserDetailsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_user_details
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
            resources.getStringArray(R.array.documentType)
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

        val adapter1 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.vehicleMaker)
        )
        binding.spVehicleMaker.adapter = adapter1

        binding.spVehicleMaker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                R.id.tvContinue -> {
                    startActivity(UploadDocumentActivity.newIntent(this))
                }
            }
        }
    }

}