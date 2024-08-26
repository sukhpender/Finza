package com.riggle.plug.ui.channelPartner.stock

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.BrandResult
import com.riggle.plug.data.model.CpStockResponseItem
import com.riggle.plug.data.model.LowStockDataResponse
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CpStockFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {
    val obrSearchResult = SingleRequestEvent<List<CpStockResponseItem>>()
    var obrBrandData = SingleRequestEvent<List<BrandResult>>()

    val obrLowStock = SingleRequestEvent<LowStockDataResponse>()
    fun getLowStockAlert(authorization: String, brand: String, companyid: String) {
        Coroutines.io {
            obrLowStock.postValue(Resource.loading(null))
            try {
                baseRepo.getLowStock(authorization, brand, companyid).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrLowStock.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrLowStock.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(),it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrLowStock.postValue(Resource.error(null, it))
                }
            }
        }
    }

    fun getCpStocks(authorization: String, company: String, bradId: String) {
        val query = HashMap<String, String>()/*query["page"] = count.toString()
        query["page_size"] = "15"
        query["fields"] =
            "id,name,pincode,admin,admin.full_name,admin.mobile,admin.email,full_address"*/
        query["company"] = company
        query["brand"] = bradId

        Coroutines.io {
            obrSearchResult.postValue(Resource.loading(null))
            try {
                baseRepo.getCpStocks(authorization, query).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrSearchResult.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrSearchResult.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrSearchResult.postValue(Resource.error(null, it))
                }
            }
        }
    }

    fun getBrandList(authorization: String, companyId: String) {
        val query = HashMap<String, String>()
        query["expand"] = "brand,company"
        query["company"] = "$companyId"
        query["page"] = "1"
        query["page_size"] = "25"
        query["cp_order"] = "true"
        Coroutines.io {
            obrBrandData.postValue(Resource.loading(null))
            try {
                baseRepo.getBrandList1(authorization, query).let {
                    if (it.isSuccessful) {
                        it.body()?.results?.let { results ->
                            obrBrandData.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrBrandData.postValue(
                            Resource.warn(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrBrandData.postValue(Resource.error(null, it))
                }
            }
        }
    }

}