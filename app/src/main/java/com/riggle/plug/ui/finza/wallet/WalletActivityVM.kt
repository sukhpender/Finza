package com.riggle.plug.ui.finza.wallet

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.UserWalletResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

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