package com.riggle.plug.ui.finza.issueSuperTag.verify

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.riggle.plug.R
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.databinding.ActivityVerifyTagBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.issueSuperTag.verifyOtp.VerifyOtpActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@AndroidEntryPoint
class VerifyTagActivity : BaseActivity<ActivityVerifyTagBinding>() {

    private val viewModel: VerifyTagActivityVM by viewModels()
    var reqType = "REG"
    var provider = "bajaj"
    var isChassi = 0
    var resend = 0

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
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String
                if (selectedItem == "Registration") {
                    reqType = "REG"
                }
                if (selectedItem == "Replacement") {
                    reqType = "REP"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        viewModel.obrSendOtp.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        it.data.message.let { it1 -> showSuccessToast(it1) }
                        startActivity(VerifyOtpActivity.newIntent(this))
                        finish()
                        Log.e("SendOtpResponseModel--->>>", it.data.data.toString())
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }
                else -> {}
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }

                R.id.tvVerifyNumber -> {
                    val mNumber = binding.etPhone.text.toString()
                    val vehicleNumber = binding.etvVehicleNumber.text.toString()
                    val isChassis = binding.cbChassisNumber.isChecked
                    val chassisNumber = binding.etvChassisNumber.text.toString()
                    val engineNumber = binding.etvEngineNumber.text.toString()

                    if (isChassis) {
                        isChassi = 1
                    } else {
                        isChassi = 0
                    }

                    if (mNumber == "") {
                        showErrorToast("Please enter phone number")
                    } else if (mNumber.length != 10) {
                        showErrorToast("Please enter a valid phone number")
                    } else if (vehicleNumber == "") {
                        showErrorToast("Please enter vehicle number")
                    } else if (isChassis && chassisNumber == "") {
                        showErrorToast("Please enter chassis number")
                    } else if (isChassis && chassisNumber.length != 17) {
                        showErrorToast("Please enter a valid chassis number")
                    } else if (!isChassis && engineNumber == "") {
                        showErrorToast("Please enter engine number")
                    } else if (isChassis && engineNumber.length != 5) {
                        showErrorToast("Please enter valid last 5 digit ")
                    } else {
                        val requestBody = SendOtpRequest(
                            agentId = "",
                            channel = "",
                            chassisNo = chassisNumber,
                            engineNo = engineNumber,
                            isChassis = isChassi,
                            mobileNo = mNumber,
                            provider = provider,
                            reqDateTime = getCurrentDateFormatted(),
                            reqType = reqType,
                            requestId = generateRandom15DigitId(),
                            resend = resend,
                            udf1 = "",
                            udf2 = "",
                            udf3 = "",
                            udf4 = "",
                            udf5 = "",
                            vehicleNo = vehicleNumber
                        )
                        Log.e("SendOtpRequest--->>>", requestBody.toString())
                        viewModel.sendOtp(sharedPrefManager.getToken().toString(), requestBody)
                    }
                }
            }
        }
    }

    fun generateRandom15DigitId(): String {
        val random = Random.Default
        val random15DigitNumber = 100000000000000L + random.nextLong(900000000000000L)
        return "REQ$random15DigitNumber"
    }

    @SuppressLint("NewApi")
    fun getCurrentDateFormatted(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
}