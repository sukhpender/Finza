package com.riggle.finza_finza.ui.finza.inventory.available

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.AssignInventoryResponseModel
import com.riggle.finza_finza.data.model.InventoryResponseModel1
import com.riggle.finza_finza.data.model.InventryResponseModel
import com.riggle.finza_finza.data.model.MultipleTransferResponseModel
import com.riggle.finza_finza.data.model.TransferRequest
import com.riggle.finza_finza.data.model.UsersListResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
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

    val obrInverntory1 = SingleRequestEvent<InventoryResponseModel1>()
    fun getInventory1(header: String,status: String,page: Int) {
        Coroutines.io {
            obrInverntory1.postValue(Resource.loading(null))
            try {
                baseRepo.getInventoriesList1(header,status,page).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrInverntory1.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrInverntory1.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrInverntory1.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrFilterInverntory = SingleRequestEvent<InventoryResponseModel1>()
    fun getFilterInventory1(header: String,status: String,from_bar_code: String,to_bar_code: String,page: Int) {
        Coroutines.io {
            obrFilterInverntory.postValue(Resource.loading(null))
            try {
                baseRepo.getFilterInventoriesList1(header,status,from_bar_code,to_bar_code,page).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrFilterInverntory.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrFilterInverntory.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrFilterInverntory.postValue(Resource.error(null, it))
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

    val obrAssignInventory1 = SingleRequestEvent<MultipleTransferResponseModel>()
    fun assignInventory1(header: String,reqBody: TransferRequest) {
        Coroutines.io {
            obrAssignInventory1.postValue(Resource.loading(null))
            try {
                baseRepo.assignInventory1(header,reqBody).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrAssignInventory1.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrAssignInventory1.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrAssignInventory1.postValue(Resource.error(null, it))
                }
            }
        }
    }
}