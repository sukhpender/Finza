package com.riggle.finza_finza.data.model

data class SalesLocationResponseModel(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<ResultSalesLocation>
)

data class ResultSalesLocation(
    val created_at: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String,
    val type: String,
    var distance: String
)