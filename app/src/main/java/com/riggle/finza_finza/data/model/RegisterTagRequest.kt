package com.riggle.finza_finza.data.model

import com.squareup.moshi.Json

data class RegisterTagRequest(
    @Json(name = "regDetails") val regDetails: RegDetails,
    @Json(name = "vrnDetails") val vrnDetails: VrnDetails1,
    @Json(name = "custDetails") val custDetails: CustDetails2,
    @Json(name = "fasTagDetails") val fasTagDetails: FasTagDetails,
    @Json(name = "provider") val provider: String
)

data class RegDetails(
    val requestId: String,
    val sessionId: String,
    val channel: String,
    val agentId: String,
    val reqDateTime: String
)

data class VrnDetails1(
    val vrn: String,
    val chassis: String,
    val engine: String,
    val vehicleManuf: String,
    val model: String,
    val vehicleColour: String,
    val type: String,
    val status: String,
    val npciStatus: String,
    val isCommercial: Boolean,
    val tagVehicleClassID: String,
    val npciVehicleClassID: String,
    val vehicleType: String,
    val rechargeAmount: String,
    val securityDeposit: String,
    val tagCost: String,
    val debitAmt: String,
    val vehicleDescriptor: String,
    val isNationalPermit: String,
    val permitExpiryDate: String,
    val stateOfRegistration: String
)

data class CustDetails2(
    val name: String,
    val mobileNo: String,
    val walletId: String
)

data class FasTagDetails(
    val serialNo: String,
    val tid: String?,
    val udf1: String?,
    val udf2: String?,
    val udf3: String?,
    val udf4: String?,
    val udf5: String?
)
