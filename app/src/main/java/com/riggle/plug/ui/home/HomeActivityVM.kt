package com.riggle.plug.ui.home

import com.riggle.plug.data.api.BaseRepo
import com.riggle.plug.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeActivityVM @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {
}