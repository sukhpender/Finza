package com.riggle.plug.data.local

import android.content.SharedPreferences

import com.riggle.plug.data.model.LoginResponseAPI

import com.riggle.plug.data.model.LoginResponseApiData
import com.riggle.plug.utils.getValue
import com.riggle.plug.utils.saveValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.plug.data.local.SharedPrefManager.KEY.USER
import com.riggle.plug.data.model.BeatCityResponseModel
import com.riggle.plug.data.model.BrandVerifyOtp
import com.riggle.plug.data.model.FinzaLoginData
import com.riggle.plug.data.model.VerifyOtpResponseModel
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    object KEY {
        const val USER = "user"
        const val USER_TOKEN = "user_token"
    }

    fun clearUser() {
        sharedPreferences.edit().remove(USER).apply()
    }

    fun saveUser(bean: FinzaLoginData) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.USER, Gson().toJson(bean))
        editor.apply()
    }

    fun getCurrentUser(): FinzaLoginData? {
        val s: String? = sharedPreferences.getString(KEY.USER, null)
        return Gson().fromJson(s, FinzaLoginData::class.java)
    }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        val token: String? = sharedPreferences.getString(KEY.USER_TOKEN, null)
        return token
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}