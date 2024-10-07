package com.riggle.finza_finza.data.model

data class ResetPasswordResponseModel(
    val `data`: DataX,
    val message: String,
    val status: String,
    val statusCode: Int
)