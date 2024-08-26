package com.riggle.plug.ui.orders.details

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.OwnOrderDetailsResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnOrderDetailsType2FragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrOrderDetails: SingleRequestEvent<OwnOrderDetailsResponseModel> =
        SingleRequestEvent()

    fun getList(
        header: String, query: Map<String, String>,id: Int
    ) {
        Coroutines.io {
            obrOrderDetails.postValue(Resource.loading(null))
            try {
                baseRepo.getOwnOrderDetails(header, query,id).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrOrderDetails.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrOrderDetails.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrOrderDetails.postValue(Resource.error(null, it))
                }
            }
        }
    }
}