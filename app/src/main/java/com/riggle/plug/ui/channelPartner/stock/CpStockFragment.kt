package com.riggle.plug.ui.channelPartner.stock

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.BrandResult
import com.riggle.plug.data.model.CpStockResponseItem
import com.riggle.plug.data.model.GraphData
import com.riggle.plug.data.model.LowStockData
import com.riggle.plug.data.model.ProductCpStock
import com.riggle.plug.databinding.FragmentCpStockBinding
import com.riggle.plug.databinding.HolderCpStocksBinding
import com.riggle.plug.databinding.HolderPieDataBinding
import com.riggle.plug.databinding.ItemLowStockBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.stream.Collectors

@AndroidEntryPoint
class CpStockFragment : BaseFragment<FragmentCpStockBinding>() {

    private var homeActivity: HomeActivity? = null
    val viewModel: CpStockFragmentVM by viewModels()
    private var uniqueList = ArrayList<ProductCpStock>()
    private var allData = ArrayList<CpStockResponseItem>()
    private var brandList = ArrayList<BrandResult>()

    companion object {
        var name = ""
        var fullAddress = ""
        var companyId = ""
        var brandId = ""
        var brandName = ""
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_cp_stock
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        binding.headerCpStocks.tvHeaderTitle.text = "$name Stocks"
        homeActivity = requireActivity() as HomeActivity

        binding.tvName.text = name
        binding.tvAddress.text = fullAddress
        binding.tvBrandsName.text = brandName

        initView()
        initOnClick()
        initObservers()
    }

    private fun initView() {
        setRecyclerView()
        setupPieList()
        setupLowStock()

        binding.headerCpStocks.ivHeaderSearch.visibility = View.GONE
        if (brandId != "") {
            viewModel.getCpStocks(sharedPrefManager.getSessionId()!!, companyId, brandId)
        } else {
            viewModel.getBrandList(sharedPrefManager.getSessionId()!!, companyId)
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity?.onBackPressed()
                }

                R.id.tvBrandsName -> {
                    setPopUpMenu()
                }

                R.id.tvSelectBrand -> {
                    setPopUpMenuBrand()
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.obrBrandData.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    //showHideLoader(false)
                    brandList = try {
                        it.data?.let { list -> filterData1(list) } as ArrayList<BrandResult>
                    } catch (e: Exception) {
                        it.data as ArrayList<BrandResult>
                    }
                    if (brandList.isNotEmpty()) {
                        binding.tvSelectBrand.text = brandList[0].brand?.name

                        viewModel.getLowStockAlert(
                            sharedPrefManager.getSessionId()!!,
                            brandList[0].brand?.id.toString(),
                            companyId
                        )

                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    it.message?.let { it1 -> showErrorToast(it1) }
                }

                else -> {}
            }
        })

        viewModel.obrLowStock.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    it.data?.let { data ->
                        binding.tvTotal.text = it.data.list_data?.size.toString()
                        data.graph_data?.let { graph ->
                            try {
                                for (index in graph.withIndex()) {
                                    graph[index.index].color = getColors()[index.index]
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            if (pieAdapter?.list?.isNotEmpty() == true) {
                                pieAdapter?.clearList()
                            }
                            pieAdapter?.list = graph
                            setUpPieChart(graph)
                        }

                        if (lowStockAdapter?.list?.isNotEmpty() == true) {
                            lowStockAdapter?.clearList()
                        }
                        data.list_data?.let { list ->
                            lowStockAdapter?.list = list
                        }
                    }
                    emptyPieView()
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    emptyPieView()
                }

                else -> {}
            }
        }

        viewModel.obrSearchResult.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    it.data?.let { list ->
                        cpStockAdapter.list = list
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {
                    showHideLoader(false)
                }

            }
        }
    }

    private fun filterData1(results: List<BrandResult>): List<BrandResult> {
        return results.stream().filter(com.riggle.plug.utils.distinctByKey(BrandResult::brand))
            .collect(Collectors.toList())
    }

    private fun setPopUpMenu() {
        val popupMenu = PopupMenu(requireActivity(), binding.tvBrandsName)
        for (index in uniqueList) {
            index.brand?.id?.let { id ->
                popupMenu.menu.add(id, id, id, index.brand.name)
            }
        }
        popupMenu.setOnMenuItemClickListener { item ->
            binding.tvBrandsName.text = item?.title
            val dataList = getFilterData(item?.groupId)
            cpStockAdapter.list = dataList
            true
        }
        popupMenu.show()
    }

    private fun getFilterData(brandId: Int?): ArrayList<CpStockResponseItem> {
        val newList = ArrayList<CpStockResponseItem>()
        for (index in allData) {
            if (index.product?.brand?.id == brandId) {
                newList.add(index)
            }
        }
        return newList
    }

    private lateinit var cpStockAdapter: SimpleRecyclerViewAdapter<CpStockResponseItem, HolderCpStocksBinding>
    private fun setRecyclerView() {
        cpStockAdapter = SimpleRecyclerViewAdapter<CpStockResponseItem, HolderCpStocksBinding>(
            R.layout.holder_cp_stocks, BR.bean
        ) { v, m, pos ->
            when (v?.id) {

            }
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvStocks.layoutManager = layoutManager
        binding.rvStocks.adapter = cpStockAdapter

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), layoutManager.orientation
        )

        dividerItemDecoration.setDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireContext(), R.color.black_theme
                )
            )
        )
        binding.rvStocks.addItemDecoration(dividerItemDecoration)
    }

    private fun setPopUpMenuBrand() {
        val popupMenu = PopupMenu(requireActivity(), binding.tvSelectBrand)
        for (index in brandList) {
            index.brand?.id?.let { id ->
                popupMenu.menu.add(id, id, id, index.brand.name)
            }
        }
        popupMenu.setOnMenuItemClickListener { item ->
            binding.tvSelectBrand.text = item?.title

            viewModel.getLowStockAlert(
                sharedPrefManager.getSessionId()!!, item?.groupId.toString(), companyId
            )
            true
        }
        popupMenu.show()
    }

    private var lowStockAdapter: SimpleRecyclerViewAdapter<LowStockData, ItemLowStockBinding>? =
        null

    private fun setupLowStock() {
        val layoutManager = LinearLayoutManager(requireContext())
        lowStockAdapter = SimpleRecyclerViewAdapter(
            R.layout.item_low_stock, BR.bean
        ) { v, m, pos -> }
        binding.rvInventoryStock.layoutManager = layoutManager
        binding.rvInventoryStock.adapter = lowStockAdapter
    }

    var pieAdapter: SimpleRecyclerViewAdapter<GraphData, HolderPieDataBinding>? = null
    private fun setupPieList() {
        pieAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_pie_data, BR.bean
        ) { v, m, pos -> }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPieChartValue.layoutManager = layoutManager
        binding.rvPieChartValue.adapter = pieAdapter
    }


    private fun emptyPieView() {
        if (pieAdapter?.list?.size == 0) {
            binding.tvNoPieData.visibility = View.VISIBLE
        } else {
            binding.tvNoPieData.visibility = View.GONE
        }
        if (lowStockAdapter?.list?.size == 0) {
            binding.tvNoStockData.visibility = View.VISIBLE
        } else {
            binding.tvNoStockData.visibility = View.GONE
        }
    }

    private fun setUpPieChart(graphData: List<GraphData>) {
        try {
            binding.pieChartView.clear()

            val pieEntries: ArrayList<PieEntry> = ArrayList()
            val label = ""
            val colors: ArrayList<Int> = ArrayList()
            for (type in graphData.withIndex()) {
                graphData[type.index].percentage?.let { perc ->
                    if (perc > 0) {
                        pieEntries.add(PieEntry(perc, ""/*type*/))
                        when (type.index) {
                            0 -> {
                                colors.add(Color.parseColor("#2563EB"))
                            }

                            1 -> {
                                colors.add(Color.parseColor("#C084FC"))
                            }

                            2 -> {
                                colors.add(Color.parseColor("#FC8C4D"))
                            }

                            3 -> {
                                colors.add(Color.parseColor("#22C55E"))
                            }

                            4 -> {
                                colors.add(Color.parseColor("#F3AC23"))
                            }

                            else -> {
                                colors.add(Color.parseColor("#F3AC23"))
                            }
                        }
                    }
                }
            }
            val pieDataSet = PieDataSet(pieEntries, label)
            pieDataSet.valueTextSize = 12f
            pieDataSet.colors = colors
            pieDataSet.sliceSpace = 1.5f
            val pieData = PieData(pieDataSet)
            pieData.setDrawValues(false)

            binding.pieChartView.holeRadius = 0f
            binding.pieChartView.transparentCircleRadius = 0f
            binding.pieChartView.isDrawHoleEnabled = true
            binding.pieChartView.setDrawSlicesUnderHole(true)
            binding.pieChartView.minAngleForSlices = 10f
            binding.pieChartView.legend.isEnabled = false
            binding.pieChartView.description.isEnabled = false
            binding.pieChartView.isRotationEnabled = false
            binding.pieChartView.setTouchEnabled(false)
            binding.pieChartView.data = pieData
            binding.pieChartView.animateY(1200,Easing.Linear)

            binding.pieChartView.invalidate()

            binding.pieChartView.animateY(1200,Easing.Linear)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getColors(): ArrayList<String> {
        return arrayListOf("#2563EB", "#C084FC", "#FC8C4D", "#22C55E", "#F3AC23")
    }
}