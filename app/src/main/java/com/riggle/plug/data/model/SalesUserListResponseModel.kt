package com.riggle.plug.data.model

class SalesUserListResponseModel (
    val active: Boolean,
    val company: Int,
    val designation: String,
    val existing_orders_count: Int,
    val existing_orders_values: Int,
    val existing_remarks: Int,
    val id: Int,
    val image: String,
    val is_active: Boolean,
    val is_manager: Boolean,
    val name: String,
    val new_orders_count: Int,
    val new_orders_values: Int,
    val new_remarks: Int,
    val total_delivered_value: Int,
    val total_orders_count: Int,
    val total_placed_value: Int,
    val total_remarks: Int,
    val total_visits: Int
)