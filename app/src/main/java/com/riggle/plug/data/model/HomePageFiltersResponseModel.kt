package com.riggle.plug.data.model

data class HomePageFiltersResponseModel(
    val channel_partners: List<ChannelPartner>,
    val districts: List<District>,
    val sales_person: List<SalesPerson>,
    val states: List<State>
)

data class ChannelPartner(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
)

data class District(
    val district_name: String,
    var isSelected: Boolean = false
)

data class SalesPerson(
    val first_name: String,
    val id: Int,
    val last_name: String,
    var isSelected: Boolean = false
)

data class State(
    val state_name: String,
    var isSelected: Boolean = false
)