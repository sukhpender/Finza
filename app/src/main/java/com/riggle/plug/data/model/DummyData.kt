package com.riggle.plug.data.model

data class Drawer(val image: Int, val name: String)

data class UglyIssuence(val date: String,val fNumber: String,val bank: String)

data class Language(val englishName:String, val languageName: String)

data class ProductRate(val productName: String)

data class Manufacturer(val manufacturerName: String)

data class Distributor(val distributorName: String)

data class WholeSaler(val wholesalerName: String)

data class Coupons(val couponName: String)

data class SalesTable(val name: String)

data class PartnerActiveCP(val name: String)

data class Remarks(val name: String)

data class HomeModel(val name: String)

data class HomeBarModel(val name: String, val bar1: Int, val bar2: Int)

data class NetworkTypes(val name: String, val count: String, var isSelected: Boolean)

data class NetworkPendingOrders(val name: String)

data class MoreItems(val name: String, val image: Int)

data class MoreBeats(val name: String, var isAssigned: Boolean, val person: String)

data class MoreBeatDetails(val name: String)

data class MoreBeatStock(val name: String)

data class SKUs(val name: String,var isSelected: Boolean = false)

data class NetworkLinkModel(val name: String, val id: Int)

data class SalesDummy(val name: String)

data class VerifyOtp(
    val mobile: String,
    val otp: String,
    val FCM_CLIENT_TOKEN: String? = null
)

data class SendOtpBean(
    val mobile: String
)

data class DeliveryRatioResponseModel(
    val orderType: String,
    val percentage: String
)

data class DailyAnalysis(
    val type: String,
    val visit: String,
    val remark: String,
    val orders: String,
    val ordersValue: String
)

data class FolderNameData(
    val name: String?,
    val id: Int?
)