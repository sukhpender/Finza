package com.riggle.finza_finza.data.model

data class FinzaForgotPassResponseModel(
    val `data`: Data,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class ForgotPasswordData(
    val address: String,
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