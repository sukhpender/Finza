package com.riggle.finza_finza.ui.finza.inventory

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityInventoryBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.avtivation.ActivationFragment
import com.riggle.finza_finza.ui.finza.inventory.available.AvailableInventoryFragment
import com.riggle.finza_finza.ui.finza.inventory.forwarded.ForwardedInventoryFragment
import com.riggle.finza_finza.ui.finza.inventory.incoming.IncomingInventoryFragment
import com.riggle.finza_finza.ui.finza.inventory.old.OldInventoryFragment
import com.riggle.finza_finza.ui.finza.issuance.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryActivity : BaseActivity<ActivityInventoryBinding>() {

    private val viewModel: InventoryActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, InventoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initView() {
        initViewPager()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                    ActivationFragment.isUpdatesAvailable.value = true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivationFragment.isUpdatesAvailable.value = true
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.add(fragList1(), titleList1())
        binding.viewpager.setAdapter(viewPagerAdapter)
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    private fun fragList1(): ArrayList<Fragment> {
        val fragList = ArrayList<Fragment>()
        fragList.add(ForwardedInventoryFragment())
        fragList.add(IncomingInventoryFragment())
        fragList.add(AvailableInventoryFragment())
        fragList.add(OldInventoryFragment())
        return fragList
    }

    private val titleList = ArrayList<String>()
    private fun titleList1(): ArrayList<String> {
        titleList.add("Forward")
        titleList.add("Dispatch")
        titleList.add("In Hand")
        titleList.add("Old")
        return titleList
    }
}