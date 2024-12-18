package com.riggle.finza_finza.data.model

data class NetworkOrderDetailsResponseModel(
    val actual_delivery_date: Any,
    val add_on_mode: Any,
    val add_on_remark: Any,
    val add_on_type: Any,
    val assigned_runner: Any,
    val assured_retailer_coins: Int,
    val brand: Any,
    val buyer: BuyerNetworkOrderDetails,
    val buyer_gst: Any?,
    val buyer_outstanding_amount: Int,
    val buyers_orders_pending_amount: Double,
    val cart_user: CartUserNetworkOrderDetails,
    val challan_file: Any,
    val code: String,
    val coin_multiplier: Int,
    val created_at: String,
    val credit_otp: String,
    val delivery_date: String,
    val earned_coins: Int,
    val expected_payment_mode: Any,
    val final_amount: Double?,
    val final_bill_value: Double,
    val gst_invoice: Any?,
    val gst_invoice_number: Any?,
    val id: Int,
    val invoice_challan: Any,
    val invoice_no: Any,
    val is_cart: Boolean,
    val is_credit_enabled: Boolean,
    val is_new_order: Boolean,
    val paid_amount: Double,
    val payments: Any,
    val pending_amount: Double,
    val product_amount: Double,
    val products: List<ProductNetworkOrderDetails>,
    val redeemed_coins: Int,
    val remarks: Any,
    val revisit_date: Any,
    val rsa_earned_coins: Int,
    val seller: SellerNetworkOrderDetails,
    val seller_earned_coins: Int,
    val seller_referral_code: Any,
    val status: String,
    val statuses: List<StatuseNetworkOrderDetails>,
    val storage_user: Any,
    val total_cart_discount_amount: Double,
    val total_discount_amount: Double,
    val total_product_discount_amount: Double,
    val unloading_time: Any,
    val update_url: String,
    val updated_at: String,
    val uploaded_challan: Any
)

data class BuyerNetworkOrderDetails(
    val admin: AdminNetworkOrderDetails,
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val lat: String,
    val logo: Any,
    val long: String,
    val name: String,
    val pincode: String,
    val update_url: String
)

data class CartUserNetworkOrderDetails(
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class ProductNetworkOrderDetails(
    val add_on_mode: Any,
    val add_on_remark: Any,
    val add_on_type: Any,
    val amount: Double,
    val coins: Int,
    val created_at: String,
    val discount_amount: Double,
    val free_product: Any,
    val free_product_quantity: Int,
    val id: Int,
    val is_free_product: Boolean,
    val loaded_quantity: Int,
    val order: Int,
    val ordered_quantity: Int,
    val original_rate: Double,
    val product: ProductXNetworkOrderDetails,
    val product_combo: Any,
    val product_discount_amount: Double,
    val product_moq: Int,
    val profit: Double,
    val quantity: Int,
    val rate: Double,
    val received_quantity: Int,
    val temp_retailer_seller: Int,
    val update_url: String,
    val updated_at: String
)

data class SellerNetworkOrderDetails(
    val admin: Admin,
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val lat: Any,
    val logo: Any,
    val long: Any,
    val name: String,
    val pincode: String,
    val update_url: String
)

data class StatuseNetworkOrderDetails(
    val created_at: String,
    val id: Int,
    val location: String,
    val order: Int,
    val remark: Any,
    val status: String,
    val updated_at: String,
    val user: Int,
    val visit_date: Any
)

data class AdminNetworkOrderDetails(
    val email: Any,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: Any,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class ProductXNetworkOrderDetails(
    val banner_image: BannerImage,
    val base_quantity: Double,
    val base_unit: String,
    val brand: Int,
    val cess: Any,
    val code: String,
    val coins: Int,
    val color: Any,
    val combo: Any,
    val created_at: String,
    val delivery_tat_days: Int,
    val description: String,
    val expiry_in: Int,
    val expiry_unit: String,
    val favorite_users: List<Int>,
    val gst: Double?,
    val hsn_code: Int,
    val id: Int,
    val is_active: Boolean,
    val is_combo_available: Boolean,
    val is_listing_enabled: Boolean,
    val is_quick_product: Boolean,
    val mrp: Double,
    val name: String,
    val normalized_weight: Double,
    val retailer_rate: Double,
    val size: Any,
    val sub_category: Int,
    val update_url: String,
    val updated_at: String
)

/*

data class NetworkOrderDetailsResponseModel(
    val actual_delivery_date: String?,
    val add_on_mode: Any,
    val add_on_remark: Any,
    val add_on_type: Any,
    val assigned_runner: Any,
    val assured_retailer_coins: Int,
    val brand: Any,
    val buyer: Buyer5,
    val seller: Seller5?,
    val buyer_gst: String?,
    val buyer_outstanding_amount: Int,
    val cart_user: CartUser5,
    val challan_file: Any,
    val code: String,
    val coin_multiplier: Int,
    val created_at: String,
    val credit_otp: String,
    val delivery_date: String,
    val earned_coins: Int,
    val expected_payment_mode: String,
    val final_amount: Double,
    val gst_invoice: Any?,
    val gst_invoice_number: Any?,
    val id: Int,
    val invoice_challan: Any,
    val invoice_no: Any,
    val is_cart: Boolean,
    val is_credit_enabled: Boolean,
    val paid_amount: Double,
    val payments: Any,
    val pending_amount: Double,
    val product_amount: Double,
    val products: List<Product5>,
    val redeemed_coins: Int,
    val remarks: Any,
    val revisit_date: Any,
    val rsa_earned_coins: Int,
    val seller_earned_coins: Int,
    val seller_referral_code: Any,
    val status: String,
    val statuses: List<Statuse5>,
    val storage_user: Any,
    val total_cart_discount_amount: Double,
    val total_discount_amount: Double,
    val total_product_discount_amount: Double,
    val unloading_time: Any,
    val update_url: String,
    val updated_at: String,
    val uploaded_challan: Any
)

data class Buyer5(
    val admin: Admin5,
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val lat: String,
    val logo: String,
    val long: String,
    val name: String,
    val pincode: String,
    val update_url: String
)

data class Seller5(
    val full_address: String,
    val name: String,
)

data class CartUser5(
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class Product5(
    val add_on_mode: Any,
    val add_on_remark: Any,
    val add_on_type: Any,
    val amount: Double,
    val coins: Int,
    val created_at: String,
    val discount_amount: Double,
    val free_product: Any,
    val free_product_quantity: Int,
    val id: Int,
    val is_free_product: Boolean,
    val loaded_quantity: Int,
    val order: Int,
    val ordered_quantity: Int,
    val original_rate: Double,
    val product: ProductX,
    val product_combo: Any,
    val product_discount_amount: Double,
    val product_moq: Int,
    val profit: Double,
    val quantity: Int,
    val rate: Double,
    val received_quantity: Int,
    val temp_retailer_seller: Int,
    val update_url: String,
    val updated_at: String
)

data class Statuse5(
    val created_at: String,
    val id: Int,
    val location: String,
    val order: Int,
    val remark: Any,
    val status: String,
    val updated_at: String,
    val user: Int,
    val visit_date: Any
)

data class Admin5(
    val email: Any,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: Any,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class ProductX(
    val banner_image: Any,
    val base_quantity: Double,
    val base_unit: String,
    val brand: Int,
    val cess: Any,
    val code: String,
    val coins: Int,
    val color: Any,
    val combo: Any,
    val created_at: String,
    val delivery_tat_days: Int,
    val description: String,
    val expiry_in: Int,
    val expiry_unit: String,
    val favorite_users: List<Int>,
    val gst: Double?,
    val hsn_code: Int,
    val id: Int,
    val is_active: Boolean,
    val is_combo_available: Boolean,
    val is_listing_enabled: Boolean,
    val is_quick_product: Boolean,
    val mrp: Double,
    val name: String?,
    val normalized_weight: Double,
    val retailer_rate: Double,
    val size: Any,
    val sub_category: Int,
    val update_url: String,
    val updated_at: String
)*/
