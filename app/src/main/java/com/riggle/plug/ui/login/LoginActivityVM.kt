package com.riggle.plug.ui.login

import com.riggle.plug.data.api.BaseRepo
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

    val obrSendOtp = SingleRequestEvent<SendOtpResponseModel>()
    var obrVerifyOtp = SingleRequestEvent<VerifyOtpResponseModel>()

    //8879362100
    fun sendOtp(mobile: String) {
        Coroutines.io {
            obrSendOtp.postValue(Resource.loading(null))
            try {
                baseRepo.sendOtp(mobile).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSendOtp.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSendOtp.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSendOtp.postValue(Resource.error(null, it))
                }
            }
        }
    }

    fun verifyOtp(mobile: String, otp: String) {
        Coroutines.io {
            obrVerifyOtp.postValue(Resource.loading(null))
            try {
                baseRepo.verifyOtp(mobile, otp).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrVerifyOtp.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrVerifyOtp.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrVerifyOtp.postValue(Resource.error(null, it))
                }
            }
        }
    }
}