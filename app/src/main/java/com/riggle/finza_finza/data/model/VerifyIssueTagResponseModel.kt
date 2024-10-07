package com.riggle.finza_finza.data.model

data class VerifyIssueTagResponseModel(
    val `data`: VerifyIssueTagData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class VerifyIssueTagData(
    val response: Response,
    val validateCustResp: ValidateCustResp
)

data class Response(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)

data class ValidateCustResp(
    val agentId: String,
    val channel: String,
    val chassisNo: String,
    val mobileNo: String,
    val otpStatus: String,
    val reqDateTime: String,
    val reqType: String,
    val requestId: String,
    val respDateTime: String,
    val sessionId: String,
    val udf1: Any?,
    val udf2: Any?,
    val udf3: Any?,
    val udf4: Any?,
    val udf5: Any?,
    val vehicleNo: String
)