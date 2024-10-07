package com.riggle.finza_finza.utils

import com.riggle.finza_finza.utils.event.SimpleApiResponse
import retrofit2.Response

abstract class Method<T> {
    open suspend fun getSimpleApiMethod(token: String?): Response<SimpleApiResponse<T>>? {
        return null
    }

}