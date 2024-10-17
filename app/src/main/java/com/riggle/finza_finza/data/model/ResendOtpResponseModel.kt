package com.riggle.finza_finza.data.model

data class ResendOtpResponseModel(
    val `data`: ResendOtpData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class ResendOtpData(
    val aadhar_back_photo: String,
    val aadhar_front_photo: String,
    val aadhar_no: String,
    val address: String,
    val city: String,
    val created_at: String,
    val designation: String,
    val email: String,
    val email_verified_at: String,
    val first_name: String,
    val full_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val nationality: String,
    val pancard_no: String,
    val pancard_photo: String,
    val phone_number: String,
    val pin_code: String,
    val profile_image: String,
    val project_name: String,
    val role_id: Int,
    val state: String,
    val toll_plaza_name: String,
    val updated_at: String,
    val username: Any
)