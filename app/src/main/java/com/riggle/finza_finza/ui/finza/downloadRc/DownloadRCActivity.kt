package com.riggle.finza_finza.ui.finza.downloadRc

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.RadioButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.RcDownloadedDataX
import com.riggle.finza_finza.databinding.ActivityDownloadRcactivityBinding
import com.riggle.finza_finza.databinding.HolderDownloadTransactionsBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.VerticalPagination
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showInfoToast
import com.riggle.finza_finza.utils.showSheet
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DownloadRCActivity : BaseActivity<ActivityDownloadRcactivityBinding>(),
    VerticalPagination.VerticalScrollListener {

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

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.all { it.value }
                if (granted) {
                    downloadPdf()
                } else {
                    showErrorToast("Storage permission is required!")
                }
            }

        // Initialize Storage Access Framework (SAF) launcher for Android 10+
        createFileLauncher =
            registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
                uri?.let {
                    downloadPdfToUri(uri)
                } ?: run {
                    showErrorToast("No folder selected!")
                }
            }



        viewModel.downloadList(sharedPrefManager.getToken().toString(), currentPage)
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

        initAdapter()
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
                        if (selectedText == "Download QR RC") {
                            viewModel.downloadPdf(
                                sharedPrefManager.getToken().toString(), vehicleNumber, 1, 2
                            )
                        } else {
                            viewModel.downloadPdf(
                                sharedPrefManager.getToken().toString(), vehicleNumber, 1, 1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.obrDownloadList.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (currentPage < it.data.data.last_page) {
                            verticalPagination.isLoading = false
                        }
                        if (it.data.data.current_page == 1) {
                            adapter.list = it.data.data.data
                        } else {
                            if (it.data.data.data.isNotEmpty()) {
                                adapter.addToList(it.data.data.data)
                            }
                        }
//                        it.data.data.let { it1 ->
//                            it1.data.let { it2 ->
//                                adapter.list = it2
//                            }
//                        }
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                }

                else -> {}
            }
        }

        viewModel.obrDownload.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    // showHideLoader(false)

                    if (it.data != null) {
                        pdfUrl = it.data.pdf_url
                        if (pdfUrl != null && pdfUrl != "") {
                            startDownloadProcess()
                        }else{
                            showErrorToast("pdf url not found")
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

    private var currentPage: Int = 1
    private lateinit var verticalPagination: VerticalPagination
    private lateinit var adapter: SimpleRecyclerViewAdapter<RcDownloadedDataX, HolderDownloadTransactionsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_download_transactions, BR.bean
        ) { v, m, pos ->

        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvHomeDrawer.layoutManager = layoutManager
        binding.rvHomeDrawer.setItemViewCacheSize(50)
        binding.rvHomeDrawer.adapter = adapter
        verticalPagination = VerticalPagination(layoutManager, 3)
        verticalPagination.setListener(this)
        binding.rvHomeDrawer.addOnScrollListener(verticalPagination)
        binding.rvHomeDrawer.adapter = adapter
    }

    private var pdfUrl = ""
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var createFileLauncher: ActivityResultLauncher<String>
    private fun startDownloadProcess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (Use SAF)
                createFileLauncher.launch(getCurrentDateTime()+".jpg")
            } else {
                // Android 9 and below (Use permissions and DownloadManager)
                checkPermissions()
            }
        }

        private fun checkPermissions() {
            val permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } else {
                emptyArray() // No permission needed for SAF
            }

            if (permissions.isNotEmpty()) {
                requestPermissionLauncher.launch(permissions)
            } else {
                downloadPdf()
            }
        }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }


    private fun downloadPdf() {
            val request = DownloadManager.Request(Uri.parse(pdfUrl))
                .setTitle("Downloading Document")
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getCurrentDateTime() + ".jpg")

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            showToast("Download started...")
        }

        private fun downloadPdfToUri(uri: Uri) {
            Thread {
                try {
                    val url = URL(pdfUrl)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    connection.connect()

                    val inputStream: InputStream = connection.inputStream
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                        outputStream.flush()
                    }

                    runOnUiThread {
                        showSuccessToast("Downloaded successfully!")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        showErrorToast("Failed to download")
                    }
                }
            }.start()
            hideLoading()
        }

    override fun onLoadMore() {
        currentPage++
        viewModel.downloadList(sharedPrefManager.getToken().toString(), currentPage)
    }
}