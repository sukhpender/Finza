package com.riggle.plug.ui.finza.inventory

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.data.model.FinzaLogoutResponseModel
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.Coroutines
import com.riggle.plug.utils.Resource
import com.riggle.plug.utils.event.SingleRequestEvent
import com.riggle.plug.utils.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {


}