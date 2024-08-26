package com.riggle.plug.data.model

data class AssignedBeatDetailsResponseModel(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ResultAssignedBeatDetails>
)

data class ResultAssignedBeatDetails(
    val address_line: String,
    val admin: AdminAssignedBeatDetails,
    val beats: List<BeatAssignedBeatDetails>,
    val city: String,
    val full_address: String,
    val id: Int,
    val last_order: String,
    val last_order_amount: Double?,
    val last_vist_log: String?,
    val lat: String,
    val locality: String,
    val logo: String,
    val long: String,
    val name: String,
    val pincode: String,
    val short_address: String,
    val state: String,
    val update_url: String
)

data class AdminAssignedBeatDetails(
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class BeatAssignedBeatDetails(
    val id: Int,
    val name: String,
    val update_url: String
)