package com.riggle.plug.ui.finza.issueSuperTag.verify

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.CreateCustomerRew
import com.riggle.plug.data.model.IssueTagUserCreateResponseModel
import com.riggle.plug.data.model.RegisterTagRequest
import com.riggle.plug.data.model.RegisterTagResponseModel
import com.riggle.plug.data.model.SendOtpIssueTagResponseModel
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.data.model.UploadDocumentResponseModel
import com.riggle.plug.data.model.ValidateOtpRequest
import com.riggle.plug.data.model.VehicleMakersListResponseModel
import com.riggle.plug.data.model.VehicleMakersRequest
import com.riggle.plug.data.model.VerifyOtpResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class VerifyTagActivityVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

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
                                null, handleErrorResponse(it.errorBody(), it.code())
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
    fun getMakersList(header: String, reqBody: VehicleMakersRequest) {
        Coroutines.io {
            obrMakersList.postValue(Resource.loading(null))
            try {
                baseRepo.vehicleMakersList(header, reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrMakersList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrMakersList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
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

    val obrCreateBajajCustomer = SingleRequestEvent<IssueTagUserCreateResponseModel>()
    fun createCustomer(header: String, reqBody: CreateCustomerRew) {
        Coroutines.io {
            obrCreateBajajCustomer.postValue(Resource.loading(null))
            try {
                baseRepo.createCustomerBajaj(header, reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCreateBajajCustomer.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCreateBajajCustomer.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCreateBajajCustomer.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrUploadDocument = SingleRequestEvent<UploadDocumentResponseModel>()
    fun uploadDocument(
        header: String,
        requestId: RequestBody,
        sessionId: RequestBody,
        channel: RequestBody,
        agentId: RequestBody,
        reqDateTime: RequestBody,
        imageType: RequestBody,
        image: MultipartBody.Part,
        provider: RequestBody,
    ) {
        Coroutines.io {
            obrUploadDocument.postValue(Resource.loading(null))
            try {
                baseRepo.bajajUploadDocument(
                    header,
                    requestId,
                    sessionId,
                    channel,
                    agentId,
                    reqDateTime,
                    imageType,
                    image,
                    provider
                ).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUploadDocument.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUploadDocument.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUploadDocument.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrRegisterTag = SingleRequestEvent<RegisterTagResponseModel>()
    fun registerTag(header: String, request: RegisterTagRequest, ) {
        Coroutines.io {
            obrRegisterTag.postValue(Resource.loading(null))
            try {
                baseRepo.registerTag(header, request).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrRegisterTag.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrRegisterTag.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrRegisterTag.postValue(Resource.error(null, it))
                }
            }
        }
    }
}