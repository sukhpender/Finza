package com.riggle.finza_finza.ui.finza.issuance

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments: MutableList<Fragment> = ArrayList()
    private val fragmentTitle: MutableList<String> = ArrayList()

    fun add(fragment: ArrayList<Fragment>, title: List<String>) {
        fragments.addAll(fragment)
        fragmentTitle.addAll(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitle[position]
    }
}
