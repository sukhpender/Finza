package com.riggle.plug.utils

import com.riggle.plug.utils.event.SimpleApiResponse
import retrofit2.Response

abstract class Method<T> {
    open suspend fun getSimpleApiMethod(token: String?): Response<SimpleApiResponse<T>>? {
        return null
    }

}