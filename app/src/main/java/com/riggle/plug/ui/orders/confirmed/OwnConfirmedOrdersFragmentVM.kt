package com.riggle.plug.ui.orders.confirmed

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.NetworkConfirmedResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnConfirmedOrdersFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrConfirmedOrdersOrders: SingleRequestEvent<List<NetworkConfirmedResponseModel>> =
        SingleRequestEvent()

    fun getList(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrConfirmedOrdersOrders.postValue(Resource.loading(null))
            try {
                baseRepo.getNetworkConfirmedOrdersList(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrConfirmedOrdersOrders.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrConfirmedOrdersOrders.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrConfirmedOrdersOrders.postValue(Resource.error(null, it))
                }
            }
        }
    }
}