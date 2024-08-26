package com.riggle.plug.data.model

/*
data class SalesSKUsResponseModel(
    val product__brand__name: String,
    val product__name: String,
    val product_id: Int,
    val product_moq: Double,
    val quantity: Double,
    val revenue: Double
)
*/

data class SalesSKUsResponseModel(
    val selling_skus: List<SellingSku1>
)

data class SellingSku1(
    val product__brand__name: String,
    val product__name: String,
    val product_id: Int,
    val product_moq: Double,
    val quantity: Double,
    val revenue: Double
)