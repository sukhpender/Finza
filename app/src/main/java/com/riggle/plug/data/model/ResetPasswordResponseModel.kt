package com.riggle.plug.data.model

data class ResetPasswordResponseModel(
    val `data`: DataX,
    val message: String,
    val status: String,
    val statusCode: Int
)