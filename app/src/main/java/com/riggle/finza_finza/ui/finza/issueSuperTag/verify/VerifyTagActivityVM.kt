package com.riggle.finza_finza.ui.finza.issueSuperTag.verify

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.CreateCustomerRew
import com.riggle.finza_finza.data.model.GetVehicleDetailsResponseModel
import com.riggle.finza_finza.data.model.IssueTagCheckWalletResponseModel
import com.riggle.finza_finza.data.model.IssueTagUserCreateResponseModel
import com.riggle.finza_finza.data.model.RegisterTagRequest
import com.riggle.finza_finza.data.model.RegisterTagResponseModel
import com.riggle.finza_finza.data.model.SendOtpIssueTagResponseModel
import com.riggle.finza_finza.data.model.SendOtpRequest
import com.riggle.finza_finza.data.model.UploadDocumentResponseModel
import com.riggle.finza_finza.data.model.ValidateOtpRequest
import com.riggle.finza_finza.data.model.VehicleMakersListResponseModel
import com.riggle.finza_finza.data.model.VehicleMakersRequest
import com.riggle.finza_finza.data.model.VehicleModelListResponseModel
import com.riggle.finza_finza.data.model.VehicleModelRequest
import com.riggle.finza_finza.data.model.VerifyOtpResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
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

    val obrModelList = SingleRequestEvent<VehicleModelListResponseModel>()
    fun getModelsList(header: String, reqBody: VehicleModelRequest) {
        Coroutines.io {
            obrModelList.postValue(Resource.loading(null))
            try {
                baseRepo.vehicleModelList(header, reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrModelList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrModelList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrModelList.postValue(Resource.error(null, it))
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
        inventory_id: RequestBody,
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
                    provider,
                    inventory_id
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

    val obrIssueTagCheckWallet = SingleRequestEvent<IssueTagCheckWalletResponseModel>()
    fun issueTagCheckWallet(token: String) {
        Coroutines.io {
            obrIssueTagCheckWallet.postValue(Resource.loading(null))
            try {
                baseRepo.issueTagCheckWallet(token).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrIssueTagCheckWallet.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrIssueTagCheckWallet.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrIssueTagCheckWallet.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrVehicleDetails = SingleRequestEvent<GetVehicleDetailsResponseModel>()
    fun getVehicleDetails(token: String,rc_no: String,type: Int) {
        Coroutines.io {
            obrVehicleDetails.postValue(Resource.loading(null))
            try {
                baseRepo.getVehicleDetails(token,rc_no,type).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrVehicleDetails.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrVehicleDetails.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrVehicleDetails.postValue(Resource.error(null, it))
                }
            }
        }
    }
}