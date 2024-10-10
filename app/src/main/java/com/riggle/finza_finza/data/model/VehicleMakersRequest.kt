package com.riggle.finza_finza.data.model

//class VehicleMakersRequest(
//    var requestId: String = "",
//    var sessionId: String = "",
//    var channel: String = "",
//    var agentId: String = "",
//    var reqDateTime: String = "",
//    var provider: String = ""
//)

data class VehicleMakersRequest(
    val getVehicleMake: GetVehicleMake
)

data class GetVehicleMake(
    val requestId: String,
    val sessionId: String,
    val channel: String,
    val agentId: String,
    val reqDateTime: String,
    val provider: String,
    val inventory_id : String
)

data class VehicleModelRequest(
    val getVehicleModel: GetVehicleModel
)

data class GetVehicleModel(
    val requestId: String,
    val sessionId: String,
    val channel: String,
    val agentId: String,
    val reqDateTime: String,
    val provider: String,
    val vehicleMake: String,
    val inventory_id : String
)