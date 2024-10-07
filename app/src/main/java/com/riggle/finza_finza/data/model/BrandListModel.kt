package com.riggle.finza_finza.data.model

data class BrandListModel(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)

data class Result(
    val brand: Brand,
    val id: Int
)

data class Brand(
    val id: Int,
    val image: String,
    val name: String
)