package com.riggle.plug.ui.finza.profile

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.FinzaProfileResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrProfile = SingleRequestEvent<FinzaProfileResponseModel>()

    fun finzaGetProfile(token: String) {
        Coroutines.io {
            obrProfile.postValue(Resource.loading(null))
            try {
                baseRepo.finzaGetProfile(token).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrProfile.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrProfile.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrProfile.postValue(Resource.error(null, it))
                }
            }
        }
    }
}