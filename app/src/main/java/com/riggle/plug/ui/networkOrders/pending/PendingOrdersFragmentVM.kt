package com.riggle.plug.ui.networkOrders.pending

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.NetworkPendingOrdersResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PendingOrdersFragmentVM @Inject constructor(private val baseRepo: BaseRepo) :
    BaseViewModel() {

    val obrPendingOrders: SingleRequestEvent<List<NetworkPendingOrdersResponseModel>> =
        SingleRequestEvent()

    fun getList(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrPendingOrders.postValue(Resource.loading(null))
            try {
                baseRepo.getNetworkPendingOrdersList(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrPendingOrders.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrPendingOrders.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrPendingOrders.postValue(Resource.error(null, e.localizedMessage))
                }
            }
        }
    }
}