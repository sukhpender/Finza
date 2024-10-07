package com.riggle.finza_finza.ui.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.riggle.finza_finza.App
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.local.SharedPrefManager
import com.riggle.finza_finza.data.network.ErrorCodes
import com.riggle.finza_finza.data.network.NetworkError
import com.riggle.finza_finza.ui.base.connectivity.ConnectivityProvider
import com.riggle.finza_finza.ui.login.LoginActivity
import com.riggle.finza_finza.utils.AlertManager
import com.riggle.finza_finza.utils.LoadingDialog
import com.riggle.finza_finza.utils.event.NoInternetSheet
import com.riggle.finza_finza.utils.hideKeyboard
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity(),
    ConnectivityProvider.ConnectivityStateListener {

    lateinit var progressDialogAvl: ProgressDialogAvl
    private var progressSheet: ProgressSheet? = null
    open val onRetry: (() -> Unit)? = null
    private var loaderDialog: LoadingDialog? = null


    var onStartCount = 0
    lateinit var binding: Binding
    val app: App
        get() = application as App

    private lateinit var connectivityProvider: ConnectivityProvider
    private var noInternetSheet: NoInternetSheet? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.setContentView(this, layout)

        val vm = getViewModel()
        binding.setVariable(BR.vm, vm)
        vm.onUnAuth.observe(this, Observer {
            showUnauthorised()
        })

        connectivityProvider = ConnectivityProvider.createProvider(this)
        connectivityProvider.addListener(this)
        progressDialogAvl = ProgressDialogAvl(this)
        loaderDialog = LoadingDialog(this)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)
        window.decorView.systemUiVisibility = 0
        window.navigationBarColor = ContextCompat.getColor(this, R.color.line_color)
        setNavigationBarIconsColor(isLight = false) // Change to false for dark icons
        onCreateView()
    }

    private fun setNavigationBarIconsColor(isLight: Boolean) {
        val flags = if (isLight) {
            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            0
        }
        window.decorView.systemUiVisibility = flags
    }

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView()

    fun showUnauthorised() {
        sharedPrefManager.clear()
        sharedPrefManager.clearUser()
        startActivity(LoginActivity.newIntent(this))
        finishAffinity()
    }

    fun showToast(msg: String? = "Something went wrong !!") {
        Toast.makeText(this, msg ?: "Showed null value !!", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    fun showHideLoader(state: Boolean) {
        if (loaderDialog != null) {
            if (state) loaderDialog?.show()
            else loaderDialog?.dismiss()
        }
    }

    fun showLoading() {
        if (!this::progressDialogAvl.isInitialized) progressDialogAvl = ProgressDialogAvl(this)
        progressDialogAvl.isLoading(true)
    }


    fun hideLoading() {
        if (!this::progressDialogAvl.isInitialized) progressDialogAvl = ProgressDialogAvl(this)
        progressDialogAvl.isLoading(false)
    }


    fun onError(error: Throwable) {
        if (error is NetworkError) {

            when (error.errorCode) {
                ErrorCodes.SESSION_EXPIRED -> {
                    showToast(getString(R.string.session_expired))
                    app.onLogout()
                }

                else -> AlertManager.showNegativeAlert(
                    this, error.message, getString(R.string.alert)
                )
            }
        } else {
            AlertManager.showNegativeAlert(
                this, getString(R.string.please_try_again), getString(R.string.alert)
            )
        }
    }

    override fun onDestroy() {

        connectivityProvider.removeListener(this)
        super.onDestroy()
    }


    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        if (noInternetSheet == null) {
            noInternetSheet = NoInternetSheet()
            noInternetSheet?.isCancelable = false
        }
        if (state.hasInternet()) {
            if (noInternetSheet?.isVisible == true) noInternetSheet?.dismiss()
        } else {
            if (noInternetSheet?.isVisible == false) noInternetSheet?.show(
                supportFragmentManager, noInternetSheet?.tag
            )
        }
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

    fun textToRequestBody(text: String?): RequestBody {
        return text!!.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun multipartImageBody(image: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "profile",
//            "profileImage",
            image.name, image.asRequestBody("image/png".toMediaTypeOrNull())
        )
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}