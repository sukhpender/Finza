package com.riggle.finza_finza.data.model

data class SalesDailyAnalysisResponseModel(
    val end_day: Boolean,
    val existing_orders_count: Int,
    val existing_orders_values: Int,
    val existing_remarks: Int,
    val new_orders_count: Int,
    val new_orders_values: Int,
    val new_remarks: Int,
    val start_day: Boolean,
    val total_orders: Int,
    val total_placed_orders_value: Double,
    val total_remarks: Int
)