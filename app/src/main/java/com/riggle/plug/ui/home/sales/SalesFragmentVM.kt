package com.riggle.plug.ui.home.sales

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.SalesNetworkResponseModel
import com.riggle.plug.data.model.SalesmanListingResponseModel
import com.riggle.plug.data.model.UnAssignedBeatResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrSalesManList: SingleRequestEvent<SalesmanListingResponseModel> = SingleRequestEvent()

    fun getSalesmanList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrSalesManList.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesManList(header, companyId.toString(), query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesManList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesManList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesManList.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrNetworkList: SingleRequestEvent<List<SalesNetworkResponseModel>> = SingleRequestEvent()
    fun getSalesNetworkList(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrNetworkList.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesNetworkList(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrNetworkList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrNetworkList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrNetworkList.postValue(Resource.error(null, it))
                }
            }
        }
    }
}