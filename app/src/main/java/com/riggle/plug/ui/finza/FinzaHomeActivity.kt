package com.riggle.plug.ui.finza

import android.app.Activity
import android.content.Intent
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.Drawer
import com.riggle.plug.databinding.ActivityFinzaHomeBinding
import com.riggle.plug.databinding.HolderDrawerBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.checkVehicleStatus.CheckVehicleStatusActivity
import com.riggle.plug.ui.finza.language.LanguageActivity
import com.riggle.plug.ui.finza.projectList.ProjectListActivity
import com.riggle.plug.ui.finza.wallet.WalletActivity
import com.riggle.plug.ui.login.LoginActivity
import com.riggle.plug.ui.resetPassword.ResetPasswordActivity
import com.riggle.plug.utils.showInfoToast
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

@AndroidEntryPoint
class FinzaHomeActivity : BaseActivity<ActivityFinzaHomeBinding>() {
    private val viewModel: FinzaHomeActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, FinzaHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_finza_home
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initView() {
        initNavController()
        initDrawerAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.tvResetPass -> {
                    startActivity(ResetPasswordActivity.newIntent(this))
                }
                R.id.llOpenCloseDrawer -> {
                    openCloseDrawer()
                }
            }
        }
    }

    private fun openCloseDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun initNavController() {
        val navController = findNavController(R.id.nav_host_fragment_activity_home_finza)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_activation, R.id.navigation_rockstars
            )
        )
        supportActionBar?.hide()
        setSupportActionBar(binding.tbHome)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.homeNavView.setupWithNavController(navController)
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<Drawer, HolderDrawerBinding>
    private fun initDrawerAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_drawer, BR.bean
        ) { v, m, pos ->
            when (pos) {
                0 -> {
                    /** wallet */
                    startActivity(WalletActivity.newIntent(this))
                }

                1 -> {
                    /*** inventory */
                    showInfoToast("Available soon!!")
                }

                2 -> {
                    /** issue super tag */

                }

                3 -> {
                    /** vehicle status */
                    startActivity(CheckVehicleStatusActivity.newIntent(this))
                }

                4 -> {
                    /** change project */
                    startActivity(ProjectListActivity.newIntent(this))
                }

                5 -> {
                    /** language */
                    startActivity(LanguageActivity.newIntent(this))
                }

                6 -> {
                    /** logout */
                    bsLogout()
                }
            }
        }
        binding.rvHomeDrawer.adapter = adapter
        adapter.list = prepareList()
    }

    private var index = 0
    private fun bsLogout() {
        val dialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bs_logout, null)
        val tvLogout = view.findViewById<TextView>(R.id.tvLogout)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val iv = view.findViewById<ImageView>(R.id.iv)
        tvLogout.setOnClickListener {
            sharedPrefManager.clear()
            val intent = LoginActivity.newIntent(this)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        iv.setOnClickListener {
            if (index < binding.drawerLayout.childCount) {
                binding.drawerLayout.removeViewAt(index)
            }
            dialog.dismiss()
        }
        tvCancel.setOnClickListener {
            if (index < binding.drawerLayout.childCount) {
                binding.drawerLayout.removeViewAt(index)
            }
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
        index = binding.drawerLayout.childCount
        Blurry.with(this).sampling(1).onto(binding.drawerLayout)
    }

    private fun prepareList(): ArrayList<Drawer> {
        val list = ArrayList<Drawer>()
        list.add(Drawer(R.drawable.wallet, "Wallet"))
        list.add(Drawer(R.drawable.inventory, "Inventory"))
        list.add(Drawer(R.drawable.issue_scanner, "Issue Super Tag"))
        list.add(Drawer(R.drawable.vehicle_status, "Check Vehicle Status"))
        list.add(Drawer(R.drawable.change_project, "Change Project"))
        list.add(Drawer(R.drawable.replace1, "Replacement Flow Process"))
        list.add(Drawer(R.drawable.vrn, "VRN Flow Process"))
        list.add(Drawer(R.drawable.language, "Language"))
        list.add(Drawer(R.drawable.logout, "Logout"))
        return list
    }
}