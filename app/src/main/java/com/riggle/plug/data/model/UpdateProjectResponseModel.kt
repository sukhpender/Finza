package com.riggle.plug.data.model

data class UpdateProjectResponseModel(
    val `data`: UpdateProjectData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class UpdateProjectData(
    val created_at: String,
    val id: Int,
    val price: String,
    val tag_assigned_charges: String,
    val title: String,
    val type: String,
    val updated_at: String
)