package com.riggle.plug.ui.finza.issueSuperTag.verify

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.CreateCustomerRew
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.FinzaLogoutResponseModel
import com.riggle.plug.data.model.SendOtpIssueTagResponseModel
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.data.model.ValidateOtpRequest
import com.riggle.plug.data.model.VehicleMakersListResponseModel
import com.riggle.plug.data.model.VehicleMakersRequest
import com.riggle.plug.data.model.VerifyOtpRequest
import com.riggle.plug.data.model.VerifyOtpResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyTagActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrSendOtp = SingleRequestEvent<SendOtpIssueTagResponseModel>()
    fun sendOtp(header: String,reqBody: SendOtpRequest) {
        Coroutines.io {
            obrSendOtp.postValue(Resource.loading(null))
            try {
                baseRepo.sendOtpTagIssue(header,reqBody).let {
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

    val obrVerifyOtp = SingleRequestEvent<VerifyOtpResponseModel>()
    fun verifyOtp(header: String,reqBody: ValidateOtpRequest) {
        Coroutines.io {
            obrVerifyOtp.postValue(Resource.loading(null))
            try {
                baseRepo.verifyOtpTagIssue(header,reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrVerifyOtp.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrVerifyOtp.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
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


    val obrMakersList = SingleRequestEvent<VehicleMakersListResponseModel>()
    fun getMakersList(header: String,reqBody: VehicleMakersRequest) {
        Coroutines.io {
            obrMakersList.postValue(Resource.loading(null))
            try {
                baseRepo.vehicleMakersList(header,reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrMakersList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrMakersList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrMakersList.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrCreateBajajCustomer = SingleRequestEvent<VehicleMakersListResponseModel>()
    fun createCustomer(header: String,reqBody: CreateCustomerRew) {
        Coroutines.io {
            obrMakersList.postValue(Resource.loading(null))
            try {
                baseRepo.createCustomerBajaj(header,reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrMakersList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrMakersList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrMakersList.postValue(Resource.error(null, it))
                }
            }
        }
    }

}