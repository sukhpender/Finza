package com.riggle.finza_finza.data.model

data class HomeInventoryResponseModel(
    val `data`: HomeInventoryData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class HomeInventoryData(
    val badfast_tag_count: Int,
    val badfast_tag_sum: Int,
    val inHandInventory: Int,
    val incomingInventory: Int,
    val last_month_income: Int,
    val last_month_performance: Int,
    val old_inventory: Int,
    val project_detail: ProjectDetail2,
    val this_month_income: String,
    val this_month_performance: Int,
    val today_income: Int,
    val today_performance: Int,
    val wrong_urt_count: Int,
    val wrong_urt_sum: Int
)

data class ProjectDetail2(
    val created_at: String,
    val id: Int,
    val price: String,
    val tag_assigned_charges: String,
    val title: String,
    val type: String,
    val updated_at: String
)