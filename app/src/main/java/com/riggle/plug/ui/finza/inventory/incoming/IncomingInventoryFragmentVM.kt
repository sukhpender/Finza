package com.riggle.plug.ui.finza.inventory.incoming

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.AssignInventoryResponseModel
import com.riggle.plug.data.model.InventryResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IncomingInventoryFragmentVM @Inject constructor(private val baseRepo: BaseRepo) :
    BaseViewModel() {

    val obrInverntory = SingleRequestEvent<InventryResponseModel>()

    fun getInventory(header: String, status: String) {
        Coroutines.io {
            obrInverntory.postValue(Resource.loading(null))
            try {
                baseRepo.getInventoriesList(header, status).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrInverntory.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrInverntory.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrInverntory.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrAcceptReject = SingleRequestEvent<AssignInventoryResponseModel>()
    fun acceptReject(header: String, inv_id: String, status: String) {
        Coroutines.io {
            obrAcceptReject.postValue(Resource.loading(null))
            try {
                baseRepo.acceptRejectInventory(header, inv_id, status).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrAcceptReject.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrAcceptReject.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrAcceptReject.postValue(Resource.error(null, it))
                }
            }
        }
    }
}