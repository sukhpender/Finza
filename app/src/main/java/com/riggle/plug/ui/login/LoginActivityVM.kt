package com.riggle.plug.ui.login

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.SendOtpResponseModel
import com.riggle.plug.data.model.VerifyOtpResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrLogin = SingleRequestEvent<FinzaLoginResponseModel>()

    fun finzaLogin(email: String,pass: String) {
        Coroutines.io {
            obrLogin.postValue(Resource.loading(null))
            try {
                baseRepo.finzaLogin(email,pass).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrLogin.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrLogin.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrLogin.postValue(Resource.error(null, it))
                }
            }
        }
    }
}