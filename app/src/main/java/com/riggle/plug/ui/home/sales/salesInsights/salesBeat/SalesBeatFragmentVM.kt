package com.riggle.plug.ui.home.sales.salesInsights.salesBeat

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.SalesBeatCountResponseModel
import com.riggle.plug.data.model.SalesBeatResponseModel
import com.riggle.plug.data.model.SalesUserListResponseModel
import com.riggle.plug.data.model.SalesmanListingResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesBeatFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrSalesBeatList: SingleRequestEvent<SalesBeatResponseModel> = SingleRequestEvent()

    fun getSalesmanList(
        header: String, userId: Int
    ) {
        Coroutines.io {
            obrSalesBeatList.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesBeatsList(header, userId).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesBeatList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesBeatList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesBeatList.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSalesCount: SingleRequestEvent<SalesBeatCountResponseModel> = SingleRequestEvent()

    fun getSalesCount(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrSalesCount.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesCount(header, companyId.toString(), query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSalesCount.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSalesCount.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSalesCount .postValue(Resource.error(null, it))
                }
            }
        }
    }
}