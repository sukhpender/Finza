package com.riggle.plug.data.model

data class CheckTagAvailabilityResponseModel(
    val `data`: CheckTagAvailabilityData,
    val message: String,
    val status: String,
    val statusCode: Int
)

class CheckTagAvailabilityData