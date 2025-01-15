package com.riggle.finza_finza.ui.finza.inventory.forwarded

import com.riggle.finza_finza.data.api.BaseRepo
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
class ForwardedDetailsActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrFilterInverntory = SingleRequestEvent<InventoryResponseModel1>()
    fun getFilterInventory1(header: String,status: String,from_bar_code: String,to_bar_code: String,page: Int,user_id: String) {
        Coroutines.io {
            obrFilterInverntory.postValue(Resource.loading(null))
            try {
                baseRepo.getFilterInventoriesList2(header,status,from_bar_code,to_bar_code,page,user_id).let {
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

    val obrCancel = SingleRequestEvent<CancelledResponseModel>()
    fun cancellInven(header: String,req: CancelRequest) {
        Coroutines.io {
            obrCancel.postValue(Resource.loading(null))
            try {
                baseRepo.cancelInventory(header,req).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCancel.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCancel.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCancel.postValue(Resource.error(null, it))
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
}