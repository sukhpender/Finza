package com.riggle.finza_finza.data.model


data class VerifyOtpResponseModel(
    val `data`: VerifyOtpData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class VerifyOtpData(
    val response: Response3,
    val validateOtpResp: ValidateOtpResp
)

data class Response3(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)

data class ValidateOtpResp(
    val custDetails: CustDetails,
    val npciStatus: String,
    val req_type: String,
    val requestId: String,
    val respDateTime: String,
    val udf1: String,
    val udf2: String,
    val udf3: String,
    val udf4: String,
    val udf5: String,
    val vrnDetails: VrnDetails
)

data class CustDetails(
    val kycStatus: String,
    val mobileNo: String,
    val name: String,
    val walletId: String,
    val walletStatus: String
)

data class VrnDetails(
    val chassisNo: String,
    val commercial: Boolean,
    val engineNo: String,
    val isNationalPermit: String,
    val model: String?,
    val npciVehicleClassID: String,
    val permitExpiryDate: String?,
    val rechargeAmount: String,
    val repTagCost: String,
    val rtoStatus: String,
    val securityDeposit: String,
    val stateOfRegistration: String?,
    val tagCost: String,
    val tagVehicleClassID: String,
    val type: String,
    val vehicleColour: String,
    val vehicleDescriptor: String?,
    val vehicleManuf: String?,
    val vehicleNo: String,
    val vehicleType: String
)