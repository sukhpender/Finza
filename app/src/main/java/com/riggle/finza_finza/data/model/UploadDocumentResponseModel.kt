package com.riggle.finza_finza.data.model

data class UploadDocumentResponseModel(
    val documentDetails: DocumentDetails,
    val response: Response4
)

data class DocumentDetails(
    val imageType: String,
    val sessionId: String
)

data class Response4(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)