package com.riggle.plug.ui.myBrands

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.Result
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyBrandsFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrCategories: SingleRequestEvent<List<Result>> = SingleRequestEvent()

    fun getBrandList(
        header: String, search: String, page_size: String, page: Int, expand: String, fields: String
    ) {
        Coroutines.io {
            obrCategories.postValue(Resource.loading(null))
            try {
                baseRepo.getBrandList(header, search, page_size, page, expand, fields).let {
                    if (it.isSuccessful) {
                        it.body()?.results?.let { results ->
                            obrCategories.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCategories.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCategories.postValue(Resource.error(null, it))
                }
            }
        }
    }
}