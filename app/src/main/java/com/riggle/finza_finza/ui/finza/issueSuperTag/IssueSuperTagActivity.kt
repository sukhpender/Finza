package com.riggle.finza_finza.ui.finza.issueSuperTag

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityIssueSuperTagBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.issueSuperTag.verify.VerifyTagActivity
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.barcode.BarcodeBoxView
import com.riggle.finza_finza.utils.barcode.QrCodeAnalyzer
import com.riggle.finza_finza.utils.event.SingleLiveEvent
import com.riggle.finza_finza.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class IssueSuperTagActivity : BaseActivity<ActivityIssueSuperTagBinding>() {

    private val viewModel: IssueSuperTagActivityVM by viewModels()
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeBoxView: BarcodeBoxView

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, IssueSuperTagActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

        var scannedResult = SingleLiveEvent<String>()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_issue_super_tag
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        initView()
        initOnClick()

        scannedResult.observe(this) {
            Log.e("-------------------",it)
            if (it.toString() != "") {
              //  cameraExecutor.shutdown()
                val code = it
                val parts = code.split('-')
                val string1 = parts[0]
                val string2 = parts[1]
                val string3 = parts[2]
                binding.etvFastTagId1.setText(string1)
                binding.etvFastTagId2.setText(string2)
                binding.etvFastTagId3.setText(string3)
            } else {
                showErrorToast("Scan Failure")
                finish()
            }
        }
    }

    private fun initView() {
        barcodeBoxView = BarcodeBoxView(this)
        startCamera()

        viewModel.obrCheckTagAvailable.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        val tagNumber1 = binding.etvFastTagId1.text.toString()
                        val tagNumber2 = binding.etvFastTagId2.text.toString()
                        val tagNumber3 = binding.etvFastTagId3.text.toString()
                        val finalTN = "$tagNumber1-$tagNumber2-$tagNumber3"
                        VerifyTagActivity.FastTagNumber = finalTN
                        VerifyTagActivity.FastTagId = it.data.data.id.toString()
                        startActivity(VerifyTagActivity.newIntent(this))
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

                R.id.tvSubmit -> {
                    val tagNumber1 = binding.etvFastTagId1.text.toString()
                    val tagNumber2 = binding.etvFastTagId2.text.toString()
                    val tagNumber3 = binding.etvFastTagId3.text.toString()
                    if (tagNumber1 == ""){
                        showErrorToast("Please enter complete FASTag number")
                    }else if(tagNumber1.length != 6){
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber2 == ""){
                        showErrorToast("Please enter complete FASTag number")
                    }else if (tagNumber2.length != 3){
                        showErrorToast("Please enter complete FASTag number")
                    } else if (tagNumber3 == ""){
                        showErrorToast("Please enter complete FASTag number")
                    }else if (tagNumber3.length != 7){
                        showErrorToast("Please enter complete FASTag number")
                    } else{
                        val final = "$tagNumber1-$tagNumber2-$tagNumber3"
                        viewModel.checkAvailability(sharedPrefManager.getToken().toString(),final)

                    }
                }
            }
        }
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
                this,
                Manifest.permission.CAMERA
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
}