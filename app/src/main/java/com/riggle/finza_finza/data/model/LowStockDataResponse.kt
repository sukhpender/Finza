package com.riggle.finza_finza.data.model

data class LowStockDataResponse(
    val graph_data: List<GraphData>?,
    val list_data: List<LowStockData>?
)

data class GraphData(
    val current_stock: Double?,
    val expected_stock: Double?,
    val msl: Double?,
    val name: String?,
    val percentage: Float?,
    val purchase_order: Double?,
    val sales_order: Double?,
    val status: String?,
    var color: String = "#2563EB",
    val total_sales: Double?
)

data class LowStockData(
    val current_stock: Double?,
    val expected_stock: Double?,
    val msl: Double?,
    val name: String?,
    val percentage: Double?,
    val purchase_order: Double?,
    val sales_order: Double?,
    val status: String?,
    val total_sales: Double?
)