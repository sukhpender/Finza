package com.riggle.finza_finza.data.model

data class SendOtpRequest(
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
) {
    override fun toString(): String {
        return "SendOtpRequest(chassisNo=$chassisNo, engineNo=$engineNo, isChassis=$isChassis, mobileNo=$mobileNo, provider=$provider, reqDateTime=$reqDateTime, reqType=$reqType, requestId=$requestId, resend=$resend, udf1=\"$udf1\", udf2=\"$udf2\", udf3=\"$udf3\", udf4=\"$udf4\", udf5=\"$udf5\", vehicleNo=$vehicleNo)"
    }
}