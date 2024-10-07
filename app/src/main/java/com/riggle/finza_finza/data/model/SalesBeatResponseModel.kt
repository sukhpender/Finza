package com.riggle.finza_finza.data.model

data class SalesBeatResponseModel(
    val count: Int,
    val next: Any?,
    val previous: Any?,
    val results: List<ResultSalesBeat>
)

data class ResultSalesBeat(
    val beats: List<BeatSalesBeat>,
    val created_at: String,
    val day: String,
    val id: Int,
    val ordering: Int,
    val update_url: String,
    val updated_at: String,
    val user: Int
)

data class BeatSalesBeat(
    val id: Int,
    val name: String,
    val number_of_retailers: Int
)