package com.riggle.plug.ui.more.myAccount

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.riggle.plug.R
import com.riggle.plug.data.model.UserProfileResponseModel
import com.riggle.plug.databinding.FragmentEditOwnInfoBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.permission.PermissionHandler
import com.riggle.plug.ui.base.permission.Permissions
import com.riggle.plug.ui.home.HomeActivity
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

@AndroidEntryPoint
class EditOwnInfoFragment : BaseFragment<FragmentEditOwnInfoBinding>() {

    private val viewModel: EditOwnInfoFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var userDetails: UserProfileResponseModel? = null
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_edit_own_info
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        if (userDetails != null) {
            binding.bean = userDetails
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivEditOwnInfoBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.llUserCamera -> {
                    checkPermission(binding.llUserCamera, "Profile")
                }

                R.id.tvUploadPanCard -> {
                    checkPermission(binding.tvUploadPanCard, "Pan")
                }

                R.id.tvUploadAadharCard -> {
                    checkPermission(binding.tvUploadAadharCard, "Aadhaar")
                }

                R.id.tvAddAnotherOwner -> {
                    /*val coOwnerName = binding.etvCoOwner.text.toString()
                    val coOwnerMobile = binding.etvCoOwnerPhone.text.toString()
                    val coOwnerLastName = binding.etvCoOwnerLastName.text.toString()
                    if (coOwnerName == "") {
                        showInfoToast("Please enter Co-owner first name")
                    } else if (coOwnerLastName == "") {
                        showInfoToast("Please enter Co-owner last name")
                    } else if (coOwnerMobile == "") {
                        showInfoToast("Please enter Co-owner phone number")
                    } else {
                        addCoOwner(coOwnerName, coOwnerMobile, coOwnerLastName)
                    }*/
                }

                R.id.tvSaveAccount -> {
                    val fName = binding.etvName.text.toString()
                    val lName = binding.etvLastName.text.toString()
                    val email = binding.etvEmail.text.toString()

                    if (fName == "") {
                        showToast("Please enter first name")
                    } else if (lName == "") {
                        showToast("Please enter last name")
                    } else if (email == "") {
                        showToast("Please enter email")
                    } else {

                        if (panCardFile != null) {
                            val requestFile =
                                panCardFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                                "pan_card", panCardFile?.name, requestFile!!
                            )
                            viewModel.updatePanUser(
                                sharedPrefManager.getSessionId()!!,
                                sharedPrefManager.getCurrentUser()?.user?.company?.id.toString(),
                                body
                            )
                        }
                        if (aadhaarCardFile != null) {
                            val requestFile =
                                aadhaarCardFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                                "aadhaar_card", aadhaarCardFile?.name, requestFile!!
                            )
                        }

                        /* viewModel.updateUser(
                             sharedPrefManager.getSessionId()!!,
                             sharedPrefManager.getCurrentUser()?.user?.id.toString(),
                             AppUtils.textToRequestBody(fName),
                             AppUtils.textToRequestBody(lName),
                             AppUtils.textToRequestBody(email),
                             hm)*/
                        if (photoFile != null) {
                            val requestFile =
                                photoFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                            val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                                "image", photoFile?.name, requestFile!!
                            )

                            viewModel.updateUser(
                                sharedPrefManager.getSessionId()!!,
                                sharedPrefManager.getCurrentUser()?.user?.id.toString(),
                                AppUtils.textToRequestBody(fName),
                                AppUtils.textToRequestBody(lName),
                                AppUtils.textToRequestBody(email),
                                body
                            )
                        } else {
                            viewModel.updateUserWithOutImage(
                                sharedPrefManager.getSessionId()!!,
                                sharedPrefManager.getCurrentUser()?.user?.id.toString(),
                                AppUtils.textToRequestBody(fName),
                                AppUtils.textToRequestBody(lName),
                                AppUtils.textToRequestBody(email)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.obrAddCoOwner.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    showSuccessToast("Added successfully")
                    binding.etvCoOwner.setText("")
                    binding.etvCoOwnerPhone.setText("")
                    binding.etvCoOwnerLastName.setText("")
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrUpdateUser.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    binding.bean = it.data
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    // showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrUpdatePan.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    binding.bean = it.data
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    // showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    private fun addCoOwner(name: String, mobile: String, lastName: String) {
        val query = HashMap<String, String>()
        query["first_name"] = name
        query["last_name"] = lastName
        query["mobile"] = mobile
        query["role"] = "company_admin"
        query["designation"] = "co-owner"
        query["company"] = sharedPrefManager.getCurrentUser()?.user?.company?.id.toString()

        viewModel.addCoOwner(sharedPrefManager.getSessionId()!!, query)
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

    private fun checkPermission(v: View, from: String) {
        Permissions.check(requireContext(), permissions(), 0, null, object : PermissionHandler() {
            override fun onGranted() {
                when (from) {
                    "Profile" -> {
                        getMediaOption(v)
                    }

                    "Pan" -> {
                        getPanCardMediaOption(v)
                    }

                    "Aadhaar" -> {
                        getAadhaarCardMediaOption(v)
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

    private fun getMediaOption(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(homeActivity).cropSquare().cameraOnly()
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(homeActivity).cropSquare().galleryOnly()
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
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

    private var photoFile: File? = null
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                photoFile = File(fileUri.getFilePath(requireContext()))
                binding.ivUserProfile.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getPanCardMediaOption(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(homeActivity).cropSquare().cameraOnly()
                        .createIntent { intent ->
                            startForPanCardImageResult.launch(intent)
                        }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(homeActivity).cropSquare().galleryOnly()
                        .createIntent { intent ->
                            startForPanCardImageResult.launch(intent)
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

    private var panCardFile: File? = null
    private val startForPanCardImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                panCardFile = File(fileUri.getFilePath(requireContext()))
                binding.ivPanCard.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getAadhaarCardMediaOption(v: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), v)
        popupMenu.menu.add("Select From Camera")
        popupMenu.menu.add("Select From Gallery")
        popupMenu.menu.add("Cancel")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.title) {
                "Select From Camera" -> {
                    ImagePicker.with(homeActivity).cropSquare().cameraOnly()
                        .createIntent { intent ->
                            startForAadhaarCardImageResult.launch(intent)
                        }
                }

                "Select From Gallery" -> {
                    ImagePicker.with(homeActivity).cropSquare().galleryOnly()
                        .createIntent { intent ->
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
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                aadhaarCardFile = File(fileUri.getFilePath(requireContext()))
                binding.ivAadharCard.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }/*    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 201) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }*/
}
