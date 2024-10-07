package com.riggle.finza_finza.data.model

data class HomeSKUsResponseModel(
    val selling_skus: List<SellingSku2>,
    val subcategory_analysis: SubcategoryAnalysis
)

data class SellingSku2(
    val product__brand__name: String,
    val product__name: String,
    val product_id: Int,
    val product_moq: Double,
    val quantity: Double,
    val revenue: Double
)

data class SubcategoryAnalysis(
    val order_value: Double,
    val sub_categories: List<SubCategory1>
)

data class SubCategory1(
    val percentage_revenue: Float?,
    val total_revenue: Float?,
    val sub_category: String,
    var color: String? = "#dedbd5"

    )