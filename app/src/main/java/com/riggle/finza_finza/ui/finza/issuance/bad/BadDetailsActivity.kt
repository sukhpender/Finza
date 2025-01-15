package com.riggle.finza_finza.ui.finza.issuance.bad

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.github.dhaval2404.imagepicker.ImagePicker
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityBadDetailsBinding
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

@AndroidEntryPoint
class BadDetailsActivity : BaseActivity<ActivityBadDetailsBinding>() {

    private val viewModel: BadDetailsActivityVM by viewModels()
    private var issueType = ""
    private var auditId = ""

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, BadDetailsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_bad_details
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
        initObserver()
    }

    private fun initObserver() {
        viewModel.obrUploadDocument.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        showSuccessToast(it.data.message.toString())
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

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }

                R.id.tvSelectRcFront1 -> {
                    checkPermission(binding.tvSelectRcFront1, "1")
                }

                R.id.tvSelectCarFront -> {
                    checkPermission(binding.tvSelectCarFront, "3")
                }

                R.id.tvCarSide -> {
                    checkPermission(binding.tvCarSide, "4")
                }
            }
        }
    }

    private fun initView() {
        auditId = sharedPrefManager.getAuditId().toString()
        issueType = sharedPrefManager.getTypeId().toString()
        when (issueType) {
            "Wrong RC Uploaded" -> {
                binding.cvRcFront.visibility = View.VISIBLE
                binding.cvCarFrontImage.visibility = View.GONE
                binding.cvCarSideImage.visibility = View.GONE
            }

            "Wrong Car Image Uploaded" -> {
                binding.cvRcFront.visibility = View.GONE
                binding.cvCarFrontImage.visibility = View.VISIBLE
                binding.cvCarSideImage.visibility = View.VISIBLE
            }

            "Wrong RC + Wrong Car Image Uploaded" -> {
                binding.cvRcFront.visibility = View.VISIBLE
                binding.cvCarFrontImage.visibility = View.VISIBLE
                binding.cvCarSideImage.visibility = View.VISIBLE
            }
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

    private fun checkPermission(v: View, forWhich: String) {
        Permissions.check(this, permissions(), 0, null, object : PermissionHandler() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onGranted() {
                when (forWhich) {
                    "1" -> {
                        getRCFront(v)
                    }

                    "2" -> {
                        // getRCBack(v)
                    }

                    "3" -> {
                        ImagePicker.with(this@BadDetailsActivity).cameraOnly()
                            .createIntent { intent ->
                                startForCarFrontImageResult.launch(intent)
                            }
                        //getCarFront(v)
                    }

                    "4" -> {
                        ImagePicker.with(this@BadDetailsActivity).cameraOnly()
                            .createIntent { intent ->
                                startForCarSideImageResult.launch(intent)
                            }
                        // getCarSide(v)
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

    private var carSideFile: File? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val startForCarSideImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                carSideFile = File(fileUri.getFilePath(this))
                binding.ivCarSide.setImageURI(fileUri)
                // car side image update code ***/
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
                        AppUtils.textToRequestBody(auditId),
                        body,
                        AppUtils.textToRequestBody("VEHICLE ASIDE"),
                    )
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

    private var carFrontFile: File? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val startForCarFrontImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                carFrontFile = File(fileUri.getFilePath(this))
                binding.ivCarFront.setImageURI(fileUri)
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
                        AppUtils.textToRequestBody(auditId),
                        body,
                        AppUtils.textToRequestBody("VEHICLE FRONT"),
                    )
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRCFront(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(this).cameraOnly().createIntent { intent ->
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

    @RequiresApi(Build.VERSION_CODES.O)
    private val startForRCFrontImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                rcFrontFile = File(fileUri.getFilePath(this))
                binding.ivRCFront.setImageURI(fileUri)
                // updated code from here ***/
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
                        AppUtils.textToRequestBody(auditId),
                        body,
                        AppUtils.textToRequestBody("RC"),
                    )
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }

}