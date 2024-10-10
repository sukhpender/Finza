package com.riggle.finza_finza.data.model

data class CheckTagAvailabilityResponseModel(
    val `data`: CheckTagAvailabilityData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class CheckTagAvailabilityData(
    val bar_code: String,
    val created_at: Any,
    val epc_code: String,
    val id: Int,
    val is_registered: Int,
    val issuer_key_index: String,
    val lot_number: String,
    val provider: String,
    val secret_code: String,
    val status: Int,
    val tid: String,
    val updated_at: String,
    val vehicle_class: String
)