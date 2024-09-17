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
import com.riggle.plug.ui.finza.helpAndSupport.HelpAndSupportActivity
import com.riggle.plug.ui.finza.inventory.InventoryActivity
import com.riggle.plug.ui.finza.issueSuperTag.IssueSuperTagActivity
import com.riggle.plug.ui.finza.profile.ProfileActivity
import com.riggle.plug.ui.finza.projectList.ProjectListActivity
import com.riggle.plug.ui.finza.wallet.WalletActivity
import com.riggle.plug.ui.forgotPassword.ForgotPasswordActivity
import com.riggle.plug.ui.login.LoginActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showSuccessToast
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
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)

        initView()
        initOnClick()
        initObservers()
    }

    private fun initObservers() {

        viewModel.obrLogout.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)

                    it.data?.message?.let { it1 -> showSuccessToast(it1) }
                    sharedPrefManager.clearUser()
                    sharedPrefManager.clear()
                    dialog.dismiss()
                    startActivity(LoginActivity.newIntent(this))
                    finishAffinity()

                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

    }

    private fun initView() {
        initNavController()
        initDrawerAdapter()

        sharedPrefManager.getCurrentUser().let {
            binding.bean = it
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.tvResetPass -> {
                    startActivity(ForgotPasswordActivity.newIntent(this))
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
                    openCloseDrawer()
                }

                1 -> {
                    /*** inventory */
                    startActivity(InventoryActivity.newIntent(this))
                    openCloseDrawer()
                }

                2 -> {
                    /** issue tag */
                    startActivity(IssueSuperTagActivity.newIntent(this))
                }

                3 -> {
                    /** vehicle status */
                    startActivity(CheckVehicleStatusActivity.newIntent(this))
                    openCloseDrawer()
                }

                4 -> {
                    /** change project */
                    startActivity(ProjectListActivity.newIntent(this))
                    openCloseDrawer()
                }

                5 -> {
                    /** help and support */
                    startActivity(HelpAndSupportActivity.newIntent(this))
                    openCloseDrawer()
                }

//                6 -> {
//                    /** language */
//                    startActivity(LanguageActivity.newIntent(this))
//                    openCloseDrawer()
//                }

                6 -> {
                    /** profile */
                    startActivity(ProfileActivity.newIntent(this))
                    openCloseDrawer()
                }

                7 -> {
                    /** logout */
                    bsLogout()
                    openCloseDrawer()
                }
            }
        }
        binding.rvHomeDrawer.adapter = adapter
        adapter.list = prepareList()
    }

    private lateinit var dialog: BottomSheetDialog
    private var index = 0
    private fun bsLogout() {
        dialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bs_logout, null)
        val tvLogout = view.findViewById<TextView>(R.id.tvLogout)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val iv = view.findViewById<ImageView>(R.id.iv)
        tvLogout.setOnClickListener {
            viewModel.finzaLogout(sharedPrefManager.getToken().toString())
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
        list.add(Drawer(R.drawable.wallet, "My Finza Wallet"))
        list.add(Drawer(R.drawable.inventory, "Inventory"))
        list.add(Drawer(R.drawable.ic_percentage_filled, "Issue Tag"))
        list.add(Drawer(R.drawable.vehicle_status, "Check Vehicle Status"))
        list.add(Drawer(R.drawable.change_project, "Change Project"))
        list.add(Drawer(R.drawable.replace1, "Help & Support"))
        //  list.add(Drawer(R.drawable.language, "Language"))
        list.add(Drawer(R.drawable.profile, "Profile"))
        list.add(Drawer(R.drawable.logout, "Logout"))
        return list
    }
}