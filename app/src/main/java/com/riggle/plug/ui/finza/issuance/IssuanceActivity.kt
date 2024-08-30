package com.riggle.plug.ui.finza.issuance

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.kal.rackmonthpicker.RackMonthPicker
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityIssuanceBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class IssuanceActivity : BaseActivity<ActivityIssuanceBinding>() {

    private val viewModel: IssuanceActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, IssuanceActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_issuance
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
                R.id.tvMonth -> {
                    initPicker()
                }
            }
        }
    }

    private fun initPicker(){
        RackMonthPicker(this)
            .setLocale(Locale.ENGLISH)
            .setColorTheme(R.color.line_color)
            .setPositiveButton { month, startDate, endDate, year, monthLabel ->
                binding.tvMonth.text = "$month $year"
            }
            .setNegativeButton { }.show()
    }
}