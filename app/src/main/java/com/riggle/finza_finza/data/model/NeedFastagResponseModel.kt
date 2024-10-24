package com.riggle.finza_finza.data.model

data class NeedFastagResponseModel(
    val `data`: NeedFastagData,
    val message: String,
    val status: String,
    val statusCode: Int
)

class NeedFastagData