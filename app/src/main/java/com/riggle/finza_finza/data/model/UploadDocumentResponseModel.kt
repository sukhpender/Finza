package com.riggle.finza_finza.data.model

data class Response4(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)

data class UploadDocumentResponseModel(
    val `data`: Data6,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class Data6(
    val documentDetails: DocumentDetails,
    val response: Response4
)

data class DocumentDetails(
    val imageType: String,
    val sessionId: String
)
