package com.riggle.plug.data.model

data class CityBeatsResponseModel(
    val count: Int,
    val next: String,
    val previous: String?,
    val results: List<ResultCityBeats>
)

data class ResultCityBeats(
    val company: Int,
    val created_at: String,
    val direct_reportees: List<Any>,
    val id: Int,
    val name: String,
    val number_of_retailers: Int,
    val pincodes: List<Int>,
    val retailers: List<Int>,
    val salesmans: List<SalesmanCityBeats>,
    val update_url: String,
    val updated_at: String
)

data class SalesmanCityBeats(
    val full_name: String,
    val manager_full_name: String
)