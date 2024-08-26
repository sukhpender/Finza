package com.riggle.plug.data.model

data class SalesNetworkResponseModel(
    val direct_reportee: Int,
    val dob: String,
    val expanded: Boolean,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val indirect_reportee: Int,
    val is_manager: Boolean,
    val last_name: String,
    val manager: Any?,
    val mobile: String,
    val sales_designation: SalesDesignation1,
    val update_url: String
)

data class SalesDesignation1(
    val company: Int,
    val created_at: String,
    val id: Int,
    val level: Int,
    val name: String,
    val show_on_dashboard: Boolean,
    val update_url: String,
    val updated_at: String,
    val user_count: Int
)