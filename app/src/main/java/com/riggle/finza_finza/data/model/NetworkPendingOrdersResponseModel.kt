package com.riggle.finza_finza.data.model

data class NetworkPendingOrdersResponseModel(
    val assigned_runner: AssignedRunner,
    val buyer: Buyer,
    val cart_user: CartUser,
    val code: String,
    val created_at: String,
    val delivery_date: String,
    val final_amount: Double,
    val id: Int,
    val pending_amount: Double,
    val seller: Seller,
    val statuses: List<Statuse>,
    val updated_at: String
)

data class AssignedRunner(
    val first_name: String
)

data class Buyer(
    val full_address: String,
    val name: String
)

data class CartUser(
    val first_name: String
)

data class Seller(
    val name: String
)

data class Statuse(
    val created_at: String,
    val id: Int,
    val location: String,
    val order: Int,
    val remark: String,
    val status: String,
    val updated_at: String,
    val user: Int,
    val visit_date: String
)