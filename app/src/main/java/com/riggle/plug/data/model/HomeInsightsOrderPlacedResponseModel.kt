package com.riggle.plug.data.model

data class HomeInsightsOrderPlacedResponseModel(
    val orders: Orders2
)

data class Orders2(
    val placed: List<Placed2>
)

data class Placed2(
    val day: String,
    val revenue: Double,
    val start_of_week: String?,
    val end_of_week: String?,
    val month: String
)