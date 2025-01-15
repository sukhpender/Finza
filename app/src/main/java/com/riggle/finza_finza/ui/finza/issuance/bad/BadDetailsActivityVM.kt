package com.riggle.finza_finza.ui.finza.issuance.bad

import com.riggle.finza_finza.data.api.BaseRepo
import com.riggle.finza_finza.data.model.BadResponseModel
import com.riggle.finza_finza.data.model.UploadDocResponseModel
import com.riggle.finza_finza.data.model.UploadDocumentResponseModel
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.Coroutines
import com.riggle.finza_finza.utils.Resource
import com.riggle.finza_finza.utils.event.SingleRequestEvent
import com.riggle.finza_finza.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class BadDetailsActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

    val obrUploadDocument = SingleRequestEvent<UploadDocResponseModel>()
    fun uploadDocument(
        header: String,
        audit_id: RequestBody,
        image: MultipartBody.Part,
        type: RequestBody,
    ) {
        Coroutines.io {
            obrUploadDocument.postValue(Resource.loading(null))
            try {
                baseRepo.uploadDocument(
                    header,
                    audit_id,
                    image,
                    type
                ).let {
                    if (it.isSuccessful) {
                        it.body()?.let { results ->
                            obrUploadDocument.postValue(Resource.success(results, "Success"))
                        }
                    } else {
                        obrUploadDocument.postValue(
                            Resource.error(
                                null, handleErrorResponse(it.errorBody(), it.code())
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                parseException(e.cause)?.let {
                    obrUploadDocument.postValue(Resource.error(null, it))
                }
            }
        }
    }
}