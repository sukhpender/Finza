package com.riggle.finza_finza.data.model

data class TargetGraphResponse(
    val allocation_data: AllocationData?,
    val primary_data: List<PrimaryData>,
    val secondary_data: List<SecondaryData>
)

data class AllocationData(
    val allocated_primary_target: Double?,
    val allocated_secondary_target: Double?,
    val salesman_primary_target: Double?,
    val salesman_secondary_target: Double?
)


data class PrimaryData(
    val month: String,
    val primary_order_values: Float?,
    val primary_target_value: Float?
)

data class SecondaryData(
    val month: String,
    val secondary_order_values: Float,
    val secondary_target_value: Float?
)