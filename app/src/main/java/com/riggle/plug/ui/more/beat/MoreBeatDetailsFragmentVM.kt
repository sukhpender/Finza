package com.riggle.plug.ui.more.beat

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.AssignedBeatDetailsResponseModel
import com.riggle.plug.data.model.UnAssignedBeatResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreBeatDetailsFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel(){

    val obrUnAssignedBeats: SingleRequestEvent<UnAssignedBeatResponseModel> = SingleRequestEvent()

    fun getCitiesList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrUnAssignedBeats.postValue(Resource.loading(null))
            try {
                baseRepo.getUnAssignedBeatsList(header, companyId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUnAssignedBeats.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUnAssignedBeats.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUnAssignedBeats.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrAssignedBeatDetails: SingleRequestEvent<AssignedBeatDetailsResponseModel> = SingleRequestEvent()
    fun getAssignedBeatDetails(
        header: String, beatId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrAssignedBeatDetails.postValue(Resource.loading(null))
            try {
                baseRepo.getAssignedBeatDetails(header, beatId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrAssignedBeatDetails.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrAssignedBeatDetails.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrAssignedBeatDetails.postValue(Resource.error(null, it))
                }
            }
        }
    }
}