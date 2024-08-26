package com.riggle.plug.data.model

data class HomeLeaderBoardSPResponseModel(
    val designation_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val states: List<String>,
    val target_data: List<TargetData1>
)

data class TargetData1(
    val current_month_secondary_achievement: Double,
    val current_month_secondary_target: Int,
    val last_month_secondary_achievement: Double,
    val last_month_secondary_target: Int
)