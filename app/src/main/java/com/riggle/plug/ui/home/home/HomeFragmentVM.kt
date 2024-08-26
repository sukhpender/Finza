package com.riggle.plug.ui.home.home

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

    var statesList = SingleRequestEvent<List<String>>()

}