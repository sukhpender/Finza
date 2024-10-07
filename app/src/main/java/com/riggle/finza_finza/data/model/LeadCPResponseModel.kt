package com.riggle.finza_finza.data.model

data class LeadCPResponseModel(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<ResultLeadCP>
)

data class ResultLeadCP(
    val admin: AdminLeadCP,
    val channel_id: Any?,
    val extra: ExtraLeadCP,
    val full_address: String,
    val id: Int,
    val logo: Any,
    val name: String,
    val update_url: String
)

data class AdminLeadCP(
    val created_at: String,
    val created_by: CreatedBy,
    val full_name: String,
    val mobile: String,
    val other_languages: List<Any>
)

data class ExtraLeadCP(
    val cp_interested: String,
    val current_brands: String,
    val delivery_staff: Int,
    val dist_visiting_card: String,
    val expected_closure_date: String,
    val fmcg_experience: String,
    val is_sampling_done: Boolean,
    val minimum_investment_plan: String,
    val monthly_potential: String,
    val products_category: String,
    val sample_image: Any,
    val sampling_quantity: Int,
    val sampling_required: Boolean,
    val tempo_quantity: Int,
    val tempo_type: String,
    val wharehouse_space: String,
    val working_type: String
)

data class CreatedBy(
    val full_name: String,
    val image: String,
    val mobile: String,
    val other_languages: List<Any>
)