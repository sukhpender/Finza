package com.riggle.plug.ui.networkOrders.outstanding

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.NetworkOutstandingOrdersResponseModel
import com.riggle.plug.data.model.NetworkPendingOrdersResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OutstandingOrdersFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel(){

    val obrOutstandingOrdersOrders: SingleRequestEvent<List<NetworkOutstandingOrdersResponseModel>> =
        SingleRequestEvent()

    fun getList(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrOutstandingOrdersOrders.postValue(Resource.loading(null))
            try {
                baseRepo.getNetworkOutstandingOrdersList(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrOutstandingOrdersOrders.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrOutstandingOrdersOrders.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrOutstandingOrdersOrders.postValue(Resource.error(null, it))
                }
            }
        }
    }
}