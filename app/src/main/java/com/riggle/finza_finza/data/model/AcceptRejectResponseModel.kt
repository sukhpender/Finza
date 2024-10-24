package com.riggle.finza_finza.data.model

data class AcceptRejectResponseModel(
    val `data`: List<Int>,
    val message: String,
    val status: String,
    val statusCode: Int
)