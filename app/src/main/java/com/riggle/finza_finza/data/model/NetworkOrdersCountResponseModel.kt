package com.riggle.finza_finza.data.model

data class NetworkOrdersCountResponseModel(
    val all_orders: Int,
    val cancelled_orders: Int,
    val completed_orders: Int,
    val confirmed_orders: Int,
    val network_all_orders: Int,
    val network_cancelled_orders: Int,
    val network_completed_orders: Int,
    val network_confirmed_orders: Int,
    val network_outstanding_orders: Int,
    val network_pending_orders: Int,
    val outstanding_orders: Int,
    val pending_orders: Int
)