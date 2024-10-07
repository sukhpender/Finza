package com.riggle.finza_finza.data.model

data class CpRemarksResponseModel(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<ResultCpRemarks>
)

data class ResultCpRemarks(
    val company: CompanyCpRemarks,
    val created_at: String,
    val editable: Boolean,
    val id: Int,
    val image: String,
    val img_with_dist: Any,
    val is_new_log: Boolean,
    val is_new_remark: Boolean,
    val lat: Double?,
    val long: Double?,
    val no_sale_reason: Any,
    val other_img: Any,
    val remark: String,
    val timestamp: String,
    val type: String,
    val update_url: String,
    val updated_at: String,
    val user: UserCpRemarks
)

data class CompanyCpRemarks(
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val lat: Any,
    val logo: String,
    val long: Any,
    val name: String,
    val pincode: String,
    val update_url: String
)

data class UserCpRemarks(
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val last_name: String,
    val mobile: String,
    val update_url: String
)