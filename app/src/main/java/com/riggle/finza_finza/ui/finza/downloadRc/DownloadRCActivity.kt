package com.riggle.finza_finza.ui.finza.downloadRc

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityDownloadRcactivityBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showInfoToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

@AndroidEntryPoint
class DownloadRCActivity : BaseActivity<ActivityDownloadRcactivityBinding>() {

    private var selectedText = ""
    private val viewModel: DownloadRCActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, DownloadRCActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_download_rcactivity
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView() {
        initView()
        initOnClick()
        initObservers()
    }

    private fun initView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)

        sharedPrefManager.getCurrentUser().let {
         //   binding.tv3.text = it?.full_name
        }

        sharedPrefManager.getToken()?.let {
           // viewModel.getWallet(it)
        }

        binding.rb.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedText = selectedRadioButton.text.toString()
            Log.d("Selected RadioButton", selectedText)
        }

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

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }

                R.id.tvDownload -> {
                    val vehicleNumber = binding.etvVehicleNumber.text.toString()
                    if (vehicleNumber == "") {
                        showErrorToast("Please enter vehicle number.")
                    } else if (selectedText == "") {
                        showErrorToast("Please select download type")
                    } else {  // 1 = Smart Card , 2 = RC Copy
                        if (selectedText == "Download MParivahan") {
                            viewModel.downloadPdf(
                                sharedPrefManager.getToken().toString(),
                                vehicleNumber,
                                1,
                                1
                            )
                        } else {
                            viewModel.downloadPdf(
                                sharedPrefManager.getToken().toString(),
                                vehicleNumber,
                                1,
                                2
                            )
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initObservers() {
        viewModel.obrDownload.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                   // showHideLoader(false)
                    if (it.data != null) {
                        pdfUrl = it.data.pdf_url
                        if (checkPermission()) {
                            downloadPDF(it.data.pdf_url)
                        } else {
                            requestPermission()
                        }
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.data?.error.toString())
                }

                else -> {}
            }
        }

        viewModel.obrWallet.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                      showHideLoader(false)
                    // showSuccessToast(it.data?.message.toString())
                    if (it.data != null) {
                    //    binding.tv5.text = "₹ ${it.data.wallet}"
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

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            // Show explanation
            showInfoToast("Storage permission is required to download the PDF")
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101
        )
    }

    var pdfUrl = ""
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPDF(pdfUrl)
            } else {
                showErrorToast("Permission denied")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun downloadPDF(url: String) {
        Thread {
            try {
                // Create OkHttpClient instance
                val client = OkHttpClient()

                // Create request to fetch PDF
                val request = Request.Builder().url(url).build()

                // Execute request and get response
                val response = client.newCall(request).execute()
                val inputStream: InputStream = response.body?.byteStream() ?: return@Thread

                // Get the ContentResolver and prepare ContentValues
                val contentResolver: ContentResolver = contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "downloaded_sample.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/") // Path to Downloads folder
                }

                // Insert into MediaStore (Downloads folder)
                val contentUri: Uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
                val uri: Uri? = contentResolver.insert(contentUri, contentValues)

                if (uri != null) {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(uri)

                    outputStream?.use { outputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } != -1) {
                            outputStream.write(buffer, 0, length)
                        }

                        // Notify user that the download is complete
                        runOnUiThread {
                            showHideLoader(false)
                            showSuccessToast("PDF downloaded to Downloads folder")
                            finish()
                        }
                    }
                } else {
                    runOnUiThread {
                        showHideLoader(false)
                        showErrorToast("Failed to create file in Downloads folder")
                    }
                }

                inputStream.close()
            } catch (e: Exception) {
                runOnUiThread {
                    showHideLoader(false)
                    showErrorToast("Failed to download PDF")
                }
                e.printStackTrace()
            }
        }.start()
    }

}