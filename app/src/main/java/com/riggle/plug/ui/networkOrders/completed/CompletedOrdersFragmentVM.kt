package com.riggle.plug.ui.networkOrders.completed

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.NetworkCompletedOrdersResponseModel
import com.riggle.plug.data.model.NetworkConfirmedResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompletedOrdersFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrOrders: SingleRequestEvent<List<NetworkCompletedOrdersResponseModel>> =
        SingleRequestEvent()

    fun getList(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrOrders.postValue(Resource.loading(null))
            try {
                baseRepo.getNetworkCompletedOrdersList(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrOrders.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrOrders.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrOrders.postValue(Resource.error(null, it))
                }
            }
        }
    }
}