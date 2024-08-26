package com.riggle.plug.data.model

data class HomeInsightsOrderResponseModel(
    val network: Network?,
    val self: Self
)

data class Network(
    val cancelled: Cancelled?,
    val completed: Completed?,
    val confirmed: Confirmed?,
    val delivered: Delivered?,
    val pending: Pending1?
)

data class Self(
    val cancelled: CancelledX,
    val completed: CompletedX,
    val confirmed: ConfirmedX,
    val delivered: DeliveredX,
    val pending: Pending1
)

data class Cancelled(
    val cancelled_delivery_ratio: Double?,
    val cancelled_invoices: Int,
    val cancelled_orders_count: Int,
    val cancelled_orders_values: Int
)

data class Completed(
    val completed_delivery_ratio: Double?,
    val completed_invoices: Int,
    val completed_orders_count: Int,
    val completed_orders_values: Double
)

data class Confirmed(
    val confirmed_delivery_ratio: Double?,
    val confirmed_invoices: Int,
    val confirmed_orders_count: Int,
    val confirmed_orders_values: Double
)

data class Delivered(
    val delivered_delivery_ratio: Double?,
    val delivered_invoices: Int,
    val delivered_orders_count: Int,
    val delivered_orders_values: Double
)

data class Pending1(
    val pending_delivery_ratio: Double?,
    val pending_invoices: Int,
    val pending_orders_count: Int,
    val pending_orders_values: Double
)

data class CancelledX(
    val cancelled_delivery_ratio: Int?,
    val cancelled_invoices: Int,
    val cancelled_orders_count: Int,
    val cancelled_orders_values: Int
)

data class CompletedX(
    val completed_delivery_ratio: Int?,
    val completed_invoices: Int,
    val completed_orders_count: Int,
    val completed_orders_values: Int
)

data class ConfirmedX(
    val confirmed_delivery_ratio: Int?,
    val confirmed_invoices: Int,
    val confirmed_orders_count: Int,
    val confirmed_orders_values: Int
)

data class DeliveredX(
    val delivered_delivery_ratio: Int?,
    val delivered_invoices: Int,
    val delivered_orders_count: Int,
    val delivered_orders_values: Int
)

data class PendingX(
    val pending_delivery_ratio: Int?,
    val pending_invoices: Int,
    val pending_orders_count: Int,
    val pending_orders_values: Int
)