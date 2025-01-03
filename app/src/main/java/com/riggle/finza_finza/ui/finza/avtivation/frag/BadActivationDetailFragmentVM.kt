package com.riggle.finza_finza.ui.finza.avtivation.frag

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.BadResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadActivationDetailFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrListing = SingleRequestEvent<BadResponseModel>()
    fun getList(token:String,month: String,year: String) {
        Coroutines.io {
            obrListing.postValue(Resource.loading(null))
            try {
                baseRepo.badListing(token,month,year).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrListing.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrListing.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrListing.postValue(Resource.error(null, it))
                }
            }
        }
    }
}