package com.riggle.finza_finza.ui.finza.helpAndSupport

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityHelpAndSupportBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.helpAndSupport.fitmentCerti.FitmentCertificateActivity
import com.riggle.finza_finza.ui.finza.helpAndSupport.kyc.UpdateKYCActivity
import com.riggle.finza_finza.ui.finza.helpAndSupport.needFastag.NeedFastagActivity
import com.riggle.finza_finza.ui.finza.helpAndSupport.replaceFASTag.ReplaceFASTagActivity
import com.riggle.finza_finza.ui.finza.helpAndSupport.updateVehicleNumber.UpdateVehicleNumberActivity
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

                R.id.etvFTReplacement -> { // fastag replacement
                    startActivity(ReplaceFASTagActivity.newIntent(this))
                }

                R.id.etvUpdateVN -> { // update vehicle number
                    startActivity(UpdateVehicleNumberActivity.newIntent(this))
                }

                R.id.etvUpdateKYC -> { // update KYC
                    startActivity(UpdateKYCActivity.newIntent(this))
                }

                R.id.etvFitment -> { // fitment certificate
                    startActivity(FitmentCertificateActivity.newIntent(this))
                }

                R.id.etvNeedFastag -> { // need fast tag
                    startActivity(NeedFastagActivity.newIntent(this))
                }
            }
        }
    }
}