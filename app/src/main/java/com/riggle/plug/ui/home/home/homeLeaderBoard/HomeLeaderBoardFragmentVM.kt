package com.riggle.plug.ui.home.home.homeLeaderBoard

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.CPLeaderBoardResponseModel
import com.riggle.plug.data.model.GenerateReportResponseModel
import com.riggle.plug.data.model.HomeLeaderBoardSPResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeLeaderBoardFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrReport: SingleRequestEvent<GenerateReportResponseModel> = SingleRequestEvent()
    fun getReport(header: String) {
        Coroutines.io {
            obrReport.postValue(Resource.loading(null))
            try {
                baseRepo.getLeaderBoardReport(header).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrReport.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrReport.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrReport.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrCP: SingleRequestEvent<List<CPLeaderBoardResponseModel>> = SingleRequestEvent()
    fun getCPLeaderBoardList(
        header: String,id: String, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrCP.postValue(Resource.loading(null))
            try {
                baseRepo.getCPLeaderBoardList(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCP.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCP.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCP.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSalesPerson: SingleRequestEvent<List<HomeLeaderBoardSPResponseModel>> = SingleRequestEvent()
    fun getHomeLeaderBoardSalesPersons(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrSalesPerson.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeLeaderBoardSalesPersons(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesPerson.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesPerson.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesPerson.postValue(Resource.error(null, it))
                }
            }
        }
    }
}