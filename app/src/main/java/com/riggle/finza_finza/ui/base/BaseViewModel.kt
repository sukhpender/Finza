package com.riggle.finza_finza.ui.base


import android.view.View
import androidx.lifecycle.ViewModel
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SimpleApiResponse
import com.riggle.finza_finza.utils.event.SingleActionEvent
import com.riggle.finza_finza.utils.event.SingleLiveEvent
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.net.HttpURLConnection

open class BaseViewModel : ViewModel() {

    val onClick: SingleActionEvent<View?> = SingleActionEvent()
    val onUnAuth: SingleLiveEvent<Int?> = SingleLiveEvent()

    override fun onCleared() {
        super.onCleared()
    }

    open fun onClick(view: View?) {
        onClick.value = view
    }

    fun <T> setThrowableCode(resource: Response<SimpleApiResponse<T>>?): Resource<T>? {
        if (resource?.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            onUnAuth.postValue(resource.code())
            return null
        }
        return Resource.error(null, resource?.errorBody().toString())
    }


    fun handleErrorResponse(errorBody: ResponseBody?, code: Int? = null): String {
        val text: String? = errorBody?.string()
        if (code != null && code == 401) {
            onUnAuth.postValue(code)
        }
        if (!text.isNullOrEmpty()) {
            return try {
                val obj = JSONObject(text)
                obj.getString("message")
            } catch (e: Exception) {
                return text
            }
        }
        return errorBody.toString()
    }
}