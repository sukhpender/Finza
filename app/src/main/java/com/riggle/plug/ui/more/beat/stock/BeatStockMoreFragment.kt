package com.riggle.plug.ui.more.beat.stock

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.MoreBeatDetails
import com.riggle.plug.data.model.MoreBeatStock
import com.riggle.plug.databinding.CustomLayoutMoreBeatDetailsBinding
import com.riggle.plug.databinding.CustomLayoutMoreBeatStockBinding
import com.riggle.plug.databinding.FragmentBeatStockMoreBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeatStockMoreFragment : BaseFragment<FragmentBeatStockMoreBinding>() {

    private val viewModel: BeatStockMoreFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_beat_stock_more
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerBeatStockMore.tvHeaderTitle.text = getString(R.string.history)
        binding.headerBeatStockMore.ivHeaderSearch.visibility = View.GONE

        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }
                R.id.tvSelectDateBeatBrand -> {

                }
                R.id.tvSelectDateBeatStock -> {

                }
            }
        }
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<MoreBeatStock, CustomLayoutMoreBeatStockBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_more_beat_stock, BR.bean
        ) { v, m, pos ->


        }
        binding.rvMoreBeatDetails.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<MoreBeatStock> {
        val dataList = ArrayList<MoreBeatStock>()
        dataList.add(MoreBeatStock("Party Pack"))
        dataList.add(MoreBeatStock("Limited Edition"))
        dataList.add(MoreBeatStock("Party Pack"))
        dataList.add(MoreBeatStock("Limited Edition"))
        dataList.add(MoreBeatStock("Party Pack"))
        dataList.add(MoreBeatStock("Limited Edition"))
        return dataList
    }
}