package com.riggle.plug.data.model

data class BrandOfferResponseModel(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<BrandOfferResult>
)

data class BrandOfferResult(
    val aim_for: String,
    val available_quantity: Int,
    val brand: Int,
    val code: String,
    val company: Int,
    val created_at: String,
    val description: String,
    val discount_amount: Double,
    val discount_type: String,
    val expiry: Any,
    val id: Int,
    val is_active: Boolean,
    val min_amount: Double,
    val order_amount: Double,
    val ordered_quantity: Int,
    val payment_amount: Int,
    val pincodes: List<Any>,
    val title: String,
    val type: String,
    val update_url: String,
    val updated_at: String,
    val usage_type: String
)