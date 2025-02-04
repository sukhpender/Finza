package com.riggle.finza_finza.data.model

data class DownloadDocResponseModel(
    val pdf_url: String,
    val error: String,
    val success: Boolean
)