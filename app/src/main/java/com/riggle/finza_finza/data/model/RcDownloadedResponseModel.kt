package com.riggle.finza_finza.data.model

data class RcDownloadedResponseModel(
    val `data`: RcDownloadedData,
    val success: Boolean
)

data class RcDownloadedData(
    val current_page: Int,
    val `data`: List<RcDownloadedDataX>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<RcDownloadedLink>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class RcDownloadedDataX(
    val amount: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val status: Int,
    val transaction_id: Any,
    val updated_at: String,
    val user_id: Int
)

data class RcDownloadedLink(
    val active: Boolean,
    val label: String,
    val url: String
)