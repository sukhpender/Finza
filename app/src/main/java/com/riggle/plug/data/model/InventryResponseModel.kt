package com.riggle.plug.data.model

data class InventryResponseModel(
    val `data`: List<InventryData>,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class InventryData(
    val assigned_by: AssignedBy,
    val assigned_to: AssignedTo,
    val created_at: String,
    val id: Int,
    val inventory: Inventory,
    val inventory_id: Int,
    val is_accepted: Int,
    val is_installed: Int,
    val status: Int,
    val updated_at: String
)

data class AssignedBy(
    val email: String,
    val first_name: String,
    val id: Int
)

data class AssignedTo(
    val aadhar_back_photo: Any,
    val aadhar_front_photo: Any,
    val aadhar_no: Any,
    val address: String,
    val city: String,
    val created_at: String,
    val deleted_at: Any,
    val email: String,
    val email_verified_at: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val nationality: Any,
    val phone_number: String,
    val pin_code: String,
    val profile_image: String,
    val project_id: Any,
    val role_id: Int,
    val state: String,
    val status: Int,
    val toll_plaza_id: Any,
    val updated_at: String,
    val username: Any
)

data class Inventory(
    val bar_code: String,
    val created_at: Any,
    val epc_code: String,
    val id: Int,
    val is_registered: Int,
    val provider: Any,
    val secret_code: String,
    val status: Int,
    val tag_class: Int,
    val tag_id: Int,
    val tag_id2: Int,
    val tid: String,
    val updated_at: Any
)