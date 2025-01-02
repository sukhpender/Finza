package com.riggle.finza_finza.data.model

data class TagReplaceRequest(
    val tagReplaceReq: TagReplaceReq
)

data class TagReplaceReq(
    val mobileNo: String,
    val walletId: String,
    val vehicleNo: String,
    val channel: String,
    val agentId: String,
    val reqDateTime: String,
    val debitAmt: String,
    val requestId: String,
    val sessionId: String,
    val serialNo: String,
    val reason: String,
    val reasonDesc: String = "",
    val chassisNo: String,
    val engineNo: String,
    val isNationalPermit: String,
    val permitExpiryDate: String,
    val stateOfRegistration: String,
    val vehicleDescriptor: String,
    val udf1: String,
    val udf2: String,
    val udf3: String,
    val udf4: String,
    val udf5: String
)