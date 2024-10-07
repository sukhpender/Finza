package com.riggle.finza_finza.data.model

data class CoOwnersListResponseModel(
    val designation: String?,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val last_name: String,
    val mobile: String,
    val other_languages: List<Any>,
    val update_url: String
)