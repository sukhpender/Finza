package com.riggle.plug.data.model

data class FinzaLoginResponseModel(
    val `data`: FinzaLoginData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class FinzaLoginData(
    val address: String,
    val auth_token: String,
    val city: String,
    val created_at: String,
    val email: String,
    val email_verified_at: String,
    val first_name: String,
    val full_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val phone_number: String,
    val pin_code: String,
    val profile_image: Any,
    val role_id: Int,
    val state: String,
    val updated_at: String,
    val username: Any
)