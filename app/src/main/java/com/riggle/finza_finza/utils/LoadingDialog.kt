package com.riggle.finza_finza.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.riggle.finza_finza.R

class LoadingDialog(context: Context?) : Dialog(context!!) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        //rl_main.isEnabled = false
        setCancelable(false)
    }
}