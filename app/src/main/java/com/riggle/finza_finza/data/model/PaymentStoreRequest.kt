package com.riggle.finza_finza.data.model

data class PaymentStoreRequest(
    val acquirer_data: AcquirerData,
    val amount: Int,
    val amount_refunded: Int,
    val bank: Any,
    val captured: Boolean,
    val card_id: Any,
    val contact: String,
    val created_at: String,
    val currency: String,
    val customer_id: String,
    val description: String,
    val email: String,
    val entity: String,
    val error_code: Any,
    val error_description: Any,
    val error_reason: Any,
    val error_source: Any,
    val error_step: Any,
    val fee: Int,
    val id: String,
    val international: Boolean,
    val invoice_id: Any,
    val method: String,
    val notes: List<Any>,
    val order_id: String,
    val refund_status: Any,
    val status: String,
    val tax: Int,
    val vpa: String,
    val wallet: Any
)

data class AcquirerData(
    val rrn: String
)