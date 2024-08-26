package com.riggle.plug.ui.hr

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.GetHRResponseList
import com.riggle.plug.data.model.GetLeaveCountData
import com.riggle.plug.data.model.GetLeaveData
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyHRFragmentVM @Inject constructor(val baseRepo: BaseRepo) : BaseViewModel() {


/*
    var obrAddLeave = SingleRequestEvent<BaseResponse>()

    fun addLeave(authorization: String, request: AddLeaveRequest) {
        Coroutines.io {
            obrAddLeave.postValue(Resource.loading(null))
            try {
                baseRepo.addLeave(authorization, request).let {
                    if (it.isSuccessful) {
                        it.body().let { results ->
                            obrAddLeave.postValue(
                                Resource.success(
                                    results,
                                    "Leave Created Successfully..."
                                )
                            )
                        }
                    } else {
                        obrAddLeave.postValue(Resource.warn(null, it.body().toString()))
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    if (e.hashCode() != 401) {
                        obrAddLeave.postValue(Resource.error(null, it))
                    }
                }
            }
        }
    }
*/

    var obrLeaveList = SingleRequestEvent<GetHRResponseList>()
    fun getLeaveList(header: String, request: HashMap<String, String>) {
        Coroutines.io {
            obrLeaveList.postValue(Resource.loading(null))
            try {
                baseRepo.getLeaveList(header,request).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrLeaveList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrLeaveList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrLeaveList.postValue(Resource.error(null, it))
                }
            }
        }
    }

/*
    fun updateLeave(authorization: String, id: String, request: AddLeaveRequest) {
        Coroutines.io {
            obrAddLeave.postValue(Resource.loading(null))
            try {
                baseRepo.updateLeave(authorization, id, request).let {
                    if (it.isSuccessful) {
                        it.body().let { results ->
                            obrAddLeave.postValue(
                                Resource.success(
                                    results,
                                    "Leave Updated Successfully..."
                                )
                            )
                        }
                    } else {
                        obrAddLeave.postValue(Resource.warn(null, it.body().toString()))
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    if (e.hashCode() != 401) {
                        obrAddLeave.postValue(Resource.error(null, it))
                    }
                }
            }
        }
    }
*/

/*
    fun cancelLeave(authorization: String, id: String) {
        Coroutines.io {
            obrAddLeave.postValue(Resource.loading(null))
            try {
                baseRepo.cancelLeave(authorization, id).let {
                    if (it.isSuccessful) {
                        it.body().let { results ->
                            obrAddLeave.postValue(
                                Resource.success(
                                    results,
                                    "Leave Cancelled Successfully..."
                                )
                            )
                        }
                    } else {
                        obrAddLeave.postValue(Resource.warn(null, it.body().toString()))
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    if (e.hashCode() != 401) {
                        obrAddLeave.postValue(Resource.error(null, it))
                    }
                }
            }
        }
    }
*/

    var obrLeaveCount = SingleRequestEvent<GetLeaveCountData>()
    fun getLeaveCount(authorization: String, month: String, year: String) {
        Coroutines.io {
            obrLeaveCount.postValue(Resource.loading(null))
            try {
                baseRepo.getLeaveCount(authorization, month, year).let {
                    if (it.isSuccessful) {
                        it.body().let { results ->
                            obrLeaveCount.postValue(
                                Resource.success(
                                    results,
                                    "Successfully..."
                                )
                            )
                        }
                    } else {
                        obrLeaveCount.postValue(Resource.warn(null, it.body().toString()))
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    if (e.hashCode() != 401) {
                        obrLeaveCount.postValue(Resource.error(null, it))
                    }
                }
            }
        }
    }

}