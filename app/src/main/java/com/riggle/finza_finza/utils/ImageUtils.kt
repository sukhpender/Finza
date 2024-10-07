package com.riggle.finza_finza.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.riggle.finza_finza.R
import com.google.gson.Gson

object ImageUtils {
    fun navigateWithSlideAnimations(navController: NavController, destinationId: Int) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Define enter animation
            .setExitAnim(R.anim.slide_out_left) // Define exit animation
            //.setPopEnterAnim(R.anim.slide_in_left) // Define pop enter animation
            //.setPopExitAnim(R.anim.slide_out_right) // Define pop exit animation
            .build()

        navController.navigate(destinationId, null, navOptions)
    }

    fun goActivity(context: Context, activity: Int) {
        val intent = Intent(context, activity::class.java)
        startActivity(context, intent, null)


    }

    fun getStatusBarColor(activity: Activity, intColor: Int) {
        activity.window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        activity.window.statusBarColor = ContextCompat.getColor(activity, intColor)


    }


    @BindingAdapter("setIcon")
    @JvmStatic
    fun setIcon(image: ImageView, i: Int) {
        image.setImageResource(i)
    }

    inline fun <reified T> parseJson(json: String): T? {
        return try {
            val gson = Gson()
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}