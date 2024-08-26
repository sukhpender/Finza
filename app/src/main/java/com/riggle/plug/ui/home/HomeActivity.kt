package com.riggle.plug.ui.home

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.databinding.ActivityHomeBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.TimeZone

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val viewModel: HomeActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_home
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {/*window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)*/
        initNavController()

        // Order Type Filter
        sharedPrefManager.saveOrderType("primary")

        // Brand Id's Filter
        val brandsList = sharedPrefManager.getCurrentUser()?.user?.brands
        var brandsId = ""
        if (!brandsList.isNullOrEmpty()) {
            for (i in brandsList.indices) {
                brandsList[i].isSelected = true
            }
            sharedPrefManager.saveBrandsList(brandsList)
            if (brandsList.isNotEmpty()) {
                for (i in 0..brandsList.size - 1) {
                    if (brandsList[i].isSelected) {
                        if (brandsList.size - 1 != i) {
                            brandsId += brandsList[i].id.toString() + ","
                        } else {
                            brandsId += brandsList[i].id.toString()
                        }
                    }
                }
                sharedPrefManager.saveBrandsIdString(brandsId)
            }
        } else {
            sharedPrefManager.saveBrandsIdString("")
        }

        // Start & End Date Filter
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

        sharedPrefManager.saveStartDate("$currentYear-$currentMonth-01")
        sharedPrefManager.saveEndDate("$currentYear-$currentMonth-$currentDay")

        // LeaderBoard month year
        Constants.selectedYear = currentYear.toString()
        Constants.selectedMonth = currentMonth.toString()
        // State Filter


        // City Filter


        // Channel Partner Filter
        sharedPrefManager.saveCP("")

        // Sales Person Filter
        sharedPrefManager.saveSalesPerson("")

        // tab selection Home Frag
        Constants.selectedTab = "1" // for overView tab
    }

    private fun initNavController() {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_brands,
                R.id.navigation_home_arena,
                R.id.navigation_home_network_orders,
                R.id.navigation_home_more
            )
        )
        supportActionBar?.hide()
        setSupportActionBar(binding.tbHome)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.homeNavView.setupWithNavController(navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPrefManager.saveSelectedCity(null)
        sharedPrefManager.saveCP(null)
    }
}