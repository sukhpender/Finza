package com.riggle.finza_finza.data.model


data class RegisterTagResponseModel(
    val `data`: Data7,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class Data7(
    val response: RegisterTagResponse,
    val tagRegistrationResp: TagRegistrationResp
)

data class RegisterTagResponse(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)

data class TagRegistrationResp(
    val agentBalance: String,
    val agentId: String,
    val channel: String,
    val npciStatus: String,
    val requestId: String,
    val respDateTime: String,
    val serialNo: String,
    val tid: String,
    val udf1: String,
    val udf2: String,
    val udf3: String,
    val udf4: String,
    val udf5: String,
    val vrn: String,
    val walletId: String
)