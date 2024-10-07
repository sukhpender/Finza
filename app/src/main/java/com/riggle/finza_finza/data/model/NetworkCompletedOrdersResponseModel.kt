package com.riggle.finza_finza.data.model

data class NetworkCompletedOrdersResponseModel(
    val assigned_runner: AssignedRunner3,
    val buyer: Buyer3,
    val cart_user: CartUser3,
    val code: String,
    val created_at: String,
    val delivery_date: String,
    val final_amount: Double,
    val id: Int,
    val pending_amount: Double,
    val seller: Seller3,
    val statuses: List<Statuse3>,
    val updated_at: String
)

data class AssignedRunner3(
    val first_name: String
)

data class Buyer3(
    val full_address: String,
    val name: String
)

data class CartUser3(
    val first_name: String
)

data class Seller3(
    val name: String
)

data class Statuse3(
    val created_at: String,
    val id: Int,
    val location: String,
    val order: Int,
    val remark: String,
    val status: String,
    val updated_at: String,
    val user: Int?,
    val visit_date: String
)