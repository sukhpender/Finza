package com.riggle.finza_finza.ui.finza.inventory.forwarded

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.DispatchUsersResponseModel
import com.riggle.finza_finza.data.model.ForwardUsersResponseModel
import com.riggle.finza_finza.data.model.InventryResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForwardedInventoryFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrInverntory = SingleRequestEvent<DispatchUsersResponseModel>()

    fun getInventory(header: String,status: String) {
        Coroutines.io {
            obrInverntory.postValue(Resource.loading(null))
            try {
                baseRepo.getForwardUsersList(header,status).let {
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
}