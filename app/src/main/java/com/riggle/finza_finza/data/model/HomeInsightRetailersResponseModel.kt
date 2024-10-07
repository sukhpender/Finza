package com.riggle.finza_finza.data.model

data class HomeInsightRetailersResponseModel(
    val retailers: List<Retailer>
)

data class Retailer(
    val day: String,
    val start_of_week: String?,
    val end_of_week: String?,
    val month: String,
    val transacting_count: Int

)