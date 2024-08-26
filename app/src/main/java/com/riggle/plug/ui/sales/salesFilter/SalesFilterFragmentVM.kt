package com.riggle.plug.ui.sales.salesFilter

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.ActiveCPResponseModel
import com.riggle.plug.data.model.DesignationSalesFilterResponseModel
import com.riggle.plug.data.model.ReportingManagerResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesFilterFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrReportingManager: SingleRequestEvent<List<ReportingManagerResponseModel>> = SingleRequestEvent()
    fun getActiveCPList(header: String,userID: String) {
        Coroutines.io {
            obrReportingManager.postValue(Resource.loading(null))
            try {
                baseRepo.getReportingManagerList(header,userID).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrReportingManager.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrReportingManager.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrReportingManager.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrDesignations: SingleRequestEvent<List<DesignationSalesFilterResponseModel>> = SingleRequestEvent()
    fun getDesignations(header: String) {
        Coroutines.io {
            obrDesignations.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesDesignationsList(header).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrDesignations.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrDesignations.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrDesignations.postValue(Resource.error(null, it))
                }
            }
        }
    }

}