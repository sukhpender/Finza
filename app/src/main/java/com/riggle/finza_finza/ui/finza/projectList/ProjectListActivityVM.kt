package com.riggle.finza_finza.ui.finza.projectList

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.ProjectListResponseModel
import com.riggle.finza_finza.data.model.UpdateProjectResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
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