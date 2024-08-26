package com.riggle.plug.ui.channelPartner

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.ActiveCPResponseModel
import com.riggle.plug.data.model.BrandOfferResult
import com.riggle.plug.data.model.LeadCPResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChannelPartnerFragmentVM @Inject constructor(private val baseRepo: BaseRepo):BaseViewModel() {

    val obrActiveCP: SingleRequestEvent<ActiveCPResponseModel> = SingleRequestEvent()

    fun getActiveCPList(
        header: String,companyId: Int, query:Map<String, String>
    ) {
        Coroutines.io {
            obrActiveCP.postValue(Resource.loading(null))
            try {
                baseRepo.getActiveCPList(header,companyId,query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrActiveCP.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrActiveCP.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrActiveCP.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrLeadCP: SingleRequestEvent<LeadCPResponseModel> = SingleRequestEvent()

    fun getLeadCPList(
        header: String, query:Map<String, String>
    ) {
        Coroutines.io {
            obrLeadCP.postValue(Resource.loading(null))
            try {
                baseRepo.getLeadCPList(header,query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrLeadCP.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrLeadCP.postValue(
                            Resource.warn(
                                null,
                                handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrLeadCP.postValue(Resource.error(null, it))
                }
            }
        }
    }
}