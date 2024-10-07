package com.riggle.finza_finza.data.model

data class VehicleMakersListResponseModel(
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