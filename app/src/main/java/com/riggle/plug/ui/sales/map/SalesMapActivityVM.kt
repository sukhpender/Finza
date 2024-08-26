package com.riggle.plug.ui.sales.map

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.SalesLocationResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesMapActivityVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrSalesLocations: SingleRequestEvent<SalesLocationResponseModel> =
        SingleRequestEvent()

    fun getSalesLocations(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrSalesLocations.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesLocations(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesLocations.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesLocations.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesLocations.postValue(Resource.error(null, e.localizedMessage))
                }
            }
        }
    }
}