package com.riggle.plug.data.model

data class CPLeaderBoardResponseModel(
    val city: String,
    val current_month_revenue: Double,
    val current_month_target: Int,
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val last_month_revenue: Double,
    val last_month_target: Int,
    val lat: String,
    val logo: String,
    val long: String,
    val name: String,
    val pincode: String,
    val retailer_approved: Boolean,
    val state: String,
    val update_url: String
)