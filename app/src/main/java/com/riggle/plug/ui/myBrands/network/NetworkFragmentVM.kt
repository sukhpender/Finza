package com.riggle.plug.ui.myBrands.network

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.BrandNetworkCPCount
import com.riggle.plug.data.model.NetworkCPCount1Item
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    val obrCPCount: SingleRequestEvent<BrandNetworkCPCount> = SingleRequestEvent()
    fun getNetworkCPCount(
        header: String, brand: Int,id: Int
    ) {
        Coroutines.io {
            obrCPCount.postValue(Resource.loading(null))
            try {
                baseRepo.getBrandNetworkUserCPCount(header, brand,id).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCPCount.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCPCount.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCPCount.postValue(Resource.error(null, it))
                }
            }
        }
    }

    val obrCPCount1: SingleRequestEvent<List<NetworkCPCount1Item>> = SingleRequestEvent()
    fun getNetworkCPCount1(
        header: String,id: Int, brand: Int,company: Int
    ) {
        Coroutines.io {
            obrCPCount1.postValue(Resource.loading(null))
            try {
                baseRepo.getBrandNetworkUserCPCount1(header, id,brand,company).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrCPCount1.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrCPCount1.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrCPCount1.postValue(Resource.error(null, it))
                }
            }
        }
    }
}