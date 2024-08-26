package com.riggle.plug.ui.more.beat

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.BeatCityResponseModel
import com.riggle.plug.data.model.CityBeatsResponseModel
import com.riggle.plug.data.model.UnAssignedCountResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreBeatFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrCities: SingleRequestEvent<List<BeatCityResponseModel>> = SingleRequestEvent()

    fun getCitiesList(
        header: String, companyId: Int, query: Map<String, String>
    ) {
        Coroutines.io {
            obrCities.postValue(Resource.loading(null))
            try {
                baseRepo.getBeatCities(header, companyId, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCities.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCities.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCities.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrUnAssignedCount: SingleRequestEvent<UnAssignedCountResponseModel> = SingleRequestEvent()
    fun getUnAssignedCount(
        header: String, companyId: Int, city: String
    ) {
        Coroutines.io {
            obrCities.postValue(Resource.loading(null))
            try {
                baseRepo.getUnAssignedCount(header, companyId, city).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUnAssignedCount.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUnAssignedCount.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUnAssignedCount.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrCityBeatsList: SingleRequestEvent<CityBeatsResponseModel> = SingleRequestEvent()
    fun getCityBeatsList(
        header: String, query: Map<String, String>
    ) {
        Coroutines.io {
            obrCities.postValue(Resource.loading(null))
            try {
                baseRepo.getCityBeatsList(header, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCityBeatsList.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCityBeatsList.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCityBeatsList.postValue(Resource.error(null, it))
                }
            }
        }
    }
}