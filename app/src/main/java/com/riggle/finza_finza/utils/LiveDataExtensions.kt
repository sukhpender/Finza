package com.riggle.finza_finza.utils

import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.event.SingleLiveEvent
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

fun <T> BaseViewModel.apiSubscription(
    method: Method<T>,
    liveData: SingleLiveEvent<Resource<T>>
) {
    Coroutines.io {
        liveData.postValue(Resource.loading(null))
        /*        try {
                    var token: String? = null
                    SharedPrefManager.getToken()?.let { it ->
                        token = "Bearer $it"
                    }

                    method.getSimpleApiMethod(token).let {
                        if (it?.body()?.isStatus == true) {
                            it.body()?.success?.let {
                                liveData.postValue(Resource.success(it.data, it.message))
                            }
                        } else {
                            liveData.postValue(setThrowableCode(it))
                        }
                    }
                } catch (e: Exception) {
                    if (e is UnknownHostException) {
                        liveData.postValue(Resource.error( null,"No Internet Connection"))
                    } else {

                    }
                }*/
    }
}

fun getErrorText(it: HttpException): String {
    val errorBody: ResponseBody? = it.response()?.errorBody()
    val text: String? = errorBody?.string()
    if (!text.isNullOrEmpty()) {
        return try {
            val obj = JSONObject(text)
            obj.getString("message")
        } catch (e: Exception) {
            return text
        }
    }
    return it.message().toString()
}




