package com.riggle.plug.ui.finza.issueSuperTag

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.AssignInventoryResponseModel
import com.riggle.plug.data.model.CheckTagAvailabilityResponseModel
import com.riggle.plug.data.model.IssueTagCheckWalletResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
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