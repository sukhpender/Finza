package com.riggle.finza_finza.data.model

data class UploadDocResponseModel(
    val `data`: UploadDocResponseData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class UploadDocResponseData(
    val audit_id: String,
    val created_at: String,
    val id: Int,
    val image: String,
    val type: String,
    val updated_at: String,
    val user_id: Int
)