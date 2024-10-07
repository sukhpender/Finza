package com.riggle.finza_finza.data.model

data class UserWalletResponseModel(
    val inventory_wallet: String,
    val message: String,
    val status: Boolean,
    val wallet: String,
    val hold_amount: String,
    )