package com.riggle.plug.data.model

data class FinzaLoginResponseModel(
    val `data`: FinzaLoginData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class FinzaLoginData(
    val aadhar_back_photo: String,
    val aadhar_front_photo: String,
    val aadhar_no: String,
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
    val nationality: Any,
    val pancard_no: String,
    val pancard_photo: String,
    val phone_number: String,
    val pin_code: String,
    val profile_image: String,
    val project_detail: ProjectDetail,
    val project_name: String,
    val role_id: Int,
    val designation: String,
    val state: String,
    val toll_plaza_name: String,
    val updated_at: String,
    val username: Any
)

data class ProjectDetail(
    val created_at: String,
    val id: Int,
    val price: String,
    val tag_assigned_charges: String,
    val title: String,
    val type: String,
    val updated_at: String
)