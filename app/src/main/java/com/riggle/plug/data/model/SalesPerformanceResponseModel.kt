package com.riggle.plug.data.model

data class SalesPerformanceResponseModel(
    val designation_name: String,
    val distance_travelled: Double,
    val full_name: String,
    val id: Int,
    val image: String,
    val orders_revenue: Double,
    val productive_count: Int,
    val total_count: Int
)