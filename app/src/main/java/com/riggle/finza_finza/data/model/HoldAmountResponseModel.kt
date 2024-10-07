package com.riggle.finza_finza.data.model

data class HoldAmountResponseModel(
    val hold_amount: String,
    val inventory_wallet: String,
    val status: Boolean,
    val wallet: String
)