package com.riggle.finza_finza.ui.forgotPassword

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.FinzaForgotPassResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrFPass = SingleRequestEvent<FinzaForgotPassResponseModel>()

    fun forgotPass(email: String) {
        Coroutines.io {
            obrFPass.postValue(Resource.loading(null))
            try {
                baseRepo.forgotPass(email).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrFPass.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrFPass.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrFPass.postValue(Resource.error(null, it))
                }
            }
        }
    }
}