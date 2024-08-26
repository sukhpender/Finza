package com.riggle.plug.data.model

data class NetworkCancelledOrdersResponseModel(
    val assigned_runner: AssignedRunner4,
    val buyer: Buyer4,
    val cart_user: CartUser4,
    val code: String,
    val created_at: String,
    val delivery_date: String,
    val final_amount: Double,
    val id: Int,
    val pending_amount: Double,
    val seller: Seller4,
    val statuses: List<Statuse4>,
    val updated_at: String
)

data class AssignedRunner4(
    val first_name: String
)

data class Buyer4(
    val full_address: String,
    val name: String
)

data class CartUser4(
    val first_name: String
)

data class Seller4(
    val name: String
)

data class Statuse4(
    val created_at: String,
    val id: Int,
    val location: String,
    val order: Int,
    val remark: String,
    val status: String,
    val updated_at: String,
    val user: Int,
    val visit_date: Any
)