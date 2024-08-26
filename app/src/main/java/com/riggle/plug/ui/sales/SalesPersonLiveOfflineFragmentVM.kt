package com.riggle.plug.ui.sales

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.ActiveSalesmanResponseModel
import com.riggle.plug.data.model.NetworkCancelledOrdersResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesPersonLiveOfflineFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrInActiveSalesman: SingleRequestEvent<List<ActiveSalesmanResponseModel>> =
        SingleRequestEvent()

    fun getActiveSalesmanList(
        header: String,companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrInActiveSalesman.postValue(Resource.loading(null))
            try {
                baseRepo.getActiveSalesmanList(header,companyId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrInActiveSalesman.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrInActiveSalesman.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrInActiveSalesman.postValue(Resource.error(null, e.localizedMessage))
                }
            }
        }
    }
}