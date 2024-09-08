package com.riggle.plug.data.model

data class SendOtpIssueTagResponseModel(
    val `data`: Data,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class SendOtpIssueTagResponseData(
    val agentId: String,
    val channel: String,
    val chassisNo: String,
    val code: String,
    val created_at: String,
    val engineNo: String,
    val errorCode: String,
    val errorDesc: String,
    val id: Int,
    val isChassis: Int,
    val mobileNo: String,
    val msg: String,
    val otpStatus: String,
    val reqDateTime: String,
    val reqType: String,
    val requestId: String,
    val resend: Int,
    val respDateTime: Any,
    val sessionId: String,
    val status: String,
    val udf1: String?,
    val udf2: String?,
    val udf3: String?,
    val udf4: String?,
    val udf5: String?,
    val updated_at: String,
    val user_id: Int,
    val vehicleNo: String
)