package com.riggle.finza_finza.data.model

data class CreatePaymentLinkResponseModel(
    val `data`: String,
    val message: String,
    val success: Boolean,
    val statusCode: Int
)
//
//data class CreatePaymentLinkData(
//    val accept_partial: Boolean,
//    val amount: Int,
//    val amount_paid: Int,
//    val cancelled_at: Int,
//    val created_at: Int,
//    val currency: String,
//    val customer: Customer,
//    val description: String,
//    val expire_by: Int,
//    val expired_at: Int,
//    val first_min_partial_amount: Int,
//    val id: String,
//    val notes: Notes,
//    val notify: Notify,
//    val payments: Any,
//    val reference_id: String,
//    val reminder_enable: Boolean,
//    val reminders: List<Any>,
//    val short_url: String,
//    val status: String,
//    val updated_at: Int,
//    val upi_link: Boolean,
//    val user_id: String,
//    val whatsapp_link: Boolean
//)
//
//data class Customer(
//    val contact: String,
//    val email: String,
//    val name: String
//)
//
//data class Notes(
//    val contact: String,
//    val email: String,
//    val name: String,
//    val policy_name: String,
//    val user_id: String
//)
//
//data class Notify(
//    val email: Boolean,
//    val sms: Boolean,
//    val whatsapp: Boolean
//)