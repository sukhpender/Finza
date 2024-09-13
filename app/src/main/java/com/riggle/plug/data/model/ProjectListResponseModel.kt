package com.riggle.plug.data.model

data class ProjectListResponseModel(
    val `data`: List<ProjectListData>,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class ProjectListData(
    val created_at: String,
    val id: Int,
    val price: String,
    val title: String,
    val updated_at: String
)