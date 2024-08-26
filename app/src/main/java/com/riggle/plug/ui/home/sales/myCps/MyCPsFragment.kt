package com.riggle.plug.ui.home.sales.myCps

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.SKUs
import com.riggle.plug.databinding.FragmentMyCPsBinding
import com.riggle.plug.databinding.HolderMyCpsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCPsFragment : BaseFragment<FragmentMyCPsBinding>() {

    private val viewModel: MyCPsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_my_c_ps
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerMyCps.tvHeaderTitle.text = getString(R.string.my_cps)
        binding.headerMyCps.tvHeaderProductName.visibility = View.GONE

        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }
            }

        }
    }

    private fun initObservers() {

    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<SKUs, HolderMyCpsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_my_cps, BR.bean
        ) { v, m, pos ->

        }
        binding.rvMyCPs.adapter = adapter
        adapter.list = prepareList()
    }

    fun prepareList(): ArrayList<SKUs> {
        val list = ArrayList<SKUs>()
        list.add(SKUs("Sukhi"))
        list.add(SKUs("Sukhi Panghal"))
        list.add(SKUs("Sukhi Panghal Haryana"))
        list.add(SKUs("Sukhi Android"))
        list.add(SKUs("Sukhi Testing"))
        list.add(SKUs("Sukhi"))
        list.add(SKUs("Sukhi Panghal"))
        list.add(SKUs("Sukhi Panghal Haryana"))
        list.add(SKUs("Sukhi Android"))
        list.add(SKUs("Sukhi Testing"))
        return list
    }
}