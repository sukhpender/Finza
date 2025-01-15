package com.riggle.finza_finza.ui.finza.inventory.incoming

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.AcceptRejectRequest
import com.riggle.finza_finza.data.model.AcceptRejectResponseModel
import com.riggle.finza_finza.data.model.CancelRequest
import com.riggle.finza_finza.data.model.CancelledResponseModel
import com.riggle.finza_finza.data.model.InventoryResponseModel1
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DispatchDetailsActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrFilterInverntory = SingleRequestEvent<InventoryResponseModel1>()
    fun getFilterInventory1(header: String,status: String,from_bar_code: String,to_bar_code: String,page: Int, userId:String) {
        Coroutines.io {
            obrFilterInverntory.postValue(Resource.loading(null))
            try {
                baseRepo.getFilterInventoriesList2(header,status,from_bar_code,to_bar_code,page, userId
                ).let {
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

    val obrInverntory1 = SingleRequestEvent<InventoryResponseModel1>()
    fun getInventory1(header: String,status: String,page: Int,user_id:Int) {
        Coroutines.io {
            obrInverntory1.postValue(Resource.loading(null))
            try {
                baseRepo.getInventoriesList2(header,status,page,user_id).let {
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

    val obrAcceptReject = SingleRequestEvent<AcceptRejectResponseModel>()
    fun acceptReject(header: String,req: AcceptRejectRequest) {
        Coroutines.io {
            obrAcceptReject.postValue(Resource.loading(null))
            try {
                baseRepo.acceptReject(header,req).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrAcceptReject.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrAcceptReject.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
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