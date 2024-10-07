package com.riggle.finza_finza.data.model

data class BeatInsightsResponseModel(
    val average_order_value: List<AverageOrderValue>?,
    val cancelled_orders_percentage: Double,
    val cities: List<String>,
    val company: CompanyBeatInsights,
    val delivered_orders_percentage: Double,
    val leaderboard: List<Leaderboard>,
    val low_selling_sku: List<LowSellingSku>,
    val number_of_retailers: List<NumberOfRetailer>,
    val order_frequency: List<OrderFrequency>,
    val pending_orders_percentage: Double,
    val pincodes: List<String>,
    val selling_sku: List<SellingSku>,
    val states: List<String>,
    val top_selling_sku: List<TopSellingSku>,
    val total_orders_count: List<TotalOrdersCount>,
    val total_revenues: List<TotalRevenue>
)

data class AverageOrderValue(
    val month_name: String,
    val revenue: Double
)

data class CompanyBeatInsights(
    val active_brands: Int,
    val address_line: String,
    val beats: List<BeatBeatInsights>,
    val belongs: List<Any>,
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
    val lat: String,
    val locality: String,
    val logo: String,
    val long: String,
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
    val reminder_message: Any,
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

data class Leaderboard(
    val city: String,
    val name: String,
    val pincode: String,
    val state: String,
    val total_revenue: Double
)

data class LowSellingSku(
    val product__brand__name: String,
    val product__name: String,
    val revenue: Double
)

data class NumberOfRetailer(
    val month_name: String,
    val number_of_retailers: Int
)

data class OrderFrequency(
    val count: Int,
    val day: String,
    val revenue: Int
)

data class SellingSku(
    val product__brand__name: String,
    val product__name: String,
    val revenue: Double
)

data class TopSellingSku(
    val product__brand__name: String,
    val product__name: String,
    val revenue: Double
)

data class TotalOrdersCount(
    val month_name: String,
    val orders_count: Int
)

data class TotalRevenue(
    val month_name: String,
    val revenue: Double
)

data class BeatBeatInsights(
    val id: Int,
    val name: String,
    val update_url: String
)