package com.riggle.finza_finza.data.model

data class VehicleMakersListResponseModel(
    val `data`: VehicleMakersData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class VehicleMakersData(
    val response: VehicleMakersListResponse,
    val vehicleMakerList: List<String>
)

data class VehicleMakersListResponse(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)

data class VehicleModelListResponseModel(
    val `data`: VehicleModelData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class VehicleModelData(
    val response: VehicleModelListResponse,
    val vehicleModelList: List<String>
)

data class VehicleModelListResponse(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)