package com.riggle.plug.ui.home.home

import android.util.Log
import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.SalesPerformanceResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesPerformanceFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {


    val obrSalesPerformance = SingleRequestEvent<List<SalesPerformanceResponseModel>>()
    fun getSalesPerformanceList(header: String,userID: String,data: HashMap<String, String>) {
        obrSalesPerformance.value = Resource.loading(null)
        Coroutines.io {
            try {
                val response = baseRepo.getSalesPerformanceList(header, userID,data)
                if (response.code() == 200 || response.code() == 201) {
                    obrSalesPerformance.postValue(
                        Resource.success(
                            response.body(),
                            response.message()
                        )
                    )
                } else {
                    obrSalesPerformance.postValue(Resource.warn(null, response.message()))
                    Log.i("Error", response.body().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                obrSalesPerformance.postValue(Resource.error(null, e.message.toString()))
                Log.i("Error", e.message.toString())
            }
        }
    }
}