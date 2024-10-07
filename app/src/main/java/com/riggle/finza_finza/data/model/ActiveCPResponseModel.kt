package com.riggle.finza_finza.data.model

data class ActiveCPResponseModel(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<ResultActiveCP>
)

data class ResultActiveCP(
    val address_line: String,
    val admin: AdminActiveCP,
    val channel_id: String?,
    val city: String,
    val extra: ExtraActiveCP,
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val locality: String,
    val logo: Any,
    val name: String,
    val pincode: String,
    val short_address: String,
    val state: String,
    val update_url: String
)

data class AdminActiveCP(
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: Any,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class ExtraActiveCP(
    val fssai: String,
    val fssai_expiry_date: String,
    val gst_number: String,
    val id: Int,
    val pan_number: String
)