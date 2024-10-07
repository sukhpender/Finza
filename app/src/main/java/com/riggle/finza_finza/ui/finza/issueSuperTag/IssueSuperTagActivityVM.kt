package com.riggle.finza_finza.ui.finza.issueSuperTag

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.CheckTagAvailabilityResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IssueSuperTagActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrCheckTagAvailable = SingleRequestEvent<CheckTagAvailabilityResponseModel>()

    fun checkAvailability(header: String, tag_id: String) {
        Coroutines.io {
            obrCheckTagAvailable.postValue(Resource.loading(null))
            try {
                baseRepo.checkTagAvailable(header, tag_id).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCheckTagAvailable.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCheckTagAvailable.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCheckTagAvailable.postValue(Resource.error(null, it))
                }
            }
        }
    }
}