package com.riggle.plug.ui.finza.projectList

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.ProjectListResponseModel
import com.riggle.plug.data.model.ResetPasswordResponseModel
import com.riggle.plug.data.model.StorePaymentResponseModel
import com.riggle.plug.data.model.UpdateProjectResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectListActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrProjectList = SingleRequestEvent<ProjectListResponseModel>()

    fun getList(token:String) {
        Coroutines.io {
            obrProjectList.postValue(Resource.loading(null))
            try {
                baseRepo.getProjectList(token).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrProjectList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrProjectList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrProjectList.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrUpdateProject= SingleRequestEvent<UpdateProjectResponseModel>()
    fun updateProject(token:String,project_id:String) {
        Coroutines.io {
            obrUpdateProject.postValue(Resource.loading(null))
            try {
                baseRepo.updateProject(token,project_id).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUpdateProject.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUpdateProject.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUpdateProject.postValue(Resource.error(null, it))
                }
            }
        }
    }
}