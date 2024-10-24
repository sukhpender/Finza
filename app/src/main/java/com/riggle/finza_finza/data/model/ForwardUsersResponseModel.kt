package com.riggle.finza_finza.data.model

data class ForwardUsersResponseModel(
    val `data`: List<ForwardUsersData>,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class ForwardUsersData(
    val assigned_by_name: String,
    val role_name: String,
    val total_inventories: Int,
    val user_id: Int
)

data class DispatchUsersResponseModel(
    val `data`: List<DispatchUsersData>,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class DispatchUsersData(
    val assigned_to_name: String,
    val role_name: String,
    val total_inventories: Int,
    val user_id: Int
)