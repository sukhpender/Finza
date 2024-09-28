package com.riggle.plug.data.model

data class UsersListResponseModel(
    val `data`: List<UsersListData>,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class UsersListData(
    val aadhar_back_photo: String?,
    val aadhar_front_photo: String?,
    val aadhar_no: String?,
    val address: String,
    val city: String,
    val toll_plaza_name: String,
    val created_at: String,
    val deleted_at: String?,
    val email: String,
    val email_verified_at: String?,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val nationality: Any,
    val phone_number: String,
    val pin_code: String,
    val profile_image: String?,
    val project_id: Any,
    val role_id: Int,
    val state: String,
    val status: Int,
    val toll_plaza_id: Int,
    val updated_at: String,
    val username: String?,
    var isUserSelected: Boolean = false
)