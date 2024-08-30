package com.riggle.plug.data.model

data class FinzaLogoutResponseModel(
    val `data`: Data,
    val message: String,
    val status: String,
    val statusCode: Int
)