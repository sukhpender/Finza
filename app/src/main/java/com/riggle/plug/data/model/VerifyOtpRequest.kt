package com.riggle.plug.data.model

class VerifyOtpRequest(
    var otp: String = "",
    var requestId: String = "",
    var sessionId: String = "",
    var channel: String = "",
    var agentId: String = "",
    var reqDateTime: String = "",
    var provider: String = ""
)


data class ValidateOtpRequest(
    val validateOtpReq: VerifyOtpRequest
)