package com.riggle.plug.data.model

data class CpInsightsResponseModel(
    val average_order_value: List<AverageOrderValueCpInsights>?,
    val buyer_type: String,
    val cancelled_orders_percentage: Double,
    val cities: List<Any>,
    val company: CompanyCpInsights,
    val delivered_orders_percentage: Double,
    val leaderboard: List<LeaderboardCpInsights>,
    val low_selling_sku: List<LowSellingSkuCpInsights>,
    val pending_orders_percentage: Double,
    val selling_skus: List<SellingSkuCpInsights>,
    val states: List<Any>,
    val top_selling_sku: List<TopSellingSkuCpInsights>,
    val total_orders_count: List<TotalOrdersCountCpInsights>,
    val total_revenues: List<TotalRevenueCpInsights>
)

data class AverageOrderValueCpInsights(
    val month_name: String,
    val revenue: Double
)

data class CompanyCpInsights(
    val active_brands: Int,
    val address_line: String,
    val beats: List<Any>,
    val belongs: List<Int>,
    val channel_id: Any,
    val city: String,
    val code: String,
    val created_at: String,
    val direct_channels: Int,
    val full_address: String,
    val id: Int,
    val inactive_brands: Int,
    val indirect_channels: Int,
    val industry: Any,
    val is_active: Boolean,
    val is_approved: Boolean,
    val is_file_uploaded: Boolean,
    val is_riggle_account: Boolean,
    val is_token_available: Boolean,
    val lat: Any,
    val locality: String,
    val logo: Any,
    val long: Any,
    val mobile: String,
    val my_brand_count: Int,
    val name: String,
    val number_of_distributors: Int,
    val number_of_retailers: Int,
    val number_of_salesman: Int,
    val order_emails: Any,
    val pending_amount: Int,
    val pincode: String,
    val play_accounts: Any,
    val plug_accounts: Any,
    val purchase_cancelled_orders: Int,
    val purchase_completed_orders: Int,
    val purchase_confirmed_orders: Int,
    val purchase_outstanding_orders: Int,
    val purchase_pending_orders: Int,
    val rejected_retailer: Boolean,
    val reminder_message: List<String>,
    val retailer_approved: Boolean,
    val runner_accounts: Any,
    val salesman_accounts: Any,
    val seller_brand_count: Int,
    val short_address: String,
    val state: String,
    val total_allowed_accounts: Any,
    val total_cancelled_orders: Int,
    val total_completed_orders: Int,
    val total_confirmed_orders: Int,
    val total_outstanding_orders: Int,
    val total_pending_orders: Int,
    val type: String,
    val update_url: String,
    val updated_at: String
)

data class LeaderboardCpInsights(
    val city: String,
    val name: String,
    val pincode: String,
    val state: String,
    val total_revenue: Double
)

data class LowSellingSkuCpInsights(
    val product__brand__name: String,
    val product__name: String,
    val revenue: Double
)

data class SellingSkuCpInsights(
    val product__brand__name: String,
    val product__name: String,
    val product_id: Int,
    val product_moq: Double,
    val quantity: Double,
    val revenue: Double
)

data class TopSellingSkuCpInsights(
    val product__brand__name: String,
    val product__name: String,
    val revenue: Double
)

data class TotalOrdersCountCpInsights(
    val month_name: String,
    val orders_count: Int
)

data class TotalRevenueCpInsights(
    val month_name: String,
    val revenue: Double
)