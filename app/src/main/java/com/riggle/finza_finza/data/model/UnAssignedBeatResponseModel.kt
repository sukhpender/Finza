package com.riggle.finza_finza.data.model

data class UnAssignedBeatResponseModel(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<ResultUnAssignedBeat>
)

data class ResultUnAssignedBeat(
    val address_line: String,
    val admin: AdminUnAssignedBeat,
    val city: String,
    val full_address: String,
    val id: Int,
    val last_order: String,
    val last_order_amount: Double?,
    val last_vist_log: String?,
    val locality: String,
    val logo: String?,
    val name: String,
    val pincode: String,
    val short_address: String,
    val state: String,
    val update_url: String
)

data class AdminUnAssignedBeat(
    val email: String?,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String?,
    val last_name: String,
    val mobile: String,
    val update_url: String
)