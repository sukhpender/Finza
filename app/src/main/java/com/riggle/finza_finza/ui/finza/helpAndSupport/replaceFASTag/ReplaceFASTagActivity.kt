package com.riggle.finza_finza.ui.finza.helpAndSupport.replaceFASTag

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.SendOtpRequest
import com.riggle.finza_finza.data.model.TagReplaceReq
import com.riggle.finza_finza.data.model.TagReplaceRequest
import com.riggle.finza_finza.data.model.ValidateOtpRequest
import com.riggle.finza_finza.data.model.ValidateOtpResp
import com.riggle.finza_finza.data.model.VerifyOtpRequest
import com.riggle.finza_finza.databinding.ActivityReplaceFastagBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.issueSuperTag.IssueSuperTagActivity.Companion.scannedResult
import com.riggle.finza_finza.ui.finza.issueSuperTag.verify.VerifyTagActivity.Companion.FastTagId
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.barcode.BarcodeBoxView
import com.riggle.finza_finza.utils.barcode.QrCodeAnalyzer
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

@AndroidEntryPoint
class ReplaceFASTagActivity : BaseActivity<ActivityReplaceFastagBinding>() {

    private val viewModel: ReplaceFASTagActivityVM by viewModels()
    private var isChassi = 0
    private var provider = "bajaj"
    private var reqType = "REP"
    private var serialNumber = ""
    private var resend = 0
    private lateinit var verifyOtpResponseModel: ValidateOtpResp
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeBoxView: BarcodeBoxView

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ReplaceFASTagActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_replace_fastag
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView() {
        cameraExecutor = Executors.newSingleThreadExecutor()

        scannedResult.observe(this) {
            Log.e("-------------------", it)
            if (it.toString() != "") {
                //  cameraExecutor.shutdown()
                val code = it
                if (code.length == 16 || code.length == 18) {
                    var firstPart = ""
                    var secondPart = ""
                    var thirdPart = ""
                    if (code.contains("-")) {
                        val cleanedCode = code.replace("-", "")
                        firstPart = cleanedCode.substring(0, 6)
                        secondPart = cleanedCode.substring(6, 9)
                        thirdPart = cleanedCode.substring(9, 16)
                    } else {
                        firstPart = code.substring(0, 6)
                        secondPart = code.substring(6, 9)
                        thirdPart = code.substring(9, 16)
                    }
                    binding.etvFastTagId1.setText(firstPart)
                    binding.etvFastTagId2.setText(secondPart)
                    binding.etvFastTagId3.setText(thirdPart)

                    serialNumber = "$firstPart-$secondPart-$thirdPart"
                } else {
                    showErrorToast("Barcode is not of expected 16 digits")
                }
//                val parts = code.split('-')
//                val string1 = parts[0]
//                val string2 = parts[1]
//                val string3 = parts[2]
//                binding.etvFastTagId1.setText(string1)
//                binding.etvFastTagId2.setText(string2)
//                binding.etvFastTagId3.setText(string3)

            } else {
                showErrorToast("Something went wrong! Please enter barcode manually.")
                // finish()
            }
        }
        initView()
        initOnClick()
        observers()
    }

    private fun initView() {

        barcodeBoxView = BarcodeBoxView(this)
        startCamera()

        binding.llVerifyNumber.visibility = View.VISIBLE
        binding.llVerifyOtp.visibility = View.GONE
        binding.llReplacement.visibility = View.GONE
        binding.otpView.setOtpCompletionListener {
            enteredOtp = it
        }

        binding.etvFastTagId1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 6) {
                    binding.etvFastTagId2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etvFastTagId2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 3) {
                    binding.etvFastTagId3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etvFastTagId3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 7) {
                    val tagNumber1 = binding.etvFastTagId1.text.toString()
                    val tagNumber2 = binding.etvFastTagId2.text.toString()
                    val tagNumber3 = binding.etvFastTagId3.text.toString()
                    if (tagNumber1 == "") {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber1.length != 6) {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber2 == "") {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber2.length != 3) {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber3 == "") {
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber3.length != 7) {
                        showErrorToast("Please enter complete FASTag number")
                    } else {
                        serialNumber = "$tagNumber1-$tagNumber2-$tagNumber3"
                    }
                }
            }
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
    }

    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, requiredPermissions, 0)
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }

    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted: start the preview
            startCamera()
        } else {
            // Permission denied
            MaterialAlertDialogBuilder(this).setTitle("Permission required")
                .setMessage("This application needs to access the camera to process barcodes")
                .setPositiveButton("Ok") { _, _ ->
                    // Keep asking for permission until granted
                    checkCameraPermission()
                }.setCancelable(false).create().apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            // Image analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                    it.setAnalyzer(
                        cameraExecutor, QrCodeAnalyzer(
                            this,
                            barcodeBoxView,
                            binding.previewView.width.toFloat(),
                            binding.previewView.height.toFloat()
                        )
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private var enteredOtp = ""
    private var requestId1 = ""
    private var sessionId1 = ""
    private var channel = ""
    private var agentId = ""
    private var mobile1 = ""
    private var vehicleChassisNumber = ""
    private var vehicleEngineNumber = ""
    private var vehicleNumber = ""
    private var vehicleManuf = ""
    private var vehicleColour1 = ""
    private var vehicleDescriptor1 = ""
    private var isNationalPermit1 = "2"
    private var permitExpiryDate1 = ""
    private var stateOfRegistration1 = ""
    private var tagVehicleClassID1 = ""
    private var model = ""
    private var walletId1 = ""
    private var userName = ""
    private var npciVehicleClassID1 = ""
    private var vehicleType = ""
    private var type = ""
    private var tagCost = ""
    private var rechargeAmount = ""
    private var securityDeposit = ""
    private fun observers() {

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
                        replacement()
                        Log.e("VerifyOtpReplace", it.data.data.toString())

                        verifyOtpResponseModel = it.data.data.validateOtpResp
                        it.data.data.validateOtpResp.custDetails.mobileNo.let { it1 ->
                            if (it1 != null) {
                                binding.etv4.setText(it1)
                            }
                        }

                        it.data.data.validateOtpResp.custDetails.walletId.let { it1 ->
                            if (it1 != null) {
                                binding.etv5.setText(it1)
                            }
                        }

                        it.data.data.validateOtpResp.vrnDetails.vehicleNo.let { it1 ->
                            if (it1 != null) {
                                binding.etv6.setText(it1)
                            }
                        }

                        it.data.data.validateOtpResp.vrnDetails.chassisNo.let { it1 ->
                            if (it1 != null) {
                                binding.etv8.setText(it1)
                            }
                        }

                        it.data.data.validateOtpResp.vrnDetails.engineNo.let { it1 ->
                            if (it1 != null) {
                                binding.etv9.setText(it1)
                            }
                        }

                        it.data.data.validateOtpResp.vrnDetails.vehicleDescriptor.let { it1 ->
                            if (it1 != null) {
                                binding.etv99.setText(it1)
                            }
                        }
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    //   showErrorToast(it.data?.data?.response?.msg.toString())
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrTagReplacement.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        it.data.message.let { it1 -> showSuccessToast(it1) }
                        Log.e("TAG------REPLACEMENT", it.data.data.toString())
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    //   showErrorToast(it.data?.data?.response?.msg.toString())
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
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
                    var vehicleNumber = binding.etvVehicleNumber.text.toString()
                    val isChassis = binding.cbChassisNumber.isChecked
                    val chassisNumber = binding.etvChassisNumber.text.toString()
                    val engineNumber = binding.etvEngineNumber.text.toString()

                    if (isChassis) {
                        isChassi = 1
                        vehicleNumber = "Test"
                    } else {
                        isChassi = 0
                        vehicleNumber = binding.etvVehicleNumber.text.toString()
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
                            inventory_id = FastTagId
                        )
                        Log.e("SendOtpReplace--->>>", requestBody.toString())
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

                R.id.tvSendRequest -> {

                    if (::verifyOtpResponseModel.isInitialized) {
                        val request = TagReplaceReq(
                            binding.etv4.text.toString(),
                            binding.etv5.text.toString(),
                            binding.etv6.text.toString(),
                            "",
                            "",
                            getCurrentDateFormatted(),
                            "100",
                            verifyOtpResponseModel.requestId,
                            sessionId1,
                            serialNumber,
                            "1",
                            binding.etv8.text.toString(),
                            binding.etv9.text.toString(),
                            verifyOtpResponseModel.vrnDetails.isNationalPermit.toString(),
                            verifyOtpResponseModel.vrnDetails.permitExpiryDate.toString(),
                            verifyOtpResponseModel.vrnDetails.stateOfRegistration.toString(),
                            binding.etv99.text.toString(),
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                        val req1 = TagReplaceRequest(request)
                        viewModel.tagReplacement(sharedPrefManager.getToken().toString(), req1)
                    } else {
                        showErrorToast("Something went wrong")
                    }
                }
            }
        }
    }

    private fun verifyOtp() {
        binding.tvHeader.text = "Verify Otp"
        binding.llVerifyNumber.visibility = View.GONE
        binding.llReplacement.visibility = View.GONE
        binding.llVerifyOtp.visibility = View.VISIBLE
    }

    private fun replacement() {
        binding.tvHeader.text = "Replacement Details"
        binding.llVerifyNumber.visibility = View.GONE
        binding.llReplacement.visibility = View.VISIBLE
        binding.llVerifyOtp.visibility = View.GONE
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