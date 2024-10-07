package com.riggle.finza_finza.data.model

data class ActiveSalesmanResponseModel(
    val active: Boolean,
    val active_hours: Any?,
    val designation_name: String,
    val end_time: EndTime,
    val full_name: String,
    val home_location: String,
    val id: Int,
    val image: String?,
    val mobile: String,
    val start_time: StartTime
)

data class EndTime(
    val image: String,
    val timestamp: String
)

data class StartTime(
    val image: String,
    val timestamp: String
)