package com.riggle.plug.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.MalformedJsonException
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.riggle.plug.R
import com.riggle.plug.data.model.BrandResult
import com.riggle.plug.databinding.ViewEmptyBinding
import com.riggle.plug.databinding.ViewLoaderBinding
import com.riggle.plug.ui.base.BaseViewModel
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function
import java.util.function.Predicate
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors

/** Network Extensions */

fun ViewEmptyBinding.hideEmptyView() {
    this.main.visibility = View.GONE
    this.tvMessage.text = ""
}

fun ViewLoaderBinding.showLoading() {
    this.shimmer.startShimmer()
    this.main.visibility = View.VISIBLE
}

fun ViewLoaderBinding.hideLoading() {
    this.shimmer.stopShimmer()
    this.main.visibility = View.GONE
}


fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun SharedPreferences.saveValue(key: String, value: Any?) {
    when (value) {
        is String? -> editNdCommit { it.putString(key, value) }
        is Int -> editNdCommit { it.putInt(key, value) }
        is Boolean -> editNdCommit { it.putBoolean(key, value) }
        is Float -> editNdCommit { it.putFloat(key, value) }
        is Long -> editNdCommit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

fun <T> SharedPreferences.getValue(key: String, defaultValue: Any? = null): T? {
    return when (defaultValue) {
        is String? -> {
            getString(key, defaultValue as? String) as? T
        }
        is Int -> {
            getInt(key, defaultValue as? Int ?: -1) as? T
        }
        is Boolean -> getBoolean(key, defaultValue as? Boolean ?: false) as? T
        is Float -> getFloat(key, defaultValue as? Float ?: -1f) as? T
        is Long -> getLong(key, defaultValue as? Long ?: -1) as? T
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

inline fun SharedPreferences.editNdCommit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}


fun Activity.hideKeyboard() {
    val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    try {
        val manager =
            this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            manager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    } catch (e: Exception) {

    }
}


fun Fragment.showSuccessToast(message: String) {
    MyToast.success(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showSuccessToast(message: String) {
    MyToast.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showInfoToast(message: String) {
    MyToast.info(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.showErrorToast(message: String) {
    MyToast.error(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showErrorToast(message: String) {
    MyToast.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showInfoToast(message: String) {
    MyToast.info(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.successToast(message: String) {
    if (message.isNotEmpty())
        MyToast.success(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.successToast(message: String) {
    if (message.isNotEmpty())
        MyToast.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also {
        it.view.setBackgroundColor(ContextCompat.getColor(this.context, R.color.teal_200))
        it.show()
    }
}


/*
fun BaseViewModel.parseException(it: Throwable?): String? {
    when (it) {
        is HttpException -> {
            when (it.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    val message = getErrorText(it)
                    if (message.contains("Unauth")) {
                        EventBus.post(UnAuthUser())
                        return ""
                    }
                    return message
                }
                HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                    return it.message()
                }
                else -> {
                    return getErrorText(it)
                }
            }
        }
        is MalformedJsonException -> {
            return it.message
        }
        is IOException -> {
            return "Slow or No Internet Access"
        }
        else -> {
            return it?.message.toString()
        }
    }


}
*/

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun Resources.dptoPx(dp: Int): Float {
    return dp * this.displayMetrics.density
}

fun Activity.startNewActivity(dest: Class<*>) {
    val intent = Intent(this, dest)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
}

fun Activity.startNewActivity(dest: Class<*>, finish: Boolean) {
    val intent = Intent(this, dest)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
    if (finish)
        finish()
}

fun Fragment.startNewActivity(dest: Class<*>, finish: Boolean) {
    val intent = Intent(this.requireContext(), dest)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
    if (finish)
        this.activity?.finish()
}

fun EditText.focusMode(mode: Boolean) {
    this.isFocusableInTouchMode = mode
    this.isFocusable = mode
}


fun RecyclerView.setLinearLayoutManger() {
    this.layoutManager = LinearLayoutManager(this.context)
}


fun Fragment.showSheet(sheet: BottomSheetDialogFragment?) {
    sheet?.show(this.childFragmentManager, sheet.tag)
}

fun FragmentActivity.showSheet(sheet: BottomSheetDialogFragment) {
    sheet.show(this.supportFragmentManager, sheet.tag)
}


fun BaseViewModel.parseException(it: Throwable?): String? {
    when (it) {
        is HttpException -> {
            when (it.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    val message = getErrorText(it)
                    return message
                }
                HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                    return it.message()
                }
                else -> {
                    return getErrorText(it)
                }
            }
        }
        is MalformedJsonException -> {
            return it.message
        }
        is IOException -> {
            return "Slow or No Internet Access"
        }
        else -> {
            return it?.message.toString()
        }
    }
}

fun Fragment.showKeyboard() {
    try {
        val view = activity?.currentFocus
        val methodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (e: Exception) {

    }
}

fun EditText.showKeyboard() {
    try {
        val methodManager =
            this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        methodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    } catch (e: Exception) {

    }
}

fun <T> distinctByKey(keyExtractor: Function<in T, *>): Predicate<T> {
    val seen: MutableSet<Any> = ConcurrentHashMap.newKeySet()
    return Predicate { t: T -> seen.add(keyExtractor.apply(t)) }
}

fun Activity.showKeyboard() {
    try {
        val view = currentFocus
        val methodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (e: Exception) {

    }
}

fun View.preventClick() {
    this.isEnabled = false
    Handler(Looper.getMainLooper()).postDelayed({
        this.isEnabled = true
    }, 1000)
}

fun View.preventClickTwo() {
    this.isEnabled = false
    Handler(Looper.getMainLooper()).postDelayed({
        this.isEnabled = true
    }, 500)
}

fun View.preventClick250m() {
    this.isEnabled = false
    Handler(Looper.getMainLooper()).postDelayed({
        this.isEnabled = true
    }, 250)
}

fun EditText.isValidNameFormat(name: String): Boolean {
    val pattern = "^.*[a-zA-Z]+.*$"
    val nameREGEX = Pattern.compile(pattern/*"^(?=.*[a-zA-Z])$"*/)
    return nameREGEX.matcher(name).matches()
}

fun EditText.isValidEmail(target: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(target).matches();
}

fun EditText.isValidPinCode(pinCode: String?): Boolean {

    // Regex to check valid pin code of India.
    val regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$"

    // Compile the ReGex
    val p = Pattern.compile(regex)

    // If the pin code is empty
    // return false
    if (pinCode == null) {
        return false
    }

    // Pattern class contains matcher() method
    // to find matching between given pin code
    // and regular expression.
    val m: Matcher = p.matcher(pinCode)

    // Return if the pin code
    // matched the ReGex
    return m.matches()
}

fun EditText.isValidGSTNo(str: String?): Boolean {
    // GST (Goods and Services Tax) number
    val regex = ("^[0-9]{2}[A-Z]{5}[0-9]{4}"
            + "[A-Z]{1}[1-9A-Z]{1}"
            + "Z[0-9A-Z]{1}$")
    // Compile the ReGex
    val p = Pattern.compile(regex)
    if (str == null) {
        return false
    }
    val m = p.matcher(str)
    return m.matches()
}

fun Activity.checkNetworkSpeed() {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // Network Capabilities of Active Network
    val nc = cm.getNetworkCapabilities(cm.activeNetwork)
    // DownSpeed in MBPS
    val downSpeed = (nc?.linkDownstreamBandwidthKbps)?.div(1000)
    // UpSpeed  in MBPS
    val upSpeed = (nc?.linkUpstreamBandwidthKbps)?.div(1000)
    // Toast to Display DownSpeed and UpSpeed
    Toast.makeText(
        this,
        "Up Speed: $upSpeed Mbps \nDown Speed: $downSpeed Mbps",
        Toast.LENGTH_LONG
    ).show()
}

fun Fragment.checkNetworkSpeed() {
    val cm = this.activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // Network Capabilities of Active Network
    val nc = cm.getNetworkCapabilities(cm.activeNetwork)
    // DownSpeed in MBPS
    val downSpeed = (nc?.linkDownstreamBandwidthKbps)?.div(1000)
    // UpSpeed  in MBPS
    val upSpeed = (nc?.linkUpstreamBandwidthKbps)?.div(1000)
    // Toast to Display DownSpeed and UpSpeed
    Toast.makeText(
        this.activity,
        "Up Speed: $upSpeed Mbps \nDown Speed: $downSpeed Mbps",
        Toast.LENGTH_LONG
    ).show()
}