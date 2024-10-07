package com.riggle.finza_finza.data.model

data class HomeOrderSummaryOrdersResponseModel(
    val orders: Orders1
)

data class Orders1(
    val delivered: List<Placed>,
    val placed: List<Placed>
)

data class Placed(
    val day: String,
    val revenue: Double,
    val start_of_week: String?,
    val end_of_week: String?,
    val month: String
)