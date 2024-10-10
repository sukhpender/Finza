package com.riggle.finza_finza.data.model

data class CreateCustomerRew(
    val createCustomerReq: CustomerRequest
)

data class CustomerRequest(
    val reqWallet: RequestWallet,
    val custDetails: CustomerDetails,
    val provider: String,
    val inventory_id: String
)

data class RequestWallet(
    val requestId: String,
    val sessionId: String,
    val channel: String,
    val agentId: String,
    val reqDateTime: String,
    val provider: String
)

data class CustomerDetails(
    val name: String,
    val lastName: String,
    val mobileNo: String,
    val dob: String,
    val doc: List<Document>,
    val udf1: String,
    val udf2: String,
    val udf3: String,
    val udf4: String,
    val udf5: String
)

data class Document(
    val docType: String,
    val docNo: String,
    val expiryDate: String = ""
)