package com.riggle.finza_finza.data.model

data class Transfer(
    val inventory_id : Int,
    var assigned_to: Int
)

data class TransferRequest(
    val transfers: List<Transfer>
)


data class Cancelled1(
    val inventory_id : Int,
    var assigned_to: Int
)

data class CancelRequest(
    val cancelled: List<Cancelled1>
)

data class AcceptRejectRequest(
    val inventory_ids : List<Int>,
    var status: Int
)
