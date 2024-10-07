package com.riggle.finza_finza.data.model

data class ReportingManagerResponseModel(
    val designation_name: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val is_active: Boolean,
    val last_name: String,
    val mobile: String,
    val update_url: String,
    var isSelected: Boolean = false
)