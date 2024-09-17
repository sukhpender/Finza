package com.riggle.plug.ui.finza.avtivation

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.HomeInventoryResponseModel
import com.riggle.plug.data.model.IssueTagCheckWalletResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivationFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

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

    val obrIssueTagCheckWallet = SingleRequestEvent<IssueTagCheckWalletResponseModel>()
    fun issueTagCheckWallet(token: String) {
        Coroutines.io {
            obrIssueTagCheckWallet.postValue(Resource.loading(null))
            try {
                baseRepo.issueTagCheckWallet(token).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrIssueTagCheckWallet.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrIssueTagCheckWallet.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrIssueTagCheckWallet.postValue(Resource.error(null, it))
                }
            }
        }
    }
}