package com.riggle.finza_finza.data.model

data class BrandRateResponseModel(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<BrandRateResult>
)

data class BrandRateResult(
    val banner_image: BannerImage,
    val brand: BrandRate,
    val cp_count: Int,
    val distributor_margin: Double,
    val distributor_moq: Int,
    val distributor_name: String,
    val distributor_rate: Double,
    val id: Int,
    val is_distributor: Boolean,
    val is_ss: Boolean,
    val is_wholesaler: Boolean,
    val product: BrandRateProduct,
    val retailer_margin: Double,
    val retailer_moq: Int,
    val retailer_rate: Double,
    val ss_margin: Double,
    val ss_moq: Int,
    val ss_name: String,
    val ss_rate: Double,
    val update_url: String,
    val wholesaler_margin: Double,
    val wholesaler_moq: Int,
    val wholesaler_name: String,
    val wholesaler_rate: Double
)

data class BrandRate(
    val id: Int
)

data class BrandRateProduct(
    val base_quantity: Double,
    val base_unit: String,
    val id: Int,
    val mrp: Double,
    val name: String
)