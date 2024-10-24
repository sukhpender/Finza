package com.riggle.finza_finza.ui.finza.helpAndSupport.needFastag

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.HomeInventoryResponseModel
import com.riggle.finza_finza.data.model.NeedFastagResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NeedFastagActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrNeedFastag = SingleRequestEvent<NeedFastagResponseModel>()
    fun needFastag(token: String,provider:String) {
        Coroutines.io {
            obrNeedFastag.postValue(Resource.loading(null))
            try {
                baseRepo.needFastag(token,provider).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrNeedFastag.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrNeedFastag.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrNeedFastag.postValue(Resource.error(null, it))
                }
            }
        }
    }
}