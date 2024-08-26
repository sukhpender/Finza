package com.riggle.plug.ui.myBrands.product

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.ProductResult
import com.riggle.plug.data.model.Result
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrBrandProducts: SingleRequestEvent<List<ProductResult>> = SingleRequestEvent()

    fun getBrandProductList(
        header: String,query:Map<String, String>
    ) {
        Coroutines.io {
            obrBrandProducts.postValue(Resource.loading(null))
            try {
                baseRepo.getBrandProductList(header,query).let {
                    if (it.isSuccessful) {
                        it.body()?.results?.let { results ->
                            obrBrandProducts.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrBrandProducts.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrBrandProducts.postValue(Resource.error(null, it))
                }
            }
        }
    }
}