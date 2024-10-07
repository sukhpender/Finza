package com.riggle.finza_finza.data.model

data class BrandResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<BrandResult>
)

data class BrandResult(
    val brand: Brand1?,
    val buyer_count: Int,
    val company: CompanyUpDated,
    val created_at: String,
    val id: Int,
    val is_marketplace_seller: Boolean,
    val is_offer_available: Boolean?,
    val monthly_target: Int,
    val pincodes: List<Int>,
    val role: String,
    val seller_count: Int,
    val update_url: String,
    val updated_at: String,
    val user: Int?,
    var isChecked : Boolean = false
)

data class Brand1(
    val active_products_count: Int,
    val code: String,
    val company: Int,
    val created_at: String,
    val id: Int,
    val image: String?,
    val name: String?,
    val cover_image: String?,
    val is_offer_available: Boolean = false,
    val update_url: String,
    val updated_at: String
)

data class CompanyUpDated(
    val address_line: Any,
    val belongs: Any,
    val city: String,
    val code: String,
    val created_at: String,
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val is_mss: Boolean,
    val is_riggle_account: Boolean,
    val is_seller: Boolean,
    val is_ss: Boolean,
    val lat: String,
    val locality: String?,
    val logo: String?,
    val long: String,
    val name: String,
    val pincode: String,
    val short_address: String,
    val state: String,
    val type: String,
    val update_url: String,
    val updated_at: String
)
