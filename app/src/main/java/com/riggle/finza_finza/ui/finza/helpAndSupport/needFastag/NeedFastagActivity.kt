package com.riggle.finza_finza.ui.finza.helpAndSupport.needFastag

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityNeedFastagBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NeedFastagActivity : BaseActivity<ActivityNeedFastagBinding>() {

    private val viewModel: NeedFastagActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, NeedFastagActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_need_fastag
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
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.banks))
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) { }
            override fun onNothingSelected(parent: AdapterView<*>) { }
        }
    }

    private fun observers() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }R.id.tvConfirm -> {
                    finish()
                }
            }
        }
    }
}