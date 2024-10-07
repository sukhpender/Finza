package com.riggle.finza_finza.ui.finza.wallet.addMoney

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.CreatePaymentLinkResponseModel
import com.riggle.finza_finza.data.model.PaymentStoreRequest
import com.riggle.finza_finza.data.model.StorePaymentResponseModel
import com.riggle.finza_finza.data.model.WalletCreateCustomerResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddMoneyActivityVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrCreateCustomer = SingleRequestEvent<WalletCreateCustomerResponseModel>()
    fun createCustomer(header: String) {
        Coroutines.io {
            obrCreateCustomer.postValue(Resource.loading(null))
            try {
                baseRepo.createWalletCustomer(header).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCreateCustomer.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCreateCustomer.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCreateCustomer.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrPaymentLinkCreate = SingleRequestEvent<CreatePaymentLinkResponseModel>()
    fun createPaymentLink(header: String, amount: String) {
        Coroutines.io {
            obrPaymentLinkCreate.postValue(Resource.loading(null))
            try {
                baseRepo.paymentLinkCreate(header, amount).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrPaymentLinkCreate.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrPaymentLinkCreate.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrPaymentLinkCreate.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrStorePayment = SingleRequestEvent<StorePaymentResponseModel>()
    fun storePayment(header: String, body: PaymentStoreRequest) {
        Coroutines.io {
            obrStorePayment.postValue(Resource.loading(null))
            try {
                baseRepo.storePayment(header, body).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrStorePayment.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrStorePayment.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrStorePayment.postValue(Resource.error(null, it))
                }
            }
        }
    }
}