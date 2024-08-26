package com.riggle.plug.ui.home.home.orderSummary

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.HomeInsightsOrderResponseModel
import com.riggle.plug.data.model.HomeOrderSummaryOrdersResponseModel
import com.riggle.plug.data.model.HomeOrderSummaryResponseModel
import com.riggle.plug.data.model.HomePageSnapShotResponseModel
import com.riggle.plug.data.model.HomeReachAnalysisResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeOrderSummaryFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrSnapShotData: SingleRequestEvent<HomePageSnapShotResponseModel> = SingleRequestEvent()
    fun getSnapShotData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrSnapShotData.postValue(Resource.loading(null))
            try {
                baseRepo.getSnapShotData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSnapShotData.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSnapShotData.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSnapShotData.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrInsightOrders: SingleRequestEvent<HomeInsightsOrderResponseModel> = SingleRequestEvent()
    fun getInsightsOrderData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrInsightOrders.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeInsightsOrderData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrInsightOrders.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrInsightOrders.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrInsightOrders.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrReachAnalysis: SingleRequestEvent<HomeReachAnalysisResponseModel> = SingleRequestEvent()
    fun getHomeReachAnalysisData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrReachAnalysis.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeReachAnalysisData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrReachAnalysis.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrReachAnalysis.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrReachAnalysis.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrRemarks: SingleRequestEvent<HomeOrderSummaryResponseModel> = SingleRequestEvent()
    fun getHomeOrderSummaryRemarks(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrRemarks.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeOrderSummaryData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrRemarks.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrRemarks.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrRemarks.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrRemarks1: SingleRequestEvent<HomeOrderSummaryOrdersResponseModel> = SingleRequestEvent()
    fun getHomeOrderSummaryOrders(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrRemarks1.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeOrderSummaryData1(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrRemarks1.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrRemarks1.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrRemarks1.postValue(Resource.error(null, it))
                }
            }
        }
    }

}