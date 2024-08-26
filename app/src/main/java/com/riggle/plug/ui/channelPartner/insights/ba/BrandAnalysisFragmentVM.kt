package com.riggle.plug.ui.channelPartner.insights.ba

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.CpInsightsResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrandAnalysisFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel(){

    val obrInsights: SingleRequestEvent<CpInsightsResponseModel> = SingleRequestEvent()

    fun getInsights(
        header: String,companyId: Int, query:Map<String, String>
    ) {
        Coroutines.io {
            obrInsights.postValue(Resource.loading(null))
            try {
                baseRepo.getCpInsights(header,companyId.toString(),query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrInsights.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrInsights.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrInsights.postValue(Resource.error(null, it))
                }
            }
        }
    }
}