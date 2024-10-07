package com.riggle.finza_finza.data.model

data class FinzaLogoutResponseModel(
    val `data`: Data,
    val message: String,
    val status: String,
    val statusCode: Int
)