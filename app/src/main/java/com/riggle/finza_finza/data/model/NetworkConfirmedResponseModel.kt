package com.riggle.finza_finza.data.model


data class NetworkConfirmedResponseModel(
    val assigned_runner: AssignedRunner2,
    val buyer: Buyer2,
    val cart_user: CartUser2,
    val code: String,
    val created_at: String,
    val delivery_date: String,
    val final_amount: Double,
    val id: Int,
    val pending_amount: Double,
    val seller: Seller2,
    val statuses: List<Statuse2>,
    val updated_at: String
)

data class AssignedRunner2(
    val first_name: String
)

data class Buyer2(
    val full_address: String,
    val name: String
)

data class CartUser2(
    val first_name: String
)

data class Seller2(
    val name: String
)

data class Statuse2(
    val created_at: String,
    val id: Int,
    val location: String,
    val order: Int,
    val remark: Any,
    val status: String,
    val updated_at: String,
    val user: Int,
    val visit_date: Any
)