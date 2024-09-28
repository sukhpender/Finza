package com.riggle.plug.data.model

data class GetApprovedLeaveData(
    val applied_on: String,
    val approved_by: Any?,
    val company: Int,
    val created_at: String,
    val end_date: String,
    val id: Int,
    val manager_remarks: String?,
    val reason: String,
    val rejected_by: Any?,
    val start_date: String,
    val status: String,
    val type: String,
    val update_url: String,
    val updated_at: String,
    val user: Int
)

data class GetLeaveData(
    val applied_on: String?,
    val approved_by: User2?,
    val company: Int?,
    val created_at: String?,
    val end_date: String?,
    val id: Int?,
    val manager_remarks: String?,
    val reason: String?,
    val rejected_by: User2?,
    val start_date: String?,
    val status: String?,
    val type: String?,
    val update_url: String?,
    val updated_at: String?,
    val user: User2?)

data class User2(
    val created_at: String,
    val date_joined: String,
    val designation: Any,
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String?,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: String,
    val last_name: String,
    val manager: Any,
    val middle_name: String,
    val mobile: String,
    val permissions: List<String>,
    val role: String,
    val update_url: String,
    val updated_at: String,
    val user_id: Any,
    val username: String?
)

data class GetLeaveCountData(
    val approved: Approved?,
    val pending: Pending?,
    val rejected: Rejected?
)

data class Approved(
    val leaves: Int?,
    val ojt: Int?
)

data class Pending(
    val leaves: Int?,
    val ojt: Int?
)

data class Rejected(
    val leaves: Int?,
    val ojt: Int?
)

data class GetHRResponseList(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<GetLeaveData>
)
