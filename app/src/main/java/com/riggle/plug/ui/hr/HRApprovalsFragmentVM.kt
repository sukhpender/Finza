package com.riggle.plug.ui.hr

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.GetHRResponseList
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HRApprovalsFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {
    var obrLeaveList = SingleRequestEvent<GetHRResponseList>()
    //  var obrStatusUpdate = SingleRequestEvent<BaseResponse>()

    fun getLeaveList(authorization: String, request: HashMap<String, String>) {
        Coroutines.io {
            obrLeaveList.postValue(Resource.loading(null))
            try {
                baseRepo.getLeaveList(authorization, request).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrLeaveList.postValue(
                                Resource.success(
                                    results, "Success..."
                                )
                            )
                        }
                    } else {
                        obrLeaveList.postValue(Resource.error(null, handleErrorResponse(it.errorBody(),it.code())))
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    if (e.hashCode() != 401) {
                        obrLeaveList.postValue(Resource.error(null, it))
                    }
                }
            }
        }
    }

    /*
        fun updateLeaveStatus(authorization: String, id: Int, request: LeaveStatusRequest) {
            Coroutines.io {
                obrStatusUpdate.postValue(Resource.loading(null))
                try {
                    baseRepo.updateLeaveStatus(authorization, id.toString(), request).let {
                        if (it.isSuccessful) {
                            it.body()?.let { results ->
                                obrStatusUpdate.postValue(
                                    Resource.success(
                                        results,
                                        "Status Updated Successfully..."
                                    )
                                )
                            }
                        } else {
                            obrStatusUpdate.postValue(Resource.warn(null, it.body().toString()))
                        }
                    }
                } catch (e: Exception) {
                    parseException(e.cause)?.let {
                        if (e.hashCode() != 401) {
                            obrStatusUpdate.postValue(Resource.error(null, it))
                        }
                    }
                }
            }
        }
    */
}