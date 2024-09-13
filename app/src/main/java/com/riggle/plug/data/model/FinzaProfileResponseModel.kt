package com.riggle.plug.data.model

data class FinzaProfileResponseModel(
    val `data`: FinzaProfileModel,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class FinzaProfileModel(
    val address: String,
    val auth_token: String,
    val city: String,
    val created_at: String,
    val email: String,
    val email_verified_at: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val phone_number: String,
    val pin_code: String,
    val profile_image: String?,
    val role_id: Int,
    val state: String,
    val updated_at: String,
    val username: Any
)