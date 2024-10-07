package com.riggle.finza_finza.data.model

/*
class BrandNetworkCPCount1 : ArrayList<NetworkCPCount1Item>()
*/

data class NetworkCPCount1Item(
    val address_line: String,
    val admin: Admin,
    val city: String,
    val direct_channels: Int,
    val full_address: String,
    val id: Int,
    val indirect_channels: Int,
    val locality: String,
    val logo: String?,
    val name: String,
    val pincode: String,
    val primary_salesperson: PrimarySalesperson,
    val role: String,
    val role_name: String,
    val salesmans: List<Salesman>,
    val sellers: Sellers,
    val short_address: String,
    val state: String,
    val update_url: String
)

data class Admin(
    val email: String,
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class PrimarySalesperson(
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String?,
    val is_active: Boolean,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class Salesman(
    val first_name: String,
    val full_name: String,
    val id: Int,
    val image: String?,
    val is_active: Boolean,
    val last_name: String,
    val mobile: String,
    val update_url: String
)

data class Sellers(
    val full_address: String,
    val id: Int,
    val is_active: Boolean,
    val lat: Double?,
    val logo: String,
    val long: Double?,
    val name: String,
    val pincode: String,
    val update_url: String
)