package com.riggle.finza_finza.data.model

data class HomeOrderSummaryResponseModel(
    val extras: Extras, val orders: Orders, val remarks: Remarks1
)

data class Extras(
    val aov: List<Arpr>, val arpr: List<Arpr>
)

data class Orders(
    val existing: List<New1>, val new: List<New1>, val total: List<New1>
)

data class Remarks1(
    val existing: List<New1>, val new: List<New1>?, val total: List<New1>
)

data class Arpr(
    val count: Int,
    val day: String,
    val revenue: Double,
    val start_of_week: String,
    val end_of_week: String,
    val month: String,
    val total_revenue: Double,
)

data class New1(
    val count: Int,
    val day: String,
    val start_of_week: String?,
    val end_of_week: String?,
    val month: String,
)

