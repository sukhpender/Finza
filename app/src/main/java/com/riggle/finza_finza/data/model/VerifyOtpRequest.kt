package com.riggle.finza_finza.data.model

class VerifyOtpRequest(
    var otp: String = "",
    var requestId: String = "",
    var sessionId: String = "",
    var channel: String = "",
    var agentId: String = "",
    var reqDateTime: String = "",
    var provider: String = "",
    var inventory_id: String = ""
)


data class ValidateOtpRequest(
    val validateOtpReq: VerifyOtpRequest
)