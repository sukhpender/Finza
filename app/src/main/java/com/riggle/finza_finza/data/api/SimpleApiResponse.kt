package com.riggle.finza_finza.data.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class SimpleApiResponse : Serializable {

    @SerializedName("statusCode")
    var statusCode: Int? = null
        protected set

    @SerializedName("message")
    var message: String? = null
        protected set

    override fun toString(): String {
        return "SimpleApiResponse{" +
                "success=" + statusCode +
                ", message='" + message + '\'' +
                '}'
    }
}