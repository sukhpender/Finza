package com.riggle.finza_finza.ui.finza.inWallet

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.UserWalletResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryWalletActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrWallet = SingleRequestEvent<UserWalletResponseModel>()

    fun getWallet(header: String) {
        Coroutines.io {
            obrWallet.postValue(Resource.loading(null))
            try {
                baseRepo.finzaUserWallet(header).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrWallet.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrWallet.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrWallet.postValue(Resource.error(null, it))
                }
            }
        }
    }
}