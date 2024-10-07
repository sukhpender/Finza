package com.riggle.finza_finza.data.model

data class IssueTagUserCreateResponseModel(
    val custDetails: CustDetails1,
    val response: Response5
)

data class CustDetails1(
    val kycStatus: String,
    val mobileNo: String,
    val name: String,
    val respDateTime: String,
    val udf1: String,
    val udf2: String,
    val udf3: String,
    val udf4: String,
    val udf5: String,
    val walletId: String,
    val walletStatus: String
)

data class Response5(
    val code: String,
    val errorCode: String,
    val errorDesc: String,
    val msg: String,
    val status: String
)