package com.riggle.plug.ui.home.sales.salesInsights

import com.riggle.plug.data.api.BaseRepo
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
class SalesUserInsightsFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrSalesManList: SingleRequestEvent<List<SalesUserListResponseModel>> = SingleRequestEvent()

    fun getSalesmanList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrSalesManList.postValue(Resource.loading(null))
            try {
                baseRepo.getSalesUserList(header, companyId.toString(), query).let {
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

}
