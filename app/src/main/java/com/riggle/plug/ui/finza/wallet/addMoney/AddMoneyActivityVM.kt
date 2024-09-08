package com.riggle.plug.ui.finza.wallet.addMoney

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.SendOtpIssueTagResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddMoneyActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrCreateCustomer = SingleRequestEvent<SendOtpIssueTagResponseModel>()

    fun createCustomer(header: String, name:String, email: String,phone: String) {
        Coroutines.io {
            obrCreateCustomer.postValue(Resource.loading(null))
            try {
                baseRepo.createWalletCustomer(header,name,email,phone).let {
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

}