package com.riggle.plug.ui.more.myAccount

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.DeleteCoOwnerResponse
import com.riggle.plug.data.model.UserProfileResponseModel
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
class EditOwnInfoFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrAddCoOwner: SingleRequestEvent<DeleteCoOwnerResponse> = SingleRequestEvent()

    fun addCoOwner(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrAddCoOwner.postValue(Resource.loading(null))
            try {
                baseRepo.addCoOwner(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrAddCoOwner.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrAddCoOwner.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrAddCoOwner.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrUpdateUser: SingleRequestEvent<UserProfileResponseModel> = SingleRequestEvent()

    fun updateUser(
        header: String,
        id: String,
        f_name: RequestBody,
        l_name: RequestBody,
        email: RequestBody,
        image: MultipartBody.Part
    ) {
        Coroutines.io {
            obrUpdateUser.postValue(Resource.loading(null))
            try {
                baseRepo.updateUser(header, id, f_name, l_name, email, image).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUpdateUser.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUpdateUser.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUpdateUser.postValue(Resource.error(null, it))
                }
            }
        }
    }

    fun updateUserWithOutImage(
        header: String,
        id: String,
        f_name: RequestBody,
        l_name: RequestBody,
        email: RequestBody) {
        Coroutines.io {
            obrUpdateUser.postValue(Resource.loading(null))
            try {
                baseRepo.updateUserWithOutImage(header, id, f_name, l_name, email)
                    .let {
                        if (it.isSuccessful) {
                            it.body()?.let { results ->
                                obrUpdateUser.postValue(Resource.success(results, "Success"))
                            }
                        } else {
                            obrUpdateUser.postValue(
                                Resource.warn(
                                    null, handleErrorResponse(it.errorBody(), it.code())
                                )
                            )
                        }
                    }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUpdateUser.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val  obrUpdatePan: SingleRequestEvent<UserProfileResponseModel> = SingleRequestEvent()

    fun updatePanUser(
        header: String,
        id: String,
        image: MultipartBody.Part
    ) {
        Coroutines.io {
            obrUpdatePan.postValue(Resource.loading(null))
            try {
                baseRepo.updatePanCard(header, id, image).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUpdatePan.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUpdatePan.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUpdatePan.postValue(Resource.error(null, it))
                }
            }
        }
    }
}