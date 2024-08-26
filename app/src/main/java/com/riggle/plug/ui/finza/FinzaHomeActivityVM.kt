package com.riggle.plug.ui.finza

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinzaHomeActivityVM @Inject constructor(private val baseRepo: BaseRepo): BaseViewModel() {

}