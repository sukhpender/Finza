package com.riggle.plug.data.model


data class CpStockResponseItem(
    val company: Int?,
    val cp_count: Int?,
    val created_at: String?,
    val distributor_margin: Double?,
    val distributor_moq: Double?,
    val distributor_name: Any?,
    val distributor_rate: Double?,
    val final_quantity: Int?,
    val id: Int?,
    val is_active: Boolean?,
    val is_distributor: Boolean?,
    val is_ss: Boolean?,
    val is_wholesaler: Boolean?,
    val product: ProductCpStock?,
    val profit: Double?,
    val purchase_moq: Double?,
    val purchase_price: Double?,
    val purchase_quantity: Int?,
    var quantity: Int?,
    var moq: Int?,
    val retailer_margin: Double?,
    val retailer_moq: Double?,
    val retailer_name: Any?,
    val retailer_rate: Double?,
    val sales_quantity: Int?,
    val selling_moq: Double?,
    val selling_price: Double?,
    val ss_margin: Double?,
    val ss_moq: Double?,
    val ss_name: Any?,
    val ss_rate: Double?,
    val update_url: String?,
    val updated_at: String?,
    val wholesaler_margin: Double?,
    val wholesaler_moq: Double?,
    val wholesaler_name: Any?,
    val wholesaler_rate: Double?
)

data class ProductCpStock(
    val base_quantity: Int?,
    val base_unit: String?,
    val brand: Brand?,
    val cess: Any?,
    val code: String?,
    val coins: Int?,
    val combo: Any?,
    val created_at: String?,
    val delivery_tat_days: Int?,
    val description: String?,
    val expiry_in: Int?,
    val expiry_unit: String?,
    val gst: Int?,
    val hsn_code: Int?,
    val id: Int?,
    val is_active: Boolean?,
    val is_combo_available: Boolean?,
    val is_listing_enabled: Boolean?,
    val mrp: Double?,
    val name: String?,
    val normalized_weight: Double?,
    val sub_category: Int?,
    val update_url: String?,
    val updated_at: String?
)
