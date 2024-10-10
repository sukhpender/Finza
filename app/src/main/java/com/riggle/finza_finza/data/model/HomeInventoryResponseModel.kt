package com.riggle.finza_finza.data.model

data class HomeInventoryResponseModel(
    val `data`: HomeInventoryData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class HomeInventoryData(
    val inHandInventory: Int,
    val incomingInventory: Int,
    val old_inventory: Int,
    val today_performance: Int,
    val today_income: Int,
    val project_detail: ProjectDetail2
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