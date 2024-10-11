package com.riggle.finza_finza.ui.finza.issueSuperTag.verify

import android.Manifest
import android.annotation.SuppressLint
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
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.CreateCustomerRew
import com.riggle.finza_finza.data.model.CustDetails2
import com.riggle.finza_finza.data.model.CustomerDetails
import com.riggle.finza_finza.data.model.CustomerRequest
import com.riggle.finza_finza.data.model.Document
import com.riggle.finza_finza.data.model.FasTagDetails
import com.riggle.finza_finza.data.model.GetVehicleMake
import com.riggle.finza_finza.data.model.GetVehicleModel
import com.riggle.finza_finza.data.model.RegDetails
import com.riggle.finza_finza.data.model.RegisterTagRequest
import com.riggle.finza_finza.data.model.RequestWallet
import com.riggle.finza_finza.data.model.SendOtpRequest
import com.riggle.finza_finza.data.model.ValidateOtpRequest
import com.riggle.finza_finza.data.model.VehicleMakersRequest
import com.riggle.finza_finza.data.model.VehicleModelRequest
import com.riggle.finza_finza.data.model.VerifyOtpRequest
import com.riggle.finza_finza.data.model.VrnDetails1
import com.riggle.finza_finza.databinding.ActivityVerifyTagBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.permission.PermissionHandler
import com.riggle.finza_finza.ui.base.permission.Permissions
import com.riggle.finza_finza.utils.AppUtils
import com.riggle.finza_finza.utils.RealPath.getFilePath
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showInfoToast
import com.riggle.finza_finza.utils.showSuccessToast
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
    private var img1 = ""
    private var img2 = ""
    private var img3 = ""
    private var img4 = ""
    private var img5 = ""
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
    private var isNationalPermit1 = "2"
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
    private var vehicleModelList = ArrayList<String>()
    val vehicleColors = arrayListOf(
        "Black", "White", "Silver", "Gray", "Blue", "Red", "Green", "Yellow", "Orange",
        "Brown", "Beige", "Purple", "Pink", "Gold", "Bronze", "Turquoise", "Maroon",
        "Teal", "Copper", "Champagne", "Emerald", "Lavender", "Burgundy", "Tan",
        "Charcoal", "Cream", "Platinum", "Pearl", "Sky Blue", "Aqua", "Rose Gold"
    )
    private val vehicleTagClassId = arrayListOf("SEDAN","SUV","TRUCK","MOTORCYCLE","VAN","COUPE","BUS")
    private val statesList = arrayListOf("AP", "AR", "AS", "BR", "CG", "GA", "GJ", "HR", "HP", "JH", "KA",
        "KL", "MP", "MH", "MN", "ML", "MZ", "NL", "OD", "PB", "RJ", "SK", "TN",
        "TS", "TR", "UP", "UK", "WB", "AN", "CH", "DN", "DD", "DL", "LD", "PY")
    private val vehicleDescriptorList = arrayListOf("DIESEL", "PETROL",)
    private val typeList = arrayListOf("LMV")
    private val vehicleClassIdList = arrayListOf("4")
    private val npciVehicleClassIdList = arrayListOf("4")

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, VerifyTagActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
        var FastTagNumber = ""
        var FastTagId = ""
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

        binding.cbNationalPermit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isNationalPermit1 = "1"
            } else {
                isNationalPermit1 = "2"
            }
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
                            vehicleNumber = binding.etvVehicleNumber.text.toString()
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
                            it.data.data.validateOtpResp.vrnDetails.vehicleManuf.let { it1 ->
                                    if (it1 != null) {
                                        vehicleManuf = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.model.let {it1 ->
                                    if (it1 != null) {
                                        model = it1
                                    }
                                }
                                it.data.data.validateOtpResp.custDetails.walletId.let { it1 ->
                                    if (it1 != null) {
                                        walletId1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.custDetails.name.let { it1 ->
                                    if (it1 != null) {
                                        userName = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleNo.let { it1 ->
                                    if (it1 != null) {
                                        //   vehicleNumber = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleColour.let { it1 ->
                                    if (it1 != null) {
                                        vehicleColour1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleDescriptor.let { it1 ->
                                    if (it1 != null) {
                                        vehicleDescriptor1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.isNationalPermit.let { it1 ->
                                    if (it1 != null) {
                                        isNationalPermit1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.permitExpiryDate.let { it1 ->
                                    if (it1 != null) {
                                        permitExpiryDate1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.stateOfRegistration.let { it1 ->
                                    if (it1 != null) {
                                        stateOfRegistration1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.tagVehicleClassID.let { it1 ->
                                    if (it1 != null) {
                                        tagVehicleClassID1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.npciVehicleClassID.let { it1 ->
                                    if (it1 != null) {
                                        npciVehicleClassID1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.type.let { it1 ->
                                    if (it1 != null) {
                                        type = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleType.let { it1 ->
                                    if (it1 != null) {
                                        vehicleType = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.engineNo.let { it1 ->
                                    if (it1 != null) {
                                        vehicleEngineNumber = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.tagCost.let { it1 ->
                                    if (it1 != null) {
                                        tagCost = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.securityDeposit.let { it1 ->
                                    if (it1 != null) {
                                        securityDeposit = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.rechargeAmount.let { it1 ->
                                    if (it1 != null) {
                                        rechargeAmount = it1
                                    }
                                }
                                createCustomer()
                            } else {
                                it.data.data.validateOtpResp.vrnDetails.vehicleManuf.let { it1 ->
                                    if (it1 != null) {
                                        vehicleManuf = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.model.let {it1 ->
                                    if (it1 != null) {
                                        model = it1
                                    }
                                }
                                it.data.data.validateOtpResp.custDetails.walletId.let { it1 ->
                                    if (it1 != null) {
                                        walletId1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.custDetails.name.let { it1 ->
                                    if (it1 != null) {
                                        userName = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleNo.let { it1 ->
                                    if (it1 != null) {
                                    //    vehicleNumber = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleColour.let { it1 ->
                                    if (it1 != null) {
                                        vehicleColour1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleDescriptor.let { it1 ->
                                    if (it1 != null) {
                                        vehicleDescriptor1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.isNationalPermit.let { it1 ->
                                    if (it1 != null) {
                                        isNationalPermit1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.permitExpiryDate.let { it1 ->
                                    if (it1 != null) {
                                        permitExpiryDate1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.stateOfRegistration.let { it1 ->
                                    if (it1 != null) {
                                        stateOfRegistration1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.tagVehicleClassID.let { it1 ->
                                    if (it1 != null) {
                                        tagVehicleClassID1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.npciVehicleClassID.let { it1 ->
                                    if (it1 != null) {
                                        npciVehicleClassID1 = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.type.let { it1 ->
                                    if (it1 != null) {
                                        type = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.vehicleType.let { it1 ->
                                    if (it1 != null) {
                                        vehicleType = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.engineNo.let { it1 ->
                                    if (it1 != null) {
                                        vehicleEngineNumber = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.tagCost.let { it1 ->
                                    if (it1 != null) {
                                        tagCost = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.securityDeposit.let { it1 ->
                                    if (it1 != null) {
                                        securityDeposit = it1
                                    }
                                }
                                it.data.data.validateOtpResp.vrnDetails.rechargeAmount.let { it1 ->
                                    if (it1 != null) {
                                        rechargeAmount = it1
                                    }
                                }
                                uploadDocument()
                            }
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.data?.message.toString())
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
                        if (it.data.data.vehicleMakerList.isNotEmpty()){
                            vehicleMakersList = it.data.data.vehicleMakerList as ArrayList<String>
                            //createCustomer()
                            initManufacturerSP(vehicleMakersList)
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

        viewModel.obrModelList.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.data.vehicleModelList.isNotEmpty()){
                            vehicleModelList = it.data.data.vehicleModelList as ArrayList<String>
                            initModelSP(vehicleModelList)
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

        viewModel.obrCreateBajajCustomer.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.custDetails.walletId != null){
                            walletId1 = it.data.custDetails.walletId
                        }
                        userName = it.data.custDetails.name
                        uploadDocument()
                    } else {
                        showErrorToast(it.message.toString())
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.data?.response?.msg.toString())
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
                        when (it.data.documentDetails.imageType) {
                            "RCFRONT" -> {
                                binding.ivRcFrontUploaded.visibility = View.VISIBLE
                                img1 = it.data.documentDetails.imageType
                            }
                            "RCBACK" -> {
                                binding.ivRcBackUploaded.visibility = View.VISIBLE
                                img2 = it.data.documentDetails.imageType
                            }
                            "VEHICLESIDE" -> {
                                binding.ivCarSdieUploaded.visibility = View.VISIBLE
                                img4 = it.data.documentDetails.imageType
                            }
                            "VEHICLEFRONT" -> {
                                binding.ivCarFrontUploaded.visibility = View.VISIBLE
                                img3 = it.data.documentDetails.imageType
                            }
                            "TAGAFFIX" -> {
                                binding.ivTagaFixUploaded.visibility = View.VISIBLE
                                img5 = it.data.documentDetails.imageType
                            }
                        }
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
                        if (it.data.response.msg.toString().contains("Invalid request parameters")){
                            showErrorToast(it.data.response.msg.toString())
                        }else{
                            it.data.response.msg.let { it1 -> showSuccessToast(it1) }
                            finish()
                        }
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

    @SuppressLint("NewApi")
    private fun registerTag() {
        binding.tvHeader.text = "Upload Document"
        binding.llRegisterTag.visibility = View.VISIBLE
        binding.llCreateCustomer.visibility = View.GONE
        binding.llVerifyNumber.visibility = View.GONE
        binding.llUploadDocument.visibility = View.GONE
        binding.llVerifyOtp.visibility = View.GONE

        val code = FastTagNumber
        val parts = code.split('-')
        val string1 = parts[0]
        val string2 = parts[1]
        val string3 = parts[2]
        binding.etvFastTagId1.setText(string1)
        binding.etvFastTagId2.setText(string2)
        binding.etvFastTagId3.setText(string3)

        binding.etv1.setText(userName)
        binding.etv2.setText(mobile1)
        binding.etv4.setText(vehicleNumber)
        binding.etv5.setText(vehicleChassisNumber)
        binding.etv6.setText(vehicleEngineNumber)
        if (vehicleManuf != null && vehicleManuf != ""){
            binding.etv8.setText(vehicleManuf)
            binding.etv8.visibility = View.VISIBLE
            binding.llManufacturer.visibility = View.GONE
        }else{
            val requestBody = VehicleMakersRequest(
                getVehicleMake  = GetVehicleMake(
                    requestId = requestId1,
                    sessionId = sessionId1,
                    channel = channel,
                    agentId = agentId,
                    reqDateTime = getCurrentDateFormatted(),
                    provider = provider,
                    inventory_id = FastTagId)
            )
            viewModel.getMakersList(
                sharedPrefManager.getToken().toString(), requestBody
            )
            binding.etv8.visibility = View.GONE
            binding.llManufacturer.visibility = View.VISIBLE
        }
        if (model != null && model != ""){
            binding.etv9.setText(model)
            binding.etv9.visibility = View.VISIBLE
            binding.llModel.visibility = View.GONE
        }else{
            binding.etv9.visibility = View.GONE
            binding.llModel.visibility = View.VISIBLE
        }
        binding.etv9.setText(model)

        binding.etv20.setText(rechargeAmount)
        binding.etv21.setText(securityDeposit)
        binding.etv22.setText(tagCost)

        if (vehicleColour1 == "" || vehicleColour1 == null){
            binding.tvVehicleColor.visibility = View.VISIBLE
            binding.llVehicleColor.visibility = View.VISIBLE
            initVehicleColorSP(vehicleColors)
        }else{
            binding.tvVehicleColor.visibility = View.GONE
            binding.llVehicleColor.visibility = View.GONE
        }

        if (vehicleType == "" || vehicleType == null){
            binding.tvVehicleType.visibility = View.VISIBLE
            binding.llVehicleType.visibility = View.VISIBLE
            initTagVehicleClassIdSP(vehicleTagClassId)
        }else{
            binding.tvVehicleType.visibility = View.GONE
            binding.llVehicleType.visibility = View.GONE
        }
        if (stateOfRegistration1 == "" || stateOfRegistration1 == null){
            binding.tvStateOfRegistration.visibility = View.VISIBLE
            binding.llStateOfRegistration.visibility = View.VISIBLE
            initStateOfRegistrationSP(statesList)
        }else{
            binding.tvStateOfRegistration.visibility = View.GONE
            binding.llStateOfRegistration.visibility = View.GONE
        }

        if (vehicleDescriptor1 == "" || vehicleDescriptor1 == null){
            binding.tvVehicleDiscriptor.visibility = View.VISIBLE
            binding.llVehicleDoscriptor.visibility = View.VISIBLE
            initVehicleDescriptorSP(vehicleDescriptorList)
        }else{
            binding.tvStateOfRegistration.visibility = View.GONE
            binding.llVehicleDoscriptor.visibility = View.GONE
        }

        if (type == "" || type == null){
            binding.tvType.visibility = View.VISIBLE
            binding.llType.visibility = View.VISIBLE
            initTypeSP(typeList)
        }else{
            binding.tvType.visibility = View.GONE
            binding.llType.visibility = View.GONE
        }

        if (tagVehicleClassID1 == "" || tagVehicleClassID1 == null){
            binding.tvVehicleClassId.visibility = View.VISIBLE
            binding.llVehicleClassID.visibility = View.VISIBLE
            initVehicleClassIdSP(vehicleClassIdList)
        }else{
            binding.tvVehicleClassId.visibility = View.GONE
            binding.llVehicleClassID.visibility = View.GONE
        }
        if (npciVehicleClassID1 == "" || npciVehicleClassID1 == null){
            binding.tvNPCIVehicleClassId.visibility = View.VISIBLE
            binding.llNPCIVehicleClassID.visibility = View.VISIBLE
            initNPCITagVehicleClassIdSP(npciVehicleClassIdList)
        }else{
            binding.tvNPCIVehicleClassId.visibility = View.GONE
            binding.llNPCIVehicleClassID.visibility = View.GONE
        }
        if (isNationalPermit1 == "" || isNationalPermit1 == null){
            binding.cbNationalPermit.visibility = View.VISIBLE
            binding.tvNationalPermit.visibility = View.VISIBLE
            binding.llPermitExpiry.visibility = View.VISIBLE
            isNationalPermit1 = "2"
        }else{
            binding.cbNationalPermit.visibility = View.GONE
            binding.tvNationalPermit.visibility = View.GONE
            binding.llPermitExpiry.visibility = View.GONE
        }
    }

    private fun initNPCITagVehicleClassIdSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spNpciVehicleClassId.adapter = adapter

        binding.spNpciVehicleClassId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                npciVehicleClassID1 = selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initVehicleClassIdSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spVehicleClassId.adapter = adapter

        binding.spVehicleClassId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                tagVehicleClassID1 = selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initTypeSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spType.adapter = adapter

        binding.spVehicleDiscriptor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                type = selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initVehicleDescriptorSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spVehicleDiscriptor.adapter = adapter

        binding.spVehicleDiscriptor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                vehicleDescriptor1 = selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initStateOfRegistrationSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spStates.adapter = adapter

        binding.spStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                stateOfRegistration1 = selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initTagVehicleClassIdSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spVehicleType.adapter = adapter

        binding.spVehicleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                vehicleType = selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initVehicleColorSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spVehicleColor.adapter = adapter

        binding.spVehicleColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                vehicleColour1 = selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initManufacturerSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spManufacturer.adapter = adapter

        binding.spManufacturer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NewApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                vehicleManuf = selectedItem as String
                val requestBody = VehicleModelRequest(
                    getVehicleModel  = GetVehicleModel(
                        requestId = requestId1,
                        sessionId = sessionId1,
                        channel = channel,
                        agentId = agentId,
                        reqDateTime = getCurrentDateFormatted(),
                        provider = provider,
                        vehicleMake = vehicleManuf,
                        inventory_id = FastTagId)
                )
                viewModel.getModelsList(
                    sharedPrefManager.getToken().toString(), requestBody
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initModelSP(manulist: ArrayList<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            manulist
        )
        binding.spModel.adapter = adapter

        binding.spModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                model = selectedItem as String
                showErrorToast(model.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
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
                            vehicleNo = vehicleNumber,
                            inventory_id  = FastTagId
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
                                provider = provider,
                                inventory_id = FastTagId
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
                    val code = FastTagNumber
                    val parts = code.split('-')
                    val string1 = parts[0]
                    val string2 = parts[1]
                    val string3 = parts[2]
                    val tagNumber1 = binding.etvFastTagId1.text.toString()
                    val tagNumber2 = binding.etvFastTagId2.text.toString()
                    val tagNumber3 = binding.etvFastTagId3.text.toString()

                    val engineNumberComplete = binding.etv6.text.toString()
                    val rechargeAmount = binding.etv20.text.toString()
                    val securityDeposit = binding.etv21.text.toString()
                    val tagCost = binding.etv22.text.toString()
                    val debitAmount = binding.etv23.text.toString()
                    userName = binding.etv1.text.toString()
                    vehicleEngineNumber = binding.etv6.text.toString()

                    if (userName == ""){
                        showErrorToast("Please enter user name")
                    }else if (vehicleEngineNumber == ""){
                        showErrorToast("Please enter complete engine number")
                    }
                    else if (tagNumber1 == "") {
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
                            serialNo = "$string1-$string2-$string3",
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
                            provider = provider,
                            inventory_id = FastTagId
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
                                ), provider = provider,
                                inventory_id = FastTagId
                            )
                        )
                        viewModel.createCustomer(sharedPrefManager.getToken().toString(), request)
                    }
                }

                R.id.llDocExpiry, R.id.ivCalender1 -> {
                    showDatePickerDialog1()
                }

                R.id.ivPermitExpiry -> {
                    showPermitExpiryDate()
                }

                /// upload documents
                R.id.tvSelectRcFront -> {
                    checkPermission(binding.tvSelectRcFront, "1")
                }

                R.id.tvUploadRcFront -> {
                    if (binding.ivRcFrontUploaded.visibility == View.VISIBLE){
                        showInfoToast("Already Uploaded.")
                    }else{
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
                                AppUtils.textToRequestBody(provider),
                                AppUtils.textToRequestBody(FastTagId)
                            )
                        }
                    }

                }

                R.id.tvSelectRcBack -> {
                    checkPermission(binding.tvSelectRcBack, "2")
                }

                R.id.tvUploadRcBack -> {
                    if (binding.ivRcBackUploaded.visibility == View.VISIBLE){
                        showInfoToast("Already Uploaded.")
                    }else{
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
                                AppUtils.textToRequestBody(provider),
                                AppUtils.textToRequestBody(FastTagId)

                            )
                        }
                    }
                }

                R.id.tvSelectCarSide -> {
                    checkPermission(binding.tvSelectCarSide, "4")
                }

                R.id.tvUploadCarSide -> {
                    if (binding.ivCarSdieUploaded.visibility == View.VISIBLE){
                        showInfoToast("Already Uploaded.")
                    }else{
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
                                AppUtils.textToRequestBody(provider),
                                AppUtils.textToRequestBody(FastTagId)

                            )
                        }
                    }

                }

                R.id.tvSelectCarFront -> {
                    checkPermission(binding.tvSelectCarFront, "3")
                }

                R.id.tvUploadCarFront -> {
                    if (binding.ivCarFrontUploaded.visibility == View.VISIBLE){
                        showInfoToast("Already Uploaded.")
                    }else{
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
                                AppUtils.textToRequestBody(provider),
                                AppUtils.textToRequestBody(FastTagId)

                            )
                        }
                    }

                }

                R.id.tvSelectTagaFix -> {
                    checkPermission(binding.tvSelectTagaFix, "5")
                }

                R.id.tvUploadTagaFix -> {
                    if (binding.ivTagaFixUploaded.visibility == View.VISIBLE){
                        showInfoToast("Already Uploaded.")
                    }else{
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
                                AppUtils.textToRequestBody(provider),
                                AppUtils.textToRequestBody(FastTagId)
                            )
                        }
                    }

                } // 608268-001-0646971

                R.id.tvContinueUpload -> {
                    if (img1 == "") {
                        showErrorToast("Please upload RC front")
                    } else if (img2 == "") {
                        showErrorToast("Please upload RC back")
                    } else if (img3 == "") {
                        showErrorToast("Please upload Car front")
                    } else if (img4 == "") {
                        showErrorToast("Please upload Car side")
                    } else if (img5 == "") {
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

    private fun showPermitExpiryDate() {
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
                binding.etvPermitExpiry.setText(sdf.format(selectedDate.time))
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