package com.riggle.plug.ui.more.beat.insights.graph

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.BeatInsightsResponseModel
import com.riggle.plug.data.model.UnAssignedBeatResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GraphFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrBeatInsights: SingleRequestEvent<BeatInsightsResponseModel> = SingleRequestEvent()

    fun getCitiesList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrBeatInsights.postValue(Resource.loading(null))
            try {
                baseRepo.getBeatInsights(header, companyId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrBeatInsights.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrBeatInsights.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrBeatInsights.postValue(Resource.error(null, it))
                }
            }
        }
    }
}