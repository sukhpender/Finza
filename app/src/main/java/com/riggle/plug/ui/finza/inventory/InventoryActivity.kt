package com.riggle.plug.ui.finza.inventory

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityInventoryBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.inventory.available.AvailableInventoryFragment
import com.riggle.plug.ui.finza.inventory.forwarded.ForwardedInventoryFragment
import com.riggle.plug.ui.finza.inventory.incoming.IncomingInventoryFragment
import com.riggle.plug.ui.finza.inventory.old.OldInventoryFragment
import com.riggle.plug.ui.finza.issuance.ViewPagerAdapter
import com.riggle.plug.ui.finza.issuance.bad.BadFragment
import com.riggle.plug.ui.finza.issuance.lost.LostFragment
import com.riggle.plug.ui.finza.issuance.old.OldFragment
import com.riggle.plug.ui.finza.issuance.ugly1.UglyFragment
import com.riggle.plug.ui.finza.issuance.urt.UrtFragment
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
                }
            }
        }
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