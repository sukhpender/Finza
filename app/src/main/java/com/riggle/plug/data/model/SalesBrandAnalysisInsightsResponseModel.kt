package com.riggle.plug.data.model

data class SalesBrandAnalysisInsightsResponseModel(
    val average_order_value: List<AverageOrderValue1>,
    val number_of_retailers: List<NumberOfRetailer1>,
    val plug_brands: List<PlugBrand>,
    val total_orders_count: List<TotalOrdersCount1>,
    val total_revenues: List<TotalRevenue1>
)

data class AverageOrderValue1(
    val month_name: String,
    val revenue: Int
)

data class NumberOfRetailer1(
    val month_name: String,
    val number_of_retailers: Int
)

data class PlugBrand(
    val brand__id: Int,
    val brand__name: String
)

data class TotalOrdersCount1(
    val month_name: String,
    val orders_count: Int
)

data class TotalRevenue1(
    val month_name: String,
    val revenue: Int
)