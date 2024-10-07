package com.riggle.finza_finza.data.model
data class IssueTagCheckWalletResponseModel(
    val `data`: IssueTagCheckWalletData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class IssueTagCheckWalletData(
    val balance: String,
    val created_at: String,
    val id: Int,
    val inventory_balance: String,
    val razorpay_customer_id: String,
    val updated_at: String,
    val user_id: Int
)
