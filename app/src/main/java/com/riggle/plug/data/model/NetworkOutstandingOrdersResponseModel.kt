package com.riggle.plug.data.model

data class NetworkOutstandingOrdersResponseModel(
    val assigned_runner: AssignedRunner1,
    val buyer: Buyer1,
    val cart_user: CartUser1,
    val code: String,
    val created_at: String,
    val delivery_date: String,
    val final_amount: Double,
    val id: Int,
    val pending_amount: Double,
    val seller: Seller1,
    val statuses: List<Statuse1>,
    val updated_at: String
)

data class AssignedRunner1(
    val first_name: String
)

data class Buyer1(
    val full_address: String,
    val name: String
)

data class CartUser1(
    val first_name: String
)

data class Seller1(
    val name: String
)

data class Statuse1(
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