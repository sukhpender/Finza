package com.riggle.plug.ui.finza.issuance

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityIssuanceBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.finza.issuance.bad.BadFragment
import com.riggle.plug.ui.finza.issuance.lost.LostFragment
import com.riggle.plug.ui.finza.issuance.old.OldFragment
import com.riggle.plug.ui.finza.issuance.ugly1.UglyFragment
import com.riggle.plug.ui.finza.issuance.urt.UrtFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssuanceActivity : BaseActivity<ActivityIssuanceBinding>() {

    private val viewModel: IssuanceActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, IssuanceActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_issuance
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
      //  fragList.add(UglyFragment())
     //   fragList.add(OldFragment())
        fragList.add(UrtFragment())
        fragList.add(BadFragment())
        //   fragList.add(LostFragment())
        return fragList
    }

    private val titleList = ArrayList<String>()
    private fun titleList1(): ArrayList<String> {
       // titleList.add("UGLY")
       // titleList.add("OLD")
        titleList.add("URT")
        titleList.add("BAD")
       // titleList.add("LOST")
        return titleList
    }
}