package com.riggle.plug.data.model

data class BrandProductResponseModel(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<ProductResult>?
)

data class ProductResult(
    val banner_image: BannerImage?,
    val category: String?,
    val id: Int?,
    val product: Product?,
    val product_aim: List<String>?
)

data class BannerImage(
    val image: String?
)

data class Product(
    val base_quantity: Double?,
    val base_unit: String?,
    val description: String?,
    val expiry_in: Int?,
    val expiry_unit: String?,
    val gst: Double?,
    val hsn_code: Long?,
    val id: Int?,
    val is_active: Boolean?,
    val mrp: Double?,
    val name: String?,
    val image: String?,
    val sub_category: SubCategory?,
    val update_url: String?
)

data class SubCategory(
    val belongs: Belongs?,
    val created_at: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val product_count: Int?,
    val type: String?,
    val update_url: String?,
    val updated_at: String?
)

data class Belongs(
    val belongs: Int?,
    val created_at: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val product_count: Int?,
    val type: String?,
    val update_url: String?,
    val updated_at: String?
)


/*
data class BrandProductResponseModel(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ProductResult>
)

data class ProductResult(
    val banner_image: String?,
    val category: String,
    val id: Int,
    val product: Product,
    val product_aim: String?
)

data class Product(
    val base_quantity: Double,
    val base_unit: String,
    val description: String,
    val expiry_in: Int,
    val expiry_unit: String,
    val gst: Double,
    val hsn_code: Int,
    val id: Int,
    val is_active: Boolean,
    val mrp: Double,
    val name: String,
    val sub_category: SubCategory,
    val update_url: String
)

data class SubCategory(
    val belongs: Belongs,
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val product_count: Int,
    val type: String,
    val update_url: String,
    val updated_at: String
)

data class Belongs(
    val belongs: Any,
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val product_count: Int,
    val type: String,
    val update_url: String,
    val updated_at: String
)*/
