package com.riggle.plug.data.model

data class SalesBeatCountResponseModel(
    val delivered_orders_value: Any,
    val orders_count: Int,
    val percentage: Int,
    val placed_order_value: Any,
    val target: Int,
    val target_data: TargetData,
    val total_orders: Int,
    val total_remarks: Int,
    val total_visits: Int
)

data class TargetData(
    val active_accounts: Int,
    val created_at: String,
    val editable: Boolean,
    val id: Int,
    val month: Int,
    val order_amount: Int,
    val order_type: Any,
    val productive_count: Int,
    val target: Int,
    val total_count: Int,
    val update_url: String,
    val updated_at: String,
    val user: Int,
    val year: Int
)