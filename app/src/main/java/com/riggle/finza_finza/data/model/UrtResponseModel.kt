package com.riggle.finza_finza.data.model

data class UrtResponseModel(
    val `data`: UrtData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class UrtData(
    val `data`: UrtDataX,
    val total_amount: String,
    val total_count: Int
)

data class UrtDataX(
    val current_page: Int,
    val `data`: List<DataXX>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<UrtLink>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class DataXX(
    val amount: String,
    val created_at: String,
    val id: Int,
    val inventory: UrtInventory,
    val inventory_id: Int,
    val tid: String,
    val updated_at: Any,
    val urt: Urt,
    val urt_id: Int,
    val user_id: Int
)

data class UrtLink(
    val active: Boolean,
    val label: String,
    val url: String
)

data class UrtInventory(
    val bar_code: String,
    val created_at: Any,
    val epc_code: String,
    val id: Int,
    val is_registered: Int,
    val issuer_key_index: String,
    val lot_number: String,
    val provider: String,
    val secret_code: String,
    val status: Int,
    val tid: String,
    val updated_at: String,
    val vehicle_class: String
)

data class Urt(
    val agent_id: String,
    val agent_name: String,
    val created_at: String,
    val file_name: String,
    val id: Int,
    val merchant: String,
    val parent_org: Any,
    val parent_org_name: String,
    val recovery: String,
    val recovery_done: Int,
    val tag_account: String,
    val tag_id: String,
    val txn_amt: Int,
    val txn_cd: String,
    val txn_date: String,
    val txn_no: String,
    val updated_at: String
)