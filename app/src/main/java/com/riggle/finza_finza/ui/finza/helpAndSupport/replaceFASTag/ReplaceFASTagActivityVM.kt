package com.riggle.finza_finza.ui.finza.helpAndSupport.replaceFASTag

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.ReplacementResponseModel
import com.riggle.finza_finza.data.model.SendOtpIssueTagResponseModel
import com.riggle.finza_finza.data.model.SendOtpRequest
import com.riggle.finza_finza.data.model.TagReplaceRequest
import com.riggle.finza_finza.data.model.ValidateOtpRequest
import com.riggle.finza_finza.data.model.VerifyOtpResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReplaceFASTagActivityVM @Inject constructor(private val baseRepo: BaseRepo) :
    BaseViewModel() {

    val obrSendOtp = SingleRequestEvent<SendOtpIssueTagResponseModel>()
    fun sendOtp(header: String, reqBody: SendOtpRequest) {
        Coroutines.io {
            obrSendOtp.postValue(Resource.loading(null))
            try {
                baseRepo.sendOtpTagIssue(header, reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSendOtp.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSendOtp.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
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

    val obrVerifyOtp = SingleRequestEvent<VerifyOtpResponseModel>()
    fun verifyOtp(header: String, reqBody: ValidateOtpRequest) {
        Coroutines.io {
            obrVerifyOtp.postValue(Resource.loading(null))
            try {
                baseRepo.verifyOtpTagIssue(header, reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrVerifyOtp.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrVerifyOtp.postValue(
                            Resource.error(
                                it.body(), handleErrorResponse(it.errorBody(), it.code())
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

    val obrTagReplacement = SingleRequestEvent<ReplacementResponseModel>()
    fun tagReplacement(header: String, reqBody: TagReplaceRequest) {
        Coroutines.io {
            obrTagReplacement.postValue(Resource.loading(null))
            try {
                baseRepo.tagReplacement(header, reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrTagReplacement.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrTagReplacement.postValue(
                            Resource.error(
                                it.body(), handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrTagReplacement.postValue(Resource.error(null, it))
                }
            }
        }
    }

}