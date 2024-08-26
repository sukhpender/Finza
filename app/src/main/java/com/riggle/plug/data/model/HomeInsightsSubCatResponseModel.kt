package com.riggle.plug.data.model

data class HomeInsightsSubCatResponseModel(
    val subcategory_analysis: SubcategoryAnalysis1
)

data class SubcategoryAnalysis1(
    val order_value: Double,
    val sub_categories: List<SubCategory2>
)

data class SubCategory2(
    val percentage_revenue: Float,
    val sub_category: String,
    val total_revenue: Double,
    var color: String? = "#dedbd5"

)