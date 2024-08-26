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
import com.riggle.plug.data.model.VerifyOtpResponseModel
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    object KEY {
        const val USER = "user"
        const val SESSION_ID = "session_id"
        const val CHANNEL_PARTNER = "channel_partner"
        const val SALES_PERSON = "sales_person"
        const val BRANDS_ID = "brands_id"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val BEARER_TOKEN = "bearer_token"
        const val PROFILE_COMPLETED = "profile_completed"
        const val APPEARANCE_KEY = "appearance_key"
        const val LOCALE = "locale_key"
        const val TODAY_RECORD = "today_record"
        const val TODAY = "today"
        const val ANS = "ans"
        const val IS_FIRST = "is_first"
        const val IS_FIRST_HOME = "is_first_home"
        const val IS_FIRST_ESTIMATE = "is_first_estimate"
        const val USER_TOKEN = "user_token"
        const val ORDER_TYPE = "order_type"
        const val BRANDS_LIST = "brands_list"
    }

    fun saveOrderType(type: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.ORDER_TYPE, type)
        editor.apply()
    }

    fun getOrderType(): String? {
        return sharedPreferences.getValue(KEY.ORDER_TYPE, "")
    }

    fun saveIsFirst(isFirst: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY.IS_FIRST, isFirst)
        editor.apply()
    }

    fun getIsFirst(): Boolean? {
        return sharedPreferences.getValue(KEY.IS_FIRST, false)
    }

    fun saveIsFirstEstimate(isFirst: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY.IS_FIRST, isFirst)
        editor.apply()
    }

    fun saveCP(id: String?) {
        sharedPreferences.saveValue(KEY.CHANNEL_PARTNER, id)
    }

    fun getCP(): String? {
        return sharedPreferences.getValue(KEY.CHANNEL_PARTNER, null)
    }

    fun saveSalesPerson(id: String) {
        sharedPreferences.saveValue(KEY.SALES_PERSON, id)
    }

    fun getSalesPerson(): String? {
        return sharedPreferences.getValue(KEY.SALES_PERSON, null)
    }

    fun saveBrandsIdString(id: String) {
        sharedPreferences.saveValue(KEY.BRANDS_ID, id)
    }

    fun getBrandsId(): String? {
        return sharedPreferences.getValue(KEY.BRANDS_ID, null)
    }

    fun saveStartDate(sDate: String) {
        sharedPreferences.saveValue(KEY.START_DATE, sDate)
    }

    fun getStartDate(): String? {
        return sharedPreferences.getValue(KEY.START_DATE, null)
    }

    fun saveEndDate(eDate: String) {
        sharedPreferences.saveValue(KEY.END_DATE, eDate)
    }

    fun getEndDate(): String? {
        return sharedPreferences.getValue(KEY.END_DATE, null)
    }

    private val gson = Gson()
    fun saveBrandsList(list: List<BrandVerifyOtp>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(list)
        editor.putString(KEY.BRANDS_LIST, json)
        editor.apply()
    }

    fun gateBrandsList(): List<BrandVerifyOtp> {
        val json = sharedPreferences.getString(KEY.BRANDS_LIST, null)
        return if (json != null) {
            val type = object : TypeToken<List<BrandVerifyOtp>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }


    fun getIsFirstEstimate(): Boolean? {
        return sharedPreferences.getValue(KEY.IS_FIRST, false)
    }

    fun clearUser() {
        sharedPreferences.edit().remove(USER).apply()
    }

    fun saveUser(bean: VerifyOtpResponseModel) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.USER, Gson().toJson(bean))
        editor.apply()
    }

    fun getCurrentUser(): VerifyOtpResponseModel? {
        val s: String? = sharedPreferences.getString(KEY.USER, null)
        return Gson().fromJson(s, VerifyOtpResponseModel::class.java)
    }

    fun saveSelectedCity(bean: BeatCityResponseModel?) {
        val editor = sharedPreferences.edit()
        editor.putString("SELECTED_CITY", Gson().toJson(bean))
        editor.apply()
    }

    fun getSelectedCity(): BeatCityResponseModel? {
        val s: String? = sharedPreferences.getString("SELECTED_CITY", null)
        return Gson().fromJson(s, BeatCityResponseModel::class.java)
    }

    fun saveSessionId(id: String) {
        sharedPreferences.saveValue(KEY.SESSION_ID, id)
    }

    fun getSessionId(): String? {
        return sharedPreferences.getValue(KEY.SESSION_ID, null)
    }

    fun saveStatus(userId: String) {
        sharedPreferences.saveValue(KEY.BEARER_TOKEN, userId)
    }

    fun getStatus(): String? {
        return sharedPreferences.getValue(KEY.BEARER_TOKEN, "Open")
    }

    fun profileCompleted(isProfile: Boolean) {
        sharedPreferences.saveValue(KEY.PROFILE_COMPLETED, isProfile)
    }

    fun isProfileCompleted(): Boolean? {
        return sharedPreferences.getValue(KEY.PROFILE_COMPLETED, false)
    }

    fun setAppearance(type: Int) {
        sharedPreferences.saveValue(KEY.APPEARANCE_KEY, type)
    }

    fun getAppearance(): Int {
        return sharedPreferences.getInt(KEY.APPEARANCE_KEY, 0)
    }

    fun setLocaleType(type: String?) {
        sharedPreferences.saveValue(KEY.LOCALE, type)
    }

    fun getLocaleType(): String? {
        return sharedPreferences.getString(KEY.LOCALE, "en")
    }


    fun getToday(): Int {
        return sharedPreferences.getInt(KEY.TODAY, 0)
    }

    fun setToday(type: Int?) {
        sharedPreferences.saveValue(KEY.TODAY, type)
    }

    fun ansToday(): Int {
        return sharedPreferences.getInt(KEY.ANS, 0)
    }

    fun setAnsToday(type: Int?) {
        sharedPreferences.saveValue(KEY.ANS, type)
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


    /* fun getToken(): String {
         return getCurrentUser()?.token?.let { token ->
             "Bearer $token"
         }.toString()
     }*/

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}