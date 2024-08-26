package com.riggle.plug.ui.more.myAccount

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.CoOwnersListResponseModel
import com.riggle.plug.data.model.DeleteCoOwnerResponse
import com.riggle.plug.data.model.UnAssignedBeatResponseModel
import com.riggle.plug.data.model.UserProfileResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyAccountFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrProfile: SingleRequestEvent<UserProfileResponseModel> = SingleRequestEvent()

    fun getCitiesList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrProfile.postValue(Resource.loading(null))
            try {
                baseRepo.getUserProfile(header, companyId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrProfile.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrProfile.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e)?.let {
                    obrProfile.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrCoOwner: SingleRequestEvent<List<CoOwnersListResponseModel>> = SingleRequestEvent()
    fun getCoOwners(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrCoOwner.postValue(Resource.loading(null))
            try {
                baseRepo.getUserProfileCoOwner(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCoOwner.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCoOwner.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e)?.let {
                    obrCoOwner.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrDeleteCoOwner: SingleRequestEvent<DeleteCoOwnerResponse> = SingleRequestEvent()
    fun deleteCoOwner(
        header: String, id: Int
    ) {
        Coroutines.io {
            obrDeleteCoOwner.postValue(Resource.loading(null))
            try {
                baseRepo.deleteCoOwner(header, id).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrDeleteCoOwner.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrDeleteCoOwner.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e)?.let {
                    obrDeleteCoOwner.postValue(Resource.error(null, it))
                }
            }
        }
    }
}