package com.riggle.finza_finza.data.model

data class DesignationSalesFilterResponseModel(
    val company: Int,
    val created_at: String,
    val id: Int,
    val level: Int,
    val name: String,
    val show_on_dashboard: Boolean,
    val update_url: String,
    val updated_at: String,
    val user_count: Int,
    var isSelected: Boolean = false
)