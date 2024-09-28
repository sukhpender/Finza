package com.riggle.plug.ui.finza.issueSuperTag.verify

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.riggle.plug.R
import com.riggle.plug.data.model.CreateCustomerRew
import com.riggle.plug.data.model.CustomerDetails
import com.riggle.plug.data.model.CustomerRequest
import com.riggle.plug.data.model.Document
import com.riggle.plug.data.model.RequestWallet
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.data.model.ValidateOtpRequest
import com.riggle.plug.data.model.VehicleMakersRequest
import com.riggle.plug.data.model.VerifyOtpRequest
import com.riggle.plug.databinding.ActivityVerifyTagBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

@AndroidEntryPoint
class VerifyTagActivity : BaseActivity<ActivityVerifyTagBinding>() {

    private val viewModel: VerifyTagActivityVM by viewModels()
    private var reqType = "REG"
    private var provider = "bajaj"
    private var isChassi = 0
    private var resend = 0
    private var enteredOtp = ""
    private var requestId1 = ""
    private var sessionId1 = ""
    private var channel = ""
    private var agentId = ""
    private var mobile1 = ""
    private var vehicleManuf = ""
    private var model = ""
    private var documentType = 1
    private var vehicleMakersList = ArrayList<String>()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView() {
        initView()
        initOnClick()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        // verifyNumber()

        viewModel.obrSendOtp.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        Log.e("SendOtpResponseModel--->>>", it.data.data.toString())
                        if (it.data.data.validateCustResp?.requestId != null && it.data.data.validateCustResp.sessionId != null) {
                            it.data.message.let { it1 -> showSuccessToast(it1) }
                            requestId1 = it.data.data.validateCustResp.requestId
                            sessionId1 = it.data.data.validateCustResp.sessionId
                            channel = it.data.data.validateCustResp.channel
                            agentId = it.data.data.validateCustResp.agentId
                            mobile1 = it.data.data.validateCustResp.mobileNo
                            verifyOtp()
                        } else {
                            showErrorToast("Response Issue")
                        }
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

        viewModel.obrVerifyOtp.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        it.data.message.let { it1 -> showSuccessToast(it1) }
                        Log.e("VerifyOtpResponseModel--->>>", it.data.data.toString())
                        if (it.data.data.validateOtpResp.custDetails.walletStatus == "Active") {
                            if (it.data.data.validateOtpResp.vrnDetails.vehicleManuf == null || it.data.data.validateOtpResp.vrnDetails.vehicleManuf == "" && it.data.data.validateOtpResp.vrnDetails.model == null || it.data.data.validateOtpResp.vrnDetails.model == "") {
                                val req = VehicleMakersRequest(
                                    requestId = requestId1,
                                    sessionId = sessionId1,
                                    channel = channel,
                                    agentId = agentId,
                                    reqDateTime = getCurrentDateFormatted(),
                                    provider = provider
                                )
                                viewModel.getMakersList(
                                    sharedPrefManager.getToken().toString(), req
                                )
                            } else {
                                vehicleManuf = it.data.data.validateOtpResp.vrnDetails.vehicleManuf
                                model = it.data.data.validateOtpResp.vrnDetails.model.toString()
                            }
                        } else {

                        }
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

        viewModel.obrMakersList.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        //   it.data.response.msg.let { it1 -> showSuccessToast(it1) }
                        Log.e("VehicleMakerListResponseModel--->>>", it.data.toString())
                        vehicleMakersList = it.data.vehicleMakerList as ArrayList<String>
                        createCustomer()
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

        viewModel.obrCreateBajajCustomer.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        //   it.data.response.msg.let { it1 -> showSuccessToast(it1) }
                        Log.e("VehicleMakerListResponseModel--->>>", it.data.toString())
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

        binding.otpView.setOtpCompletionListener {
            enteredOtp = it
            Log.d("Actual Value-----------", it)
        }

        // caps chassis number
        binding.etvChassisNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val upperCase = s.toString().uppercase(Locale.getDefault())
                if (s.toString() != upperCase) {
                    binding.etvChassisNumber.setText(upperCase)
                    binding.etvChassisNumber.setSelection(upperCase.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // caps vehicle number
        binding.etvVehicleNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val upperCase = s.toString().uppercase(Locale.getDefault())
                if (s.toString() != upperCase) {
                    binding.etvVehicleNumber.setText(upperCase)
                    binding.etvVehicleNumber.setSelection(upperCase.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // caps document number
        binding.etvDocumentNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val upperCase = s.toString().uppercase(Locale.getDefault())
                if (s.toString() != upperCase) {
                    binding.etvDocumentNumber.setText(upperCase)
                    binding.etvDocumentNumber.setSelection(upperCase.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun uploadDocument() {
        binding.tvHeader.text = "Upload Document"
        binding.llCreateCustomer.visibility = View.GONE
        binding.llVerifyNumber.visibility = View.GONE
        binding.llUploadDocument.visibility = View.VISIBLE
        binding.llVerifyOtp.visibility = View.GONE
    }

    private fun createCustomer() {
        binding.tvHeader.text = "User Details"
        binding.etvMobile.setText(mobile1)
        binding.llCreateCustomer.visibility = View.VISIBLE
        binding.llVerifyNumber.visibility = View.GONE
        binding.llUploadDocument.visibility = View.GONE
        binding.llVerifyOtp.visibility = View.GONE

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.documentType)
        )
        binding.spDocumentType.adapter = adapter
        binding.spDocumentType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    /**
                     * 1. Pan Card
                     * 2. Driving License (Need expiry)
                     * 3. Voter ID
                     * 4. Passport Number (Need expiry)
                     * */
                    val selectedItem = parent.getItemAtPosition(position) as String
                    if (selectedItem == "Pan Card") {
                        documentType = 1
                        binding.llDocExpiry.visibility = View.GONE
                        binding.etvDocumentExpiry.text = ""
                    }
                    if (selectedItem == "Driving License") {
                        documentType = 2
                        binding.llDocExpiry.visibility = View.VISIBLE
                        binding.etvDocumentExpiry.text = ""
                    }
                    if (selectedItem == "Voter ID") {
                        documentType = 3
                        binding.llDocExpiry.visibility = View.GONE
                        binding.etvDocumentExpiry.text = ""
                    }
                    if (selectedItem == "Passport Number") {
                        documentType = 4
                        binding.llDocExpiry.visibility = View.VISIBLE
                        binding.etvDocumentExpiry.text = ""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun verifyNumber() {
        binding.tvHeader.text = "Verify Number"
        binding.llVerifyNumber.visibility = View.VISIBLE
        binding.llVerifyOtp.visibility = View.GONE
        binding.llUploadDocument.visibility = View.GONE
    }

    private fun verifyOtp() {
        binding.tvHeader.text = "Verify Otp"
        binding.llVerifyNumber.visibility = View.GONE
        binding.llVerifyOtp.visibility = View.VISIBLE
        binding.llUploadDocument.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }

                // send otp -------------------------------
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

                // verify otp -----------------------------
                R.id.tvSubmit -> {
                    if (enteredOtp == "") {
                        showErrorToast("Please enter otp")
                    } else if (enteredOtp.length != 6) {
                        showErrorToast("Please enter complete otp")
                    } else {
                        val validateOtpReq = ValidateOtpRequest(
                            VerifyOtpRequest(
                                otp = enteredOtp,
                                requestId = requestId1,
                                sessionId = sessionId1,
                                channel = channel,
                                agentId = agentId,
                                reqDateTime = getCurrentDateFormatted(),
                                provider = provider
                            )
                        )

                        viewModel.verifyOtp(sharedPrefManager.getToken().toString(), validateOtpReq)
                    }
                }

                R.id.ivCalender, R.id.llDob -> {
                    showDatePickerDialog()
                }

                /** ---------- user details ----------- */
                R.id.tvContinue -> {
                    val fName = binding.etvFName.text.toString()
                    val lName = binding.etvLName.text.toString()
                    val mobile = binding.etvMobile.text.toString()
                    val dob = binding.etvDob.text.toString()
                    val documentNumber = binding.etvDob.text.toString()
                    val documentExpiry = binding.etvDocumentExpiry.text.toString()

                    if (fName == "") {
                        showErrorToast("Please enter first name")
                    } else if (lName == "") {
                        showErrorToast("Please enter last name")
                    } else if (mobile == "") {
                        showErrorToast("Please enter mobile number")
                    } else if (dob == "") {
                        showErrorToast("Please enter date of birth")
                    } else if (documentNumber == "") {
                        showErrorToast("Please enter document number")
                    } else if (documentType == 2 || documentType == 4 && documentExpiry == "") {
                        showErrorToast("Please select document expiry date")
                    } else {
                        val request = CreateCustomerRew(
                            createCustomerReq = CustomerRequest(
                                reqWallet = RequestWallet(
                                    requestId = requestId1,
                                    sessionId = sessionId1,
                                    channel = channel,
                                    agentId = agentId,
                                    reqDateTime = getCurrentDateFormatted(),
                                    provider = provider
                                ), custDetails = CustomerDetails(
                                    name = fName,
                                    lastName = lName,
                                    mobileNo = mobile,
                                    dob = dob,
                                    doc = listOf(
                                        Document(
                                            docType = documentType.toString(),
                                            docNo = documentNumber
                                        )
                                    ),
                                    udf1 = "",
                                    udf2 = "",
                                    udf3 = "",
                                    udf4 = "",
                                    udf5 = ""
                                ), provider = provider
                            )
                        )
                        viewModel.createCustomer(sharedPrefManager.getToken().toString(), request)
                    }
                }

                R.id.llDocExpiry, R.id.ivCalender1 -> {
                    showDatePickerDialog1()
                }

                /// upload documents
                R.id.tvSelectRcFront -> {

                }

                R.id.tvUploadRcFront -> {

                }

                R.id.tvSelectRcBack -> {

                }

                R.id.tvUploadRcBack -> {

                }

                R.id.tvSelectCarSide -> {

                }

                R.id.tvUploadCarSide -> {

                }

                R.id.tvSelectCarFront -> {

                }

                R.id.tvUploadCarFront -> {

                }

                R.id.tvSelectTagaFix -> {

                }

                R.id.tvUploadTagaFix -> {

                }

                R.id.tvContinueUpload -> {

                }

            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.etvDob.text = sdf.format(selectedDate.time)
            }, year, month, day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Restrict to past dates
        datePickerDialog.setOnCancelListener { }
        datePickerDialog.show()
    }

    private fun showDatePickerDialog1() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.etvDocumentExpiry.text = sdf.format(selectedDate.time)
            }, year, month, day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Restrict to past dates
        datePickerDialog.setOnCancelListener { }
        datePickerDialog.show()
    }


    private fun generateRandom15DigitId(): String {
        val random = Random.Default
        val random15DigitNumber = 100000000000000L + random.nextLong(900000000000000L)
        return "REQ$random15DigitNumber"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDateFormatted(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        return LocalDateTime.now().format(formatter)
    }
}