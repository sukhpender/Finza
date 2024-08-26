package com.riggle.plug.ui.home.home.homeSKUs

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.HomeSKUsItemDetailsResponseModel
import com.riggle.plug.data.model.HomeSKUsResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeSKUsFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrSkus: SingleRequestEvent<HomeSKUsResponseModel> = SingleRequestEvent()
    fun getHomeSKUsData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrSkus.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeSKUsData(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSkus.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSkus.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSkus.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrSkusItemDetails: SingleRequestEvent<List<HomeSKUsItemDetailsResponseModel>> = SingleRequestEvent()
    fun getHomeSKUsItemDetailsData(
        header: String,id: Int, query: HashMap<String, String>
    ) {
        Coroutines.io {
            obrSkusItemDetails.postValue(Resource.loading(null))
            try {
                baseRepo.getHomeSKUsItemDetails(header,id, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSkusItemDetails.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSkusItemDetails.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSkusItemDetails.postValue(Resource.error(null, it))
                }
            }
        }
    }

}