package com.riggle.finza_finza.ui.finza.resetPass

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.ResetPasswordResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPassActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrReset = SingleRequestEvent<ResetPasswordResponseModel>()

    fun resetPass(userId: String,newPass:String,confirmPass:String) {
        Coroutines.io {
            obrReset.postValue(Resource.loading(null))
            try {
                baseRepo.resetPasssword(userId,newPass,confirmPass).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrReset.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrReset.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrReset.postValue(Resource.error(null, it))
                }
            }
        }
    }
}