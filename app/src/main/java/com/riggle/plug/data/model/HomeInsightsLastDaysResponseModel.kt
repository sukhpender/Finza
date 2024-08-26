package com.riggle.plug.data.model

data class HomeInsightsLastDaysResponseModel(
    val aov: Double,
    val beats_count: Int,
    val day: String,
    val orders_count: Int,
    val orders_revenue: Double,
    val pending_retailers: Int,
    val remarks_count: Int,
    val total_retailers: Int,
    val total_visits: Int
)