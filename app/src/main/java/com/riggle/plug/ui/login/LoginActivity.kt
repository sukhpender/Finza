package com.riggle.plug.ui.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityLoginBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.permission.PermissionHandler
import com.riggle.plug.ui.base.permission.Permissions
import com.riggle.plug.ui.finza.FinzaHomeActivity
import com.riggle.plug.ui.finza.projectList.ProjectListActivity
import com.riggle.plug.ui.forgotPassword.ForgotPasswordActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showInfoToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginActivityVM by viewModels()
    private val CAMERA_PERMISSION_CODE = 100

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)

        initView()
        initOnClick()
        observers()
    }

    private fun initView() {
       // checkPermission( binding.root,"Profile")
    }

    private var storge_permissions = arrayOf(
        Manifest.permission.CAMERA
    )

    private var storge_permissions1 = arrayOf(
        Manifest.permission.CAMERA
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storge_permissions_33 = arrayOf(
        Manifest.permission.CAMERA
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
        Permissions.check(this, permissions(), 0, null, object : PermissionHandler() {
            override fun onGranted() {
                when (from) {
                    "Profile" -> {

                    }
                }
            }

            override fun onDenied(
                context: Context?, deniedPermissions: ArrayList<String>?
            ) {
                super.onDenied(context, deniedPermissions)
                showInfoToast("Permission required to continue")
            }
        })
    }

    private fun observers() {
        viewModel.obrLogin.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        it.data.message.let { it1 -> showSuccessToast(it1) }
                        sharedPrefManager.saveUser(it.data.data)
                        sharedPrefManager.saveToken("Bearer ${it.data.data.auth_token}")
                        Log.e("savedData",sharedPrefManager.getToken().toString())
                        if ((it.data.data.role_id != 5)) {
                            startActivity(ProjectListActivity.newIntent(this))
                            finish()
                        }else{
                            startActivity(FinzaHomeActivity.newIntent(this))
                            finish()
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
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.tvResetPass -> {
                    startActivity(ForgotPasswordActivity.newIntent(this))
                }

                R.id.tvLogin -> {
                    val email = binding.etPhone.text.toString()
                    val pass = binding.etPassword.text.toString()
                    if (email == "") {
                        showErrorToast("Please enter phone number")
                    } else if (pass == "") {
                        showErrorToast("Please enter password")
                    } else if (email.length != 10) {
                        showErrorToast("Please enter a valid phone number")
                    } else if (pass.length < 6) {
                        showErrorToast("Password must be greater then 6 digits")
                    } else {
                        if (isCameraPermissionGranted()) {
                            // Permission is granted
                            viewModel.finzaLogin(email, pass)
                         //   showSuccessToast("Camera permission is granted"
                        } else {
                            // Permission is not granted
                            //requestCameraPermission()
                            checkPermission( binding.root,"Profile")
                        }
                    }
                }
            }
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
//            // Show an explanation to the user if needed
//            showInfoToast("Camera permission is needed to use the camera.")
//        }
        // Request the permission
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                showSuccessToast("Camera permission granted")
            } else {
                // Permission denied
                showInfoToast("Camera permission denied")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}