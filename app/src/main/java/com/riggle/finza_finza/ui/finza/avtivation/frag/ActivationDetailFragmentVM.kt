package com.riggle.finza_finza.ui.finza.avtivation.frag

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.ActivationsResponseModel
import com.riggle.finza_finza.data.model.InventryResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivationDetailFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrActivations = SingleRequestEvent<ActivationsResponseModel>()
    fun getActivations(header: String,month: String) {
        Coroutines.io {
            obrActivations.postValue(Resource.loading(null))
            try {
                baseRepo.activations(header,month).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrActivations.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrActivations.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrActivations.postValue(Resource.error(null, it))
                }
            }
        }
    }
}