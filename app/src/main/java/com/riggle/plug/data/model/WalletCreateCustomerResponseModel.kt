package com.riggle.plug.data.model

data class WalletCreateCustomerResponseModel(
    val `data`: WalletCreateCustomerData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class WalletCreateCustomerData(
    val created_at: String,
    val email: String,
    val entity: String,
    val gstin: String?,
    val id: Int,
    val name: String,
    val notes: String,
    val razorpay_customer_id: String,
    val updated_at: String,
    val user_id: Int
)