package com.riggle.finza_finza.data.model

data class AssignInventoryResponseModel(
    val `data`: AssignInventoryData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class AssignInventoryData(
    val assigned_by: Int,
    val assigned_to: String,
    val created_at: String,
    val id: Int,
    val inventory_id: String,
    val is_accepted: Int,
    val is_installed: Int,
    val status: Int,
    val updated_at: String
)

data class MultipleTransferResponseModel(
    val `data`: List<MultipleTransferData>,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class MultipleTransferData(
    val age_in_days: Int,
    val assigned_by: Int,
    val assigned_to: Int,
    val created_at: String,
    val id: Int,
    val inventory_id: Int,
    val is_accepted: Int,
    val is_installed: Int,
    val status: Int,
    val updated_at: String
)