package com.riggle.finza_finza.data.model

data class GetVehicleDetailsResponseModel(
    val `data`: GetVehicleDetailsData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class GetVehicleDetailsData(
    val `data`: GetVehicleDetailsDataX,
    val dataType: Int,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class GetVehicleDetailsDataX(
    val blacklistStatus: Any,
    val bodyTypeDesc: String,
    val chassisNo: String,
    val createdAt: String,
    val cubicCap: String,
    val engineNo: String,
    val fatherName: String,
    val financerDetails: Any,
    val fitnessUpto: String,
    val fuelNorms: String,
    val fuelType: String,
    val insCompany: String,
    val insUpto: String,
    val maker: String,
    val makerModal: String,
    val manufacturedMonthYear: String,
    val mobileNo: Any,
    val nationalPermitIssuedBy: Any,
    val nationalPermitNo: Any,
    val nationalPermitUpto: Any,
    val noCylinders: String,
    val noOfSeats: String,
    val nocDetails: Any,
    val nonUseFrom: Any,
    val nonUseStatus: Any,
    val nonUseTo: Any,
    val ownJson: Any,
    val ownerName: String,
    val ownerSrNo: String,
    val permanentAddress: String,
    val permitFrom: Any,
    val permitIssueDate: Any,
    val permitNo: Any,
    val permitType: Any,
    val permitUpto: Any,
    val policyNo: String,
    val presentAddress: String,
    val privahanJson: Any,
    val pucNo: String,
    val pucUpto: String,
    val regDate: String,
    val regNo: String,
    val responseType: String,
    val rto: String,
    val rtoCode: String,
    val sleeperCap: Any,
    val source: Any,
    val standCap: Any,
    val state: String,
    val status: String,
    val statusOn: String,
    val taxUpto: String,
    val unladenWeight: String,
    val updatedAt: String,
    val vehicleCategory: String,
    val vehicleClass: String,
    val vehicleColor: String,
    val vehicleGrossWeight: String,
    val viStatus: String,
    val wheelBase: String
)