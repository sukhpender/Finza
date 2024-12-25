package com.riggle.finza_finza.ui.finza.avtivation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityActivationSummaryDetailsBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.avtivation.frag.ActivationDetailFragment
import com.riggle.finza_finza.ui.finza.avtivation.frag.BadActivationDetailFragment
import com.riggle.finza_finza.ui.finza.inventory.InventoryActivity
import com.riggle.finza_finza.ui.finza.inventory.available.AvailableInventoryFragment
import com.riggle.finza_finza.ui.finza.inventory.forwarded.ForwardedInventoryFragment
import com.riggle.finza_finza.ui.finza.inventory.incoming.IncomingInventoryFragment
import com.riggle.finza_finza.ui.finza.inventory.old.OldInventoryFragment
import com.riggle.finza_finza.ui.finza.issuance.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivationSummaryDetailsActivity : BaseActivity<ActivityActivationSummaryDetailsBinding>() {

    private val viewModel: ActivationSummaryDetailsActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ActivationSummaryDetailsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_activation_summary_details
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this){
            when(it?.id){
                R.id.iv1 ->{
                    finish()
                }
            }
        }
    }

    private fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.add(fragList1(), titleList1())
        binding.viewpager.setAdapter(viewPagerAdapter)
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    private fun fragList1(): ArrayList<Fragment> {
        val fragList = ArrayList<Fragment>()
        fragList.add(ActivationDetailFragment())
        fragList.add(BadActivationDetailFragment())
        return fragList
    }

    private val titleList = ArrayList<String>()
    private fun titleList1(): ArrayList<String> {
        titleList.add("Activations")
        titleList.add("Bad Activations")
        return titleList
    }
}