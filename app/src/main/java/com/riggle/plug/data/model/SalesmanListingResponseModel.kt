package com.riggle.plug.data.model

data class SalesmanListingResponseModel(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<ResultSalesmanListing>
)

data class ResultSalesmanListing(
    val allow_quick_order: Boolean,
    val blood_group: String,
    val company_van_sales_active: Boolean,
    val date_of_joining: String,
    val dob: String,
    val employee_id: String,
    val first_name: String,
    val full_name: String,
    val hide_cp: Boolean,
    val home_location: String,
    val id: Int,
    val image: String,
    val is_active: Boolean,
    val is_manager: Boolean,
    val last_name: String,
    val manager: Manager,
    val mobile: String,
    val role: String,
    val sales_designation: SalesDesignation,
    val update_url: String,
    val van_sales: Boolean
)

data class Manager(
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class SalesDesignation(
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