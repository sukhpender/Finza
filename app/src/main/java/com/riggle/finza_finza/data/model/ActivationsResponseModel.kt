package com.riggle.finza_finza.data.model

data class ActivationsResponseModel(
    val `data`: ActivationsResponseData,
    val status: String
)

data class ActivationsResponseData(
    val current_page: Int,
    val `data`: List<ActivationsResponseData1>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<ActivationsResponseLink>,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class ActivationsResponseData1(
    val created_at: String,
    val fast_tag_no: String,
    val id: Int,
    val vehicle_no: String
)

data class ActivationsResponseLink(
    val active: Boolean,
    val label: String,
    val url: String
)