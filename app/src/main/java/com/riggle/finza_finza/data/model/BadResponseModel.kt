package com.riggle.finza_finza.data.model

data class BadResponseModel(
    val `data`: BadData,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class BadData(
    val `data`: BadDataX,
    val total_amount: String,
    val total_count: Int
)

data class BadDataX(
    val current_page: Int,
    val `data`: List<BadDataXX>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<BadLink>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class BadDataXX(
    val amount: String,
    val audit_by: Int,
    val created_at: String,
    val description: String,
    val fastag: Fastag,
    val fastag_id: Int,
    val id: Int,
    val reason: String,
    val status: Int,
    val updated_at: String,
    val user_id: Int
)

data class BadLink(
    val active: Boolean,
    val label: String,
    val url: String
)

data class Fastag(
    val agent_id: String,
    val audit_status: Int,
    val channel: String,
    val chassis: String,
    val created_at: String,
    val debit_amt: String,
    val engine: String,
    val error_code: String,
    val error_desc: String,
    val id: Int,
    val is_commercial: Boolean,
    val is_national_permit: Boolean,
    val mobile_no: String,
    val model: String,
    val name: String,
    val npci_status: String,
    val npci_vehicle_class_id: String,
    val permit_expiry_date: Any,
    val recharge_amount: String,
    val req_date_time: String,
    val request_id: String,
    val response_code: String,
    val response_msg: String,
    val response_status: String,
    val security_deposit: String,
    val serial_no: String,
    val session_id: String,
    val state_of_registration: String,
    val status: String,
    val tag_cost: String,
    val tag_registration_resp_agent_balance: String,
    val tag_registration_resp_agent_id: String,
    val tag_registration_resp_channel: String,
    val tag_registration_resp_npci_status: String,
    val tag_registration_resp_request_id: String,
    val tag_registration_resp_resp_date_time: String,
    val tag_registration_resp_serial_no: String,
    val tag_registration_resp_tid: String,
    val tag_registration_resp_udf1: String,
    val tag_registration_resp_udf2: String,
    val tag_registration_resp_udf3: String,
    val tag_registration_resp_udf4: String,
    val tag_registration_resp_udf5: String,
    val tag_registration_resp_vrn: String,
    val tag_registration_resp_wallet_id: Any,
    val tag_vehicle_class_id: String,
    val tid: String,
    val type: String,
    val udf1: String,
    val udf2: String,
    val udf3: String,
    val udf4: String,
    val udf5: String,
    val updated_at: String,
    val user_id: Int,
    val vehicle_colour: String,
    val vehicle_descriptor: String,
    val vehicle_manuf: String,
    val vehicle_type: String,
    val vrn: String,
    val wallet_id: String
)