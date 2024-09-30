package com.riggle.plug.ui.finza.issueSuperTag.verify

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.github.dhaval2404.imagepicker.ImagePicker
import com.riggle.plug.R
import com.riggle.plug.data.model.CreateCustomerRew
import com.riggle.plug.data.model.CustDetails2
import com.riggle.plug.data.model.CustomerDetails
import com.riggle.plug.data.model.CustomerRequest
import com.riggle.plug.data.model.Document
import com.riggle.plug.data.model.FasTagDetails
import com.riggle.plug.data.model.RegDetails
import com.riggle.plug.data.model.RegisterTagRequest
import com.riggle.plug.data.model.RequestWallet
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.data.model.ValidateOtpRequest
import com.riggle.plug.data.model.VehicleMakersRequest
import com.riggle.plug.data.model.VerifyOtpRequest
import com.riggle.plug.data.model.VrnDetails1
import com.riggle.plug.databinding.ActivityVerifyTagBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.permission.PermissionHandler
import com.riggle.plug.ui.base.permission.Permissions
import com.riggle.plug.utils.AppUtils
import com.riggle.plug.utils.RealPath.getFilePath
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showInfoToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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
    private var walletId1 = ""
    private var userName = ""
    private var vehicleManuf = ""
    private var vehicleNumber = ""
    private var vehicleColour1 = ""
    private var vehicleDescriptor1 = ""
    private var isNationalPermit1 = ""
    private var permitExpiryDate1 = ""
    private var stateOfRegistration1 = ""
    private var tagVehicleClassID1 = ""
    private var status = ""
    private var npciStatus = ""
    private var npciVehicleClassID1 = ""
    private var vehicleType = ""
    private var type = ""
    private var vehicleChassisNumber = ""
    private var vehicleEngineNumber = ""
    private var model = ""
    private var tagCost = ""
    private var rechargeAmount = ""
    private var securityDeposit = ""
    private var documentType = 1
    private var isCommerical = false
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
        verifyNumber()

        binding.cbIsCommerical.setOnCheckedChangeListener { buttonView, isChecked ->
            isCommerical = isChecked
        }

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
                            vehicleChassisNumber = it.data.data.validateCustResp.chassisNo
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
                        if (it.data.data.validateOtpResp.custDetails.walletStatus != "Active") {
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
                                walletId1 = it.data.data.validateOtpResp.custDetails.walletId
                                userName = it.data.data.validateOtpResp.custDetails.name
                                vehicleNumber = it.data.data.validateOtpResp.vrnDetails.vehicleNo
                                vehicleColour1 =
                                    it.data.data.validateOtpResp.vrnDetails.vehicleColour
                                vehicleDescriptor1 =
                                    it.data.data.validateOtpResp.vrnDetails.vehicleDescriptor.toString()
                                isNationalPermit1 =
                                    it.data.data.validateOtpResp.vrnDetails.isNationalPermit.toString()
                                permitExpiryDate1 =
                                    it.data.data.validateOtpResp.vrnDetails.permitExpiryDate.toString()
                                stateOfRegistration1 =
                                    it.data.data.validateOtpResp.vrnDetails.stateOfRegistration.toString()
                                tagVehicleClassID1 =
                                    it.data.data.validateOtpResp.vrnDetails.tagVehicleClassID
                                npciVehicleClassID1 =
                                    it.data.data.validateOtpResp.vrnDetails.npciVehicleClassID
                                type = it.data.data.validateOtpResp.vrnDetails.type
                                vehicleType = it.data.data.validateOtpResp.vrnDetails.vehicleType
                                vehicleEngineNumber =
                                    it.data.data.validateOtpResp.vrnDetails.engineNo
                                tagCost =
                                    it.data.data.validateOtpResp.vrnDetails.tagCost
                                securityDeposit =
                                    it.data.data.validateOtpResp.vrnDetails.securityDeposit
                                rechargeAmount =
                                    it.data.data.validateOtpResp.vrnDetails.rechargeAmount
                                createCustomer()
                            }
                        } else {
                            vehicleManuf =
                                it.data.data.validateOtpResp.vrnDetails.vehicleManuf.toString()
                            model = it.data.data.validateOtpResp.vrnDetails.model.toString()
                            walletId1 = it.data.data.validateOtpResp.custDetails.walletId
                            userName = it.data.data.validateOtpResp.custDetails.name
                            vehicleNumber = it.data.data.validateOtpResp.vrnDetails.vehicleNo
                            vehicleColour1 = it.data.data.validateOtpResp.vrnDetails.vehicleColour
                            vehicleDescriptor1 =
                                it.data.data.validateOtpResp.vrnDetails.vehicleDescriptor.toString()
                            isNationalPermit1 =
                                it.data.data.validateOtpResp.vrnDetails.isNationalPermit.toString()
                            permitExpiryDate1 =
                                it.data.data.validateOtpResp.vrnDetails.permitExpiryDate.toString()
                            stateOfRegistration1 =
                                it.data.data.validateOtpResp.vrnDetails.stateOfRegistration.toString()
                            tagVehicleClassID1 =
                                it.data.data.validateOtpResp.vrnDetails.tagVehicleClassID
                            npciVehicleClassID1 =
                                it.data.data.validateOtpResp.vrnDetails.npciVehicleClassID
                            type = it.data.data.validateOtpResp.vrnDetails.type
                            vehicleType = it.data.data.validateOtpResp.vrnDetails.vehicleType
                            vehicleEngineNumber = it.data.data.validateOtpResp.vrnDetails.engineNo
                            tagCost =
                                it.data.data.validateOtpResp.vrnDetails.tagCost
                            securityDeposit =
                                it.data.data.validateOtpResp.vrnDetails.securityDeposit
                            rechargeAmount =
                                it.data.data.validateOtpResp.vrnDetails.rechargeAmount
                            uploadDocument()
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
                        Log.e("UserCreateResponse------", it.data.toString())
                        walletId1 = it.data.custDetails.walletId
                        userName = it.data.custDetails.name
                        uploadDocument()
                    } else {
                        showErrorToast(it.message.toString())
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

        viewModel.obrUploadDocument.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        //   it.data.response.msg.let { it1 -> showSuccessToast(it1) }
                        Log.e("UploadDocumentResponse------", it.data.toString())
                    } else {
                        showErrorToast(it.message.toString())
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

        viewModel.obrRegisterTag.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                           it.data.response.msg.let { it1 -> showSuccessToast(it1) }
                        Log.e("UploadDocumentResponse------", it.data.toString())
                        finish()
                    } else {
                        showErrorToast(it.message.toString())
                        finish()
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

    private fun registerTag() {
        binding.tvHeader.text = "Upload Document"
        binding.llRegisterTag.visibility = View.VISIBLE
        binding.llCreateCustomer.visibility = View.GONE
        binding.llVerifyNumber.visibility = View.GONE
        binding.llUploadDocument.visibility = View.GONE
        binding.llVerifyOtp.visibility = View.GONE
        binding.etv1.setText(userName)
        binding.etv2.setText(mobile1)
        binding.etv4.setText(vehicleNumber)
        binding.etv5.setText(vehicleChassisNumber)
        binding.etv6.setText(vehicleEngineNumber)
        binding.etv8.setText(vehicleManuf)
        binding.etv9.setText(model)

        binding.etv20.setText(rechargeAmount)
        binding.etv21.setText(securityDeposit)
        binding.etv22.setText(tagCost)
    }

    private fun uploadDocument() {
        binding.tvHeader.text = "Upload Document"
        binding.llRegisterTag.visibility = View.GONE
        binding.llCreateCustomer.visibility = View.GONE
        binding.llVerifyNumber.visibility = View.GONE
        binding.llUploadDocument.visibility = View.VISIBLE
        binding.llVerifyOtp.visibility = View.GONE
    }

    private fun createCustomer() {
        binding.tvHeader.text = "User Details"
        binding.llRegisterTag.visibility = View.GONE
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
                    binding.etvDocumentExpiry.text = ""
                    binding.etvDocumentNumber.setText("")
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
        binding.llRegisterTag.visibility = View.GONE
        binding.llVerifyNumber.visibility = View.VISIBLE
        binding.llVerifyOtp.visibility = View.GONE
        binding.llUploadDocument.visibility = View.GONE
        binding.llRegisterTag.visibility = View.GONE
    }

    private fun verifyOtp() {
        binding.llRegisterTag.visibility = View.GONE
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

                /** ---- Register Tag ---- */
                R.id.tvRegister -> {
                    val tagNumber1 = binding.etvFastTagId1.text.toString()
                    val tagNumber2 = binding.etvFastTagId2.text.toString()
                    val tagNumber3 = binding.etvFastTagId3.text.toString()
                    val engineNumberComplete = binding.etv6.text.toString()
                    val rechargeAmount = binding.etv20.text.toString()
                    val securityDeposit = binding.etv21.text.toString()
                    val tagCost = binding.etv22.text.toString()
                    val debitAmount = binding.etv23.text.toString()

                    if (tagNumber1 == "") {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber2 == "") {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber3 == "") {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (engineNumberComplete == "") {
                        showErrorToast("Please enter complete engine number")
                    } else if (rechargeAmount == "") {
                        showErrorToast("Please enter Recharge amount")
                    } else if (securityDeposit == "") {
                        showErrorToast("Please enter Security amount")
                    } else if (tagCost == "") {
                        showErrorToast("Please enter Tag amount")
                    } else if (debitAmount == "") {
                        showErrorToast("Please enter Debit amount")
                    } else {
                        val regDetails = RegDetails(
                            requestId = requestId1,
                            sessionId = sessionId1,
                            channel = channel,
                            agentId = agentId,
                            reqDateTime = getCurrentDateFormatted()
                        )

                        val vrnDetails = VrnDetails1(
                            vrn = vehicleNumber,
                            chassis = vehicleChassisNumber,
                            engine = engineNumberComplete,
                            vehicleManuf = vehicleManuf,
                            model = model,
                            vehicleColour = vehicleColour1,
                            type = type,
                            status = "Active",
                            npciStatus = "success",
                            isCommercial = isCommerical,
                            tagVehicleClassID = tagVehicleClassID1,
                            npciVehicleClassID = npciVehicleClassID1,
                            vehicleType = vehicleType,
                            rechargeAmount = rechargeAmount,
                            securityDeposit = securityDeposit,
                            tagCost = tagCost,
                            debitAmt = debitAmount,
                            vehicleDescriptor = vehicleDescriptor1,
                            isNationalPermit = isNationalPermit1,
                            permitExpiryDate = permitExpiryDate1,
                            stateOfRegistration = stateOfRegistration1
                        )

                        val custDetails = CustDetails2(
                            name = userName, mobileNo = mobile1, walletId = walletId1
                        )

                        val fasTagDetails = FasTagDetails(
                            serialNo = "608268-001-0062357",
                            tid = "",
                            udf1 = "",
                            udf2 = "",
                            udf3 = "",
                            udf4 = "",
                            udf5 = ""
                        )

                        val request = RegisterTagRequest(
                            regDetails = regDetails,
                            vrnDetails = vrnDetails,
                            custDetails = custDetails,
                            fasTagDetails = fasTagDetails,
                            provider = provider
                        )
                        viewModel.registerTag(sharedPrefManager.getToken().toString(), request)
                    }
                }

                /** ---------- user details ----------- */
                R.id.tvContinue -> {
                    val fName = binding.etvFName.text.toString()
                    val lName = binding.etvLName.text.toString()
                    val mobile = binding.etvMobile.text.toString()
                    val dob = binding.etvDob.text.toString()
                    val documentNumber = binding.etvDocumentNumber.text.toString()
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
                    } else if ((documentType == 2 || documentType == 4) && documentExpiry == "") {
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
                                            docNo = documentNumber,
                                            expiryDate = documentExpiry
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
                    checkPermission(binding.tvSelectRcFront, "1")
                }


                R.id.tvUploadRcFront -> {
                    if (rcFrontFile == null) {
                        showErrorToast("Please select RC front image")
                    } else {
                        val requestFile =
                            rcFrontFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "image", rcFrontFile?.name, requestFile!!
                        )
                        viewModel.uploadDocument(
                            sharedPrefManager.getToken().toString(),
                            AppUtils.textToRequestBody(requestId1),
                            AppUtils.textToRequestBody(sessionId1),
                            AppUtils.textToRequestBody(channel),
                            AppUtils.textToRequestBody(agentId),
                            AppUtils.textToRequestBody(getCurrentDateFormatted()),
                            AppUtils.textToRequestBody("RCFRONT"),
                            body,
                            AppUtils.textToRequestBody(provider)
                        )
                    }
                }

                R.id.tvSelectRcBack -> {
                    checkPermission(binding.tvSelectRcBack, "2")
                }

                R.id.tvUploadRcBack -> {
                    if (rcBackFile == null) {
                        showErrorToast("Please select RC back image")
                    } else {
                        val requestFile =
                            rcBackFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "image", rcBackFile?.name, requestFile!!
                        )
                        viewModel.uploadDocument(
                            sharedPrefManager.getToken().toString(),
                            AppUtils.textToRequestBody(requestId1),
                            AppUtils.textToRequestBody(sessionId1),
                            AppUtils.textToRequestBody(channel),
                            AppUtils.textToRequestBody(agentId),
                            AppUtils.textToRequestBody(getCurrentDateFormatted()),
                            AppUtils.textToRequestBody("RCBACK"),
                            body,
                            AppUtils.textToRequestBody(provider)
                        )
                    }
                }

                R.id.tvSelectCarSide -> {
                    checkPermission(binding.tvSelectCarSide, "4")
                }

                R.id.tvUploadCarSide -> {
                    if (carSideFile == null) {
                        showErrorToast("Please select Car side image")
                    } else {
                        val requestFile =
                            carSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "image", carSideFile?.name, requestFile!!
                        )
                        viewModel.uploadDocument(
                            sharedPrefManager.getToken().toString(),
                            AppUtils.textToRequestBody(requestId1),
                            AppUtils.textToRequestBody(sessionId1),
                            AppUtils.textToRequestBody(channel),
                            AppUtils.textToRequestBody(agentId),
                            AppUtils.textToRequestBody(getCurrentDateFormatted()),
                            AppUtils.textToRequestBody("VEHICLESIDE"),
                            body,
                            AppUtils.textToRequestBody(provider)
                        )
                    }
                }

                R.id.tvSelectCarFront -> {
                    checkPermission(binding.tvSelectCarFront, "3")
                }

                R.id.tvUploadCarFront -> {
                    if (carFrontFile == null) {
                        showErrorToast("Please select Car front image")
                    } else {
                        val requestFile =
                            carFrontFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "image", carFrontFile?.name, requestFile!!
                        )
                        viewModel.uploadDocument(
                            sharedPrefManager.getToken().toString(),
                            AppUtils.textToRequestBody(requestId1),
                            AppUtils.textToRequestBody(sessionId1),
                            AppUtils.textToRequestBody(channel),
                            AppUtils.textToRequestBody(agentId),
                            AppUtils.textToRequestBody(getCurrentDateFormatted()),
                            AppUtils.textToRequestBody("VEHICLEFRONT"),
                            body,
                            AppUtils.textToRequestBody(provider)
                        )
                    }
                }

                R.id.tvSelectTagaFix -> {
                    checkPermission(binding.tvSelectTagaFix, "5")
                }

                R.id.tvUploadTagaFix -> {
                    if (tagafixFile == null) {
                        showErrorToast("Please select TagaFix image")
                    } else {
                        val requestFile =
                            tagafixFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "image", tagafixFile?.name, requestFile!!
                        )
                        viewModel.uploadDocument(
                            sharedPrefManager.getToken().toString(),
                            AppUtils.textToRequestBody(requestId1),
                            AppUtils.textToRequestBody(sessionId1),
                            AppUtils.textToRequestBody(channel),
                            AppUtils.textToRequestBody(agentId),
                            AppUtils.textToRequestBody(getCurrentDateFormatted()),
                            AppUtils.textToRequestBody("TAGAFFIX"),
                            body,
                            AppUtils.textToRequestBody(provider)
                        )
                    }
                }

                R.id.tvContinueUpload -> {
                    if (rcFrontFile == null) {
                        showErrorToast("Please upload RC front")
                    } else if (rcBackFile == null) {
                        showErrorToast("Please upload RC back")
                    } else if (carFrontFile == null) {
                        showErrorToast("Please upload Car front")
                    } else if (carSideFile == null) {
                        showErrorToast("Please upload Car side")
                    } else if (tagafixFile == null) {
                        showErrorToast("Please upload TagaFix")
                    } else {
                        registerTag()
                    }
                }
            }
        }
    }

    private fun checkPermission(v: View, forWhich: String) {
        Permissions.check(this, permissions(), 0, null, object : PermissionHandler() {
            override fun onGranted() {
                when (forWhich) {
                    "1" -> {
                        getRCFront(v)
                    }

                    "2" -> {
                        getRCBack(v)
                    }

                    "3" -> {
                        getCarFront(v)
                    }

                    "4" -> {
                        getCarSide(v)
                    }

                    "5" -> {
                        getTagaFix(v)
                    }
                }
            }

            override fun onDenied(
                context: Context?, deniedPermissions: ArrayList<String>?
            ) {
                super.onDenied(context, deniedPermissions)
                showInfoToast("Accept Storage permission to continue")
            }
        })
    }

    private var tagafixFile: File? = null
    private val startForTagaFixImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                tagafixFile = File(fileUri.getFilePath(this))
                binding.ivTAGAFFIX.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

    private fun getTagaFix(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(this).cropSquare().cameraOnly().createIntent { intent ->
                        startForTagaFixImageResult.launch(intent)
                    }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(this).cropSquare().galleryOnly().createIntent { intent ->
                        startForTagaFixImageResult.launch(intent)
                    }
                }

                else -> {
                    popupMenu.dismiss()
                }
            }
            true
        }
        popupMenu.show()
    }

    private var carFrontFile: File? = null
    private val startForCarFrontImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                carFrontFile = File(fileUri.getFilePath(this))
                binding.ivCarFront.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

    private fun getCarFront(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(this).cropSquare().cameraOnly().createIntent { intent ->
                        startForCarFrontImageResult.launch(intent)
                    }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(this).cropSquare().galleryOnly().createIntent { intent ->
                        startForCarFrontImageResult.launch(intent)
                    }
                }

                else -> {
                    popupMenu.dismiss()
                }
            }
            true
        }
        popupMenu.show()
    }

    private var carSideFile: File? = null
    private val startForCarSideImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                carSideFile = File(fileUri.getFilePath(this))
                binding.ivCarSide.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

    private fun getCarSide(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(this).cropSquare().cameraOnly().createIntent { intent ->
                        startForCarSideImageResult.launch(intent)
                    }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(this).cropSquare().galleryOnly().createIntent { intent ->
                        startForCarSideImageResult.launch(intent)
                    }
                }

                else -> {
                    popupMenu.dismiss()
                }
            }
            true
        }
        popupMenu.show()
    }

    private var rcBackFile: File? = null
    private val startForRCBackImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                rcBackFile = File(fileUri.getFilePath(this))
                binding.ivRCBack.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

    private fun getRCBack(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(this).cropSquare().cameraOnly().createIntent { intent ->
                        startForRCBackImageResult.launch(intent)
                    }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(this).cropSquare().galleryOnly().createIntent { intent ->
                        startForRCBackImageResult.launch(intent)
                    }
                }

                else -> {
                    popupMenu.dismiss()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun getRCFront(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(this).cropSquare().cameraOnly().createIntent { intent ->
                        startForRCFrontImageResult.launch(intent)
                    }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(this).cropSquare().galleryOnly().createIntent { intent ->
                        startForRCFrontImageResult.launch(intent)
                    }
                }

                else -> {
                    popupMenu.dismiss()
                }
            }
            true
        }
        popupMenu.show()
    }

    private var rcFrontFile: File? = null
    private val startForRCFrontImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                rcFrontFile = File(fileUri.getFilePath(this))
                binding.ivRCFront.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

    private var storge_permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private var storge_permissions1 = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storge_permissions_33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA
    )

    fun permissions(): Array<String> {
        val p: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storge_permissions_33
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            storge_permissions1
        } else {
            storge_permissions
        }
        return p
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

        // Set the minimum date to tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
        datePickerDialog.datePicker.minDate = calendar.timeInMillis // Enable only future dates

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