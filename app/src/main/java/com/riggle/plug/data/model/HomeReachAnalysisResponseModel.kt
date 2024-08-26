package com.riggle.plug.data.model

data class HomeReachAnalysisResponseModel(
    val distributor: Distributor1?,
    val ss: Ss?,
    val wholesaler: Wholesaler1?
)

data class Distributor1(
    val count: List<Count>?,
    val name: String?
)

data class Ss(
    val count: List<Count>?,
    val name: String?
)

data class Wholesaler1(
    val count: List<Count>?,
    val name: String?
)

data class Count(
    val count: Int,
    val month: Int,
    val transacting_count: Int
)