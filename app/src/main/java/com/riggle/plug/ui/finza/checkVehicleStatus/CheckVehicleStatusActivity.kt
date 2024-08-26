package com.riggle.plug.ui.finza.checkVehicleStatus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.Drawer
import com.riggle.plug.databinding.ActivityCheckVehicleStatusBinding
import com.riggle.plug.databinding.HolderDrawerBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.FinzaHomeActivity
import com.riggle.plug.ui.finza.FinzaHomeActivityVM
import com.riggle.plug.ui.login.LoginActivity
import com.riggle.plug.ui.resetPassword.ResetPasswordActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

@AndroidEntryPoint
class CheckVehicleStatusActivity : BaseActivity<ActivityCheckVehicleStatusBinding>() {
    private val viewModel: CheckVehicleStatusActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, CheckVehicleStatusActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_check_vehicle_status
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)

        initView()
        initOnClick()
    }

    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }
            }
        }
    }


    private lateinit var adapter: SimpleRecyclerViewAdapter<Drawer, HolderDrawerBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_drawer, BR.bean
        ) { v, m, pos ->
            when(pos){

            }
        }
        binding.rvVNumber.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<Drawer> {
        val list = ArrayList<Drawer>()
        list.add(Drawer(R.drawable.wallet, "Wallet"))
        list.add(Drawer(R.drawable.inventory, "Inventory"))
        list.add(Drawer(R.drawable.issue_scanner, "Issue Super Tag"))
        list.add(Drawer(R.drawable.vehicle_status, "Check Vehicle Status"))
        list.add(Drawer(R.drawable.change_project, "Change Project"))
        list.add(Drawer(R.drawable.language, "Language"))
        list.add(Drawer(R.drawable.logout, "Logout"))
        return list
    }
}