package com.riggle.finza_finza.data.model

data class FinzaUpdateProfileResponseModel(
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
    val username: String?
)