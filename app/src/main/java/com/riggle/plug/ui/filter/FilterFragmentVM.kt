package com.riggle.plug.ui.filter

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.HomePageFiltersResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FilterFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrFiltersData: SingleRequestEvent<HomePageFiltersResponseModel> = SingleRequestEvent()
    fun getFiltersData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrFiltersData.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeFiltersData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrFiltersData.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrFiltersData.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrFiltersData.postValue(Resource.error(null, it))
                }
            }
        }
    }
}