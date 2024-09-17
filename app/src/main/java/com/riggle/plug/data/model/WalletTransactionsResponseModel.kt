package com.riggle.plug.data.model

data class WalletTransactionsResponseModel(
    val `data`: List<WalletTransactionsData>,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class WalletTransactionsData(
    val acquirer_data: AcquirerData1,
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
    val id: Int,
    val international: Boolean,
    val invoice_id: Any,
    val method: String,
    val notes: List<Any>,
    val order_id: String,
    val payment_id: String,
    val razorpay_created_at: String,
    val refund_status: Any,
    val status: String,
    val tax: Int,
    val updated_at: String,
    val user_id: Int,
    val vpa: String,
    val wallet: Any
)

data class AcquirerData1(
    val rrn: String
)