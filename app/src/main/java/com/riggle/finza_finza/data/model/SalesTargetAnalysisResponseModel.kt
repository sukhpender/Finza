package com.riggle.finza_finza.data.model

data class SalesTargetAnalysisResponseModel(
    val active_accounts_target_data: List<TargetProgressData>?,
    val active_accounts_target_value: Int,
    val order_amount_target_data: List<TargetProgressData>?,
    val order_amount_target_value: Int,
    val productive_count_target_data: List<TargetProgressData>?,
    val productive_count_target_value: Int,
    val total_count_target_data: List<TargetProgressData>?,
    val total_count_target_value: Int
)

data class TargetProgressData(
    val key: Float?
)