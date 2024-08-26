package com.riggle.plug.ui.home.home.homeInsights

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.HomeInsightRetailersResponseModel
import com.riggle.plug.data.model.HomeInsightsLastDaysResponseModel
import com.riggle.plug.data.model.HomeInsightsOrderPlacedResponseModel
import com.riggle.plug.data.model.HomeInsightsSubCatResponseModel
import com.riggle.plug.data.model.HomePageFiltersResponseModel
import com.riggle.plug.data.model.HomePageSalesPersonListResponsModel
import com.riggle.plug.data.model.HomePageSalesResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeInsightsFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrLastDays: SingleRequestEvent<List<HomeInsightsLastDaysResponseModel>> = SingleRequestEvent()
    fun getHomeInsightsLastDays(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrLastDays.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeInsightsLastDays(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrLastDays.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrLastDays.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrLastDays.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrFiltersData: SingleRequestEvent<HomePageFiltersResponseModel> = SingleRequestEvent()
    fun getFiltersData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrFiltersData.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeFiltersData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrFiltersData.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrFiltersData.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrFiltersData.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSalesCountData: SingleRequestEvent<HomePageSalesResponseModel> = SingleRequestEvent()
    fun getHomeSalesCountData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrSalesCountData.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeSalesCountData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesCountData.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesCountData.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesCountData.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSalesPersonsList: SingleRequestEvent<List<HomePageSalesPersonListResponsModel>> = SingleRequestEvent()
    fun getHomeSalesPersonsListData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrSalesPersonsList.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeSalesPersonsListData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesPersonsList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesPersonsList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesPersonsList.postValue(Resource.error(null, it))
                }
            }
        }
    }


    val obrHomeRetailers: SingleRequestEvent<HomeInsightRetailersResponseModel> = SingleRequestEvent()
    fun getHomeRetailersData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrHomeRetailers.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeRetailersData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrHomeRetailers.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrHomeRetailers.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrHomeRetailers.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrHomePlacedOrders: SingleRequestEvent<HomeInsightsOrderPlacedResponseModel> = SingleRequestEvent()
    fun getHomePlacedOrdersData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrHomePlacedOrders.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeOrdersPlacedData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrHomePlacedOrders.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrHomePlacedOrders.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrHomePlacedOrders.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSubCat: SingleRequestEvent<HomeInsightsSubCatResponseModel> = SingleRequestEvent()
    fun getHomeSubCatData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrSubCat.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeSubCatData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSubCat.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSubCat.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSubCat.postValue(Resource.error(null, it))
                }
            }
        }
    }
}