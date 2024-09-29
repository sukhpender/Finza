package com.riggle.plug.ui.finza.wallet.addMoney

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.PaymentStoreRequest
import com.riggle.plug.data.model.SendOtpIssueTagResponseModel
import com.riggle.plug.data.model.StorePaymentResponseModel
import com.riggle.plug.data.model.UserWalletResponseModel
import com.riggle.plug.data.model.WalletCreateCustomerResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddMoneyActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

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
                                null, handleErrorResponse(it.errorBody(),it.code())
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

    val obrStorePayment = SingleRequestEvent<StorePaymentResponseModel>()
    fun storePayment(header: String, body:PaymentStoreRequest) {
        Coroutines.io {
            obrStorePayment.postValue(Resource.loading(null))
            try {
                baseRepo.storePayment(header,body).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrStorePayment.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrStorePayment.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
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