package com.riggle.finza_finza.data.local

import android.content.SharedPreferences

import com.google.gson.Gson
import com.riggle.finza_finza.data.local.SharedPrefManager.KEY.USER
import com.riggle.finza_finza.data.local.SharedPrefManager.KEY.WALLET_USER
import com.riggle.finza_finza.data.model.FinzaLoginData
import com.riggle.finza_finza.data.model.WalletCreateCustomerData
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    object KEY {
        const val USER = "user"
        const val WALLET_USER = "wallet_user"
        const val USER_TOKEN = "user_token"
        const val PROJECT_ID = "project_id"
    }

    fun clearUser() {
        sharedPreferences.edit().remove(USER).apply()
        sharedPreferences.edit().remove(WALLET_USER).apply()
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

    fun saveWalletUser(bean: WalletCreateCustomerData) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.WALLET_USER, Gson().toJson(bean))
        editor.apply()
    }

    fun getWalletUser(): WalletCreateCustomerData? {
        val s: String? = sharedPreferences.getString(KEY.WALLET_USER, null)
        return Gson().fromJson(s, WalletCreateCustomerData::class.java)
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

    fun saveAuditId(id: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.PROJECT_ID, id)
        editor.apply()
    }

    fun getAuditId(): String? {
        val token: String? = sharedPreferences.getString(KEY.PROJECT_ID, null)
        return token
    }

    fun saveTypeId(id: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Type1", id)
        editor.apply()
    }

    fun getTypeId(): String? {
        val token: String? = sharedPreferences.getString("Type1", null)
        return token
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}