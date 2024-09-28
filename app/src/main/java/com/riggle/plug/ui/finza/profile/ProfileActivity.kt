package com.riggle.plug.ui.finza.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityProfileBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.permission.PermissionHandler
import com.riggle.plug.ui.base.permission.Permissions
import com.riggle.plug.ui.finza.resetPass.ResetPassActivity
import com.riggle.plug.utils.AppUtils
import com.riggle.plug.utils.RealPath.getFilePath
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showInfoToast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    private val viewModel: ProfileActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_profile
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)

        sharedPrefManager.getToken()?.let { viewModel.finzaGetProfile(it) }
        initView()
        initOnClick()
    }

    private fun initView() {

        binding.tv4.text = "("+sharedPrefManager.getCurrentUser()?.designation+")"

        viewModel.obrProfile.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.bean = it.data.data
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

        viewModel.obrUpdateProfile.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.bean = it.data.data
                        Log.e("Ashu--------",it.data.data.toString())
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

                R.id.tvResetPass -> {
                    startActivity(ResetPassActivity.newIntent(this))
                }

                R.id.llCamera -> {
                    checkPermission(binding.llCamera)
                }

                R.id.tvUpdate -> {
                    val etvFName = binding.etvFName.text.toString()
                    val etvLName = binding.etvLastName.text.toString()
                    if (etvFName == "") {
                        showErrorToast("Please enter first name")
                    } else if (etvLName == "") {
                        showErrorToast("Please enter last name")
                    } else {
                        if (aadhaarCardFile == null) {
                            viewModel.updateProfileWithoutIImage(
                                sharedPrefManager.getToken().toString(),
                                etvFName,
                                etvLName,
                            )
                        } else {
                            val requestFile =
                                aadhaarCardFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                                "profile_image", aadhaarCardFile?.name, requestFile!!
                            )
                            viewModel.updateProfile(
                                sharedPrefManager.getToken().toString(),
                                AppUtils.textToRequestBody(etvFName),
                                AppUtils.textToRequestBody(etvLName),
                                body
                            )
                        }
                    }
                }
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


    private fun checkPermission(v: View) {
        Permissions.check(this, permissions(), 0, null, object : PermissionHandler() {
            override fun onGranted() {
                getAadhaarCardMediaOption(v)
            }

            override fun onDenied(
                context: Context?, deniedPermissions: ArrayList<String>?
            ) {
                super.onDenied(context, deniedPermissions)
                showInfoToast("Accept Storage permission to continue")
            }
        })
    }

    private fun getAadhaarCardMediaOption(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(this).cropSquare().cameraOnly().createIntent { intent ->
                        startForAadhaarCardImageResult.launch(intent)
                    }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(this).cropSquare().galleryOnly().createIntent { intent ->
                        startForAadhaarCardImageResult.launch(intent)
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

    private var aadhaarCardFile: File? = null
    private val startForAadhaarCardImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                aadhaarCardFile = File(fileUri.getFilePath(this))
                binding.iv111.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showErrorToast(ImagePicker.getError(data))
            } else {
                showErrorToast("Task Cancelled")
            }
        }
}