package com.riggle.finza_finza.data.model

data class SendOtpResponseModel(
    val data: Data,
    val message: String
)

data class Data(
    val already_exists: Boolean
)