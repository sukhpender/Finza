package com.riggle.plug.ui.finza

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.FinzaLogoutResponseModel
import com.riggle.plug.data.model.HomeInventoryResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinzaHomeActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrHomeInventory = SingleRequestEvent<HomeInventoryResponseModel>()
    fun getHomeInventoryCount(token: String) {
        Coroutines.io {
            obrHomeInventory.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeInventoryList(token).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrHomeInventory.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrHomeInventory.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrHomeInventory.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrLogout = SingleRequestEvent<FinzaLogoutResponseModel>()

    fun finzaLogout(token: String) {
        Coroutines.io {
            obrLogout.postValue(Resource.loading(null))
            try {
                baseRepo.finzaLogout(token).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrLogout.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrLogout.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrLogout.postValue(Resource.error(null, it))
                }
            }
        }
    }
}