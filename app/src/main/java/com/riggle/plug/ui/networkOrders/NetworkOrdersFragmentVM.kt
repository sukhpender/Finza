package com.riggle.plug.ui.networkOrders

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.NetworkOrdersCountResponseModel
import com.riggle.plug.data.model.NetworkPendingOrdersResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkOrdersFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel(){

    val obrNetworkCount: SingleRequestEvent<NetworkOrdersCountResponseModel> = SingleRequestEvent()

    fun getCount(
        header: String,id: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrNetworkCount.postValue(Resource.loading(null))
            try {
                baseRepo.getNetworkOrderCount(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrNetworkCount.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrNetworkCount.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrNetworkCount.postValue(Resource.error(null, it))
                }
            }
        }
    }
}