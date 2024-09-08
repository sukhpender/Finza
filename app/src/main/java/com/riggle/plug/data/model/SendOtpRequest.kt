package com.riggle.plug.data.model

data class SendOtpRequest(
    var agentId: String = "",
    var channel: String = "",
    var chassisNo: String = "",
    var engineNo: String = "",
    var isChassis: Int = 0,
    var mobileNo: String = "",
    var provider: String = "",
    var reqDateTime: String = "",
    var reqType: String = "",
    var requestId: String = "",
    var resend: Int = 0,
    var udf1: String = "",
    var udf2: String = "",
    var udf3: String = "",
    var udf4: String = "",
    var udf5: String = "",
    var vehicleNo: String = ""
)