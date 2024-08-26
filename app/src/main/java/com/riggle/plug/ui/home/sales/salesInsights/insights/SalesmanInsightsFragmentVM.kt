package com.riggle.plug.ui.home.sales.salesInsights.insights

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.BeatInsightsResponseModel
import com.riggle.plug.data.model.DailyAnalysisCalenderResponseModel
import com.riggle.plug.data.model.SalesBrandAnalysisInsightsResponseModel
import com.riggle.plug.data.model.SalesDailyAnalysisResponseModel
import com.riggle.plug.data.model.SalesSKUsResponseModel
import com.riggle.plug.data.model.SalesTargetAnalysisResponseModel
import com.riggle.plug.data.model.SalesUserListResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesmanInsightsFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrSalesTargetList: SingleRequestEvent<List<SalesTargetAnalysisResponseModel>> = SingleRequestEvent()
    fun getSalesTargetList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrSalesTargetList.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesTargetsList(header, companyId.toString(), query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesTargetList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesTargetList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesTargetList.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrBrandAnalysisInsights: SingleRequestEvent<SalesBrandAnalysisInsightsResponseModel> = SingleRequestEvent()
    fun getSalesBrandAnalysisInsights(
        header: String, companyId: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrBrandAnalysisInsights.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesBrandAnalysisInsights(header, companyId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrBrandAnalysisInsights.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrBrandAnalysisInsights.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrBrandAnalysisInsights.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSalesSKUstList: SingleRequestEvent<SalesSKUsResponseModel> = SingleRequestEvent()
    fun getSalesSKUsList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrSalesSKUstList.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesSKUsList(header, companyId.toString(), query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesSKUstList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesSKUstList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesSKUstList.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSalesDailyAnalysis: SingleRequestEvent<SalesDailyAnalysisResponseModel> = SingleRequestEvent()
    fun getDailyAnalysis(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrSalesDailyAnalysis.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesDailyAnalysis(header, companyId.toString(), query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesDailyAnalysis.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesDailyAnalysis.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesDailyAnalysis.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrDailyCalenderAnalysisInsights: SingleRequestEvent<DailyAnalysisCalenderResponseModel> = SingleRequestEvent()
    fun getDailyCalenderAnalysisInsights(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrDailyCalenderAnalysisInsights.postValue(Resource.loading(null))
            try {
                baseRepo.getDailyAnalysisInsights(header, companyId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrDailyCalenderAnalysisInsights.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrDailyCalenderAnalysisInsights.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrDailyCalenderAnalysisInsights.postValue(Resource.error(null, it))
                }
            }
        }
    }
}