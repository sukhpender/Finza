package com.riggle.plug.ui.finza.profile

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.FinzaProfileModel
import com.riggle.plug.data.model.FinzaProfileResponseModel
import com.riggle.plug.data.model.FinzaUpdateProfileResponseModel
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
class ProfileActivityVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrProfile = SingleRequestEvent<FinzaProfileResponseModel>()
    fun finzaGetProfile(token: String) {
        Coroutines.io {
            obrProfile.postValue(Resource.loading(null))
            try {
                baseRepo.finzaGetProfile(token).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrProfile.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrProfile.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrProfile.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrUpdateProfile = SingleRequestEvent<FinzaProfileResponseModel>()
    fun updateProfile(
        header: String, body: RequestBody, body2: RequestBody, profile_image: MultipartBody.Part
    ) {
        Coroutines.io {
            obrUpdateProfile.postValue(Resource.loading(null))
            try {
                baseRepo.updateUserProfile(header, body, body2, profile_image).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUpdateProfile.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUpdateProfile.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUpdateProfile.postValue(Resource.error(null, it))
                }
            }
        }
    }
}