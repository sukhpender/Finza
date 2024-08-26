package com.riggle.plug.ui.channelPartner.remarks

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.ActiveCPResponseModel
import com.riggle.plug.data.model.CpRemarksResponseModel
import com.riggle.plug.data.model.Result
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemarksFragmentVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrRemarksCP: SingleRequestEvent<CpRemarksResponseModel> = SingleRequestEvent()

    fun getRemarks(
        header: String,companyId: Int) {
        Coroutines.io {
            obrRemarksCP.postValue(Resource.loading(null))
            try {
                baseRepo.getCpRemarks(header,companyId.toString()).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrRemarksCP.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrRemarksCP.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrRemarksCP.postValue(Resource.error(null, it))
                }
            }
        }
    }
}