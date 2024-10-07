package com.riggle.finza_finza.data.model

data class TargetUserData(
    val designation_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val new_activation_achieved: Int,
    val new_activation_target: Int,
    val primary_order_value: Int,
    val primary_target: Int,
    val retailer_achieved: Int,
    val retailer_target: Int,
    val secondary_order_value: Double,
    val secondary_target: Int,
    val sku_achieved: Double,
    val sku_target: Int,
    val states: List<String>
)