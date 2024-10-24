package com.riggle.finza_finza.data.model

data class CancelledResponseModel(
    val `data`: List<Any>,
    val message: String,
    val status: String,
    val statusCode: Int
)