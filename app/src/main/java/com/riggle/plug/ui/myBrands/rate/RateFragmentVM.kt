package com.riggle.plug.ui.myBrands.rate

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.BrandRateResult
import com.riggle.plug.data.model.Result
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrBrandRate: SingleRequestEvent<List<BrandRateResult>> = SingleRequestEvent()

    fun getBrandRateList(
        header: String,query: Map<String, String>
    ) {
        Coroutines.io {
            obrBrandRate.postValue(Resource.loading(null))
            try {
                baseRepo.getBrandRateList(header,query).let {
                    if (it.isSuccessful) {
                        it.body()?.results?.let { results ->
                            obrBrandRate.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrBrandRate.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrBrandRate.postValue(Resource.error(null, it))
                }
            }
        }
    }
}