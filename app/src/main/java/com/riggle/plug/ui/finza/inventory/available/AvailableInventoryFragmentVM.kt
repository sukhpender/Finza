package com.riggle.plug.ui.finza.inventory.available

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.AssignInventoryResponseModel
import com.riggle.plug.data.model.InventryResponseModel
import com.riggle.plug.data.model.UsersListResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AvailableInventoryFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrInverntory = SingleRequestEvent<InventryResponseModel>()

    fun getInventory(header: String,status: String) {
        Coroutines.io {
            obrInverntory.postValue(Resource.loading(null))
            try {
                baseRepo.getInventoriesList(header,status).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrInverntory.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrInverntory.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
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

    val obrUsers = SingleRequestEvent<UsersListResponseModel>()
    fun getUsersList(header: String) {
        Coroutines.io {
            obrUsers.postValue(Resource.loading(null))
            try {
                baseRepo.finzaUsersList(header).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUsers.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUsers.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUsers.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrAssignInventory = SingleRequestEvent<AssignInventoryResponseModel>()
    fun assignInventory(header: String,inventoryId: String,assignToId: String) {
        Coroutines.io {
            obrAssignInventory.postValue(Resource.loading(null))
            try {
                baseRepo.assignInventory(header,inventoryId,assignToId).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrAssignInventory.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrAssignInventory.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrAssignInventory.postValue(Resource.error(null, it))
                }
            }
        }
    }
}