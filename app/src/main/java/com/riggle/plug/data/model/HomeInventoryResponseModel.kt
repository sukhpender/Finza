package com.riggle.plug.data.model

data class HomeInventoryResponseModel(
    val `data`: HomeInventoryData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class HomeInventoryData(
    val inHandInventory: Int,
    val incomingInventory: Int,
    val old_inventory: Int
)