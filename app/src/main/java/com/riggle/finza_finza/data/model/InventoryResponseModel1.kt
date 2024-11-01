package com.riggle.finza_finza.data.model

data class InventoryResponseModel1(
    val `data`: InventoryResponseData, val message: String, val status: String, val statusCode: Int
)

data class InventoryResponseData(
    val current_page: Int,
    val next_page_url: String?,
    val path: String?,
    val per_page: Int,
    val prev_page_url: String?,
    val to: Int?,
    val total: Int?,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val `data`: List<InventoryResponseDataX>
)
data class Link(
    val active: Boolean,
    val label: String,
    val url: String
)

data class InventoryResponseDataX(
    var isSelected: Boolean = false,
    val age_in_days: Int,
    val assigned_amount: Int?,
    val assigned_by: InventoryResponseAssignedBy,
    val assigned_date: String,
    val assigned_to: InventoryResponseAssignedTo,
    val created_at: String,
    val hold_amount: Any,
    val id: Int,
    val inventory: Inventory1,
    val inventory_id: Int,
    val is_accepted: Int,
    val is_installed: Int,
    val status: Int,
    val updated_at: String
)

data class InventoryResponseAssignedBy(
    val aadhar_back_photo: String,
    val aadhar_front_photo: String,
    val aadhar_no: String,
    val address: String,
    val city: String,
    val created_at: String,
    val deleted_at: Any,
    val designation: String,
    val email: String,
    val email_verified_at: String,
    val first_name: String,
    val gender: String,
    val get_toll: GetToll,
    val get_user_role: GetUserRole,
    val id: Int,
    val last_name: String,
    val nationality: String,
    val otp: Any,
    val pancard_no: String,
    val pancard_photo: String,
    val phone_number: String,
    val pin_code: String,
    val profile_image: Any,
    val project_id: Any?,
    val role_id: Int,
    val state: String,
    val status: Int,
    val toll_plaza_id: Int?,
    val toll_plaza_name: String,
    val updated_at: String,
    val username: Any
)

data class InventoryResponseAssignedTo(
    val aadhar_back_photo: String,
    val aadhar_front_photo: String,
    val aadhar_no: String,
    val address: String,
    val city: String,
    val created_at: String,
    val deleted_at: Any,
    val designation: String,
    val email: String,
    val email_verified_at: Any,
    val first_name: String,
    val gender: String,
    val get_toll: GetToll,
    val get_user_role: GetUserRole,
    val id: Int,
    val last_name: String,
    val nationality: String,
    val otp: Any,
    val pancard_no: String,
    val pancard_photo: String,
    val phone_number: String,
    val pin_code: String,
    val profile_image: Any,
    val project_id: Int?,
    val role_id: Int,
    val state: String,
    val status: Int,
    val toll_plaza_id: Int,
    val toll_plaza_name: String,
    val updated_at: String,
    val username: Any
)

data class Inventory1(
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

data class GetToll(
    val created_at: Any,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val plaza_city: String,
    val plaza_id: String,
    val plaza_name: String,
    val plaza_state: String,
    val plaza_type: String,
    val updated_at: Any
)

data class GetUserRole(
    val created_at: String, val id: Int, val title: String, val updated_at: String
)