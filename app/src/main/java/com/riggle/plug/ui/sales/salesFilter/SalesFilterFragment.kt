package com.riggle.plug.ui.sales.salesFilter

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.DesignationSalesFilterResponseModel
import com.riggle.plug.data.model.ReportingManagerResponseModel
import com.riggle.plug.data.model.SKUs
import com.riggle.plug.databinding.FragmentSalesFilterBinding
import com.riggle.plug.databinding.HolderMainHeadingFiltersBinding
import com.riggle.plug.databinding.HolderSalesDesignationsFiltersBinding
import com.riggle.plug.databinding.HolderSalesReportManagerFiltersBinding
import com.riggle.plug.databinding.HolderSalesVanFiltersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesFilterFragment : BaseFragment<FragmentSalesFilterBinding>() {

    private val viewModel: SalesFilterFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var salesDesignation = ""
    private var salesDesignationName = ""
    private var salesManager = ""
    private var salesManagerName = ""
    private var vanSalesUser = ""

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_filter
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        if (Constants.SALES_DESIGNATION != 0){
            salesDesignation = Constants.SALES_DESIGNATION.toString()
        }
        if (Constants.SALES_DESIGNATION_Name != ""){
            salesDesignationName = Constants.SALES_DESIGNATION_Name
        }
        if (Constants.SALES_MANAGER != 0){
            salesManager = Constants.SALES_MANAGER.toString()
        }
        if (Constants.SALES_MANAGER_NAME != ""){
            salesManagerName = Constants.SALES_MANAGER_NAME
        }
        if (Constants.SALES_VAN_USER){
            vanSalesUser = "true"
        }

        initAdapter()
        initReportingManagerAdapter()
        initVanAdapter()

        viewModel.getActiveCPList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.id.toString()
        )

        viewModel.getDesignations(sharedPrefManager.getSessionId()!!)
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.iv2 -> {
                    homeActivity.onBackPressed()
                }

                R.id.tv2 -> { // clear filter
                    Constants.SALES_MANAGER = 0
                    Constants.SALES_MANAGER_NAME = ""
                    Constants.SALES_DESIGNATION = 0
                    Constants.SALES_DESIGNATION_Name = ""
                    Constants.SALES_VAN_USER = false
                    homeActivity.onBackPressed()
                }
            }
        }
    }

    private fun initObservers() {

        viewModel.obrReportingManager.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    managerAdapter.list = it.data
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrDesignations.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        val list1 = it.data
                        if (salesDesignation != "") {
                            for (i in 0..list1.size - 1) {
                                if (list1[i].id == salesDesignation.toInt()){
                                    list1[i].isSelected = true
                                }else{
                                    list1[i].isSelected = false
                                }
                            }
                        }
                        initDesignationAdapter(list1)
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    private lateinit var managerAdapter: SimpleRecyclerViewAdapter<ReportingManagerResponseModel, HolderSalesReportManagerFiltersBinding>
    private fun initReportingManagerAdapter() {
        managerAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_sales_report_manager_filters, BR.bean
        ) { v, m, pos ->
            for (i in 0..managerAdapter.list.size - 1) {
                if (i == pos) {
                    if (managerAdapter.list[i].isSelected) {
                        managerAdapter.list[i].isSelected = false
                        Constants.SALES_MANAGER = 0
                        Constants.SALES_MANAGER_NAME = ""
                    } else {
                        managerAdapter.list[i].isSelected = true
                        Constants.SALES_MANAGER = m.id
                        Constants.SALES_MANAGER_NAME = m.full_name
                    }
                } else {
                    managerAdapter.list[i].isSelected = false
                }
                managerAdapter.notifyItemChanged(i)
            }
        }
        binding.rvManager.adapter = managerAdapter
    }

    private lateinit var designationAdapter: SimpleRecyclerViewAdapter<DesignationSalesFilterResponseModel, HolderSalesDesignationsFiltersBinding>
    private fun initDesignationAdapter(data: List<DesignationSalesFilterResponseModel>?) {
        designationAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_sales_designations_filters, BR.bean
        ) { v, m, pos ->
            for (i in 0..designationAdapter.list.size - 1) {
                if (i == pos) {
                    if (designationAdapter.list[i].isSelected) {
                        designationAdapter.list[i].isSelected = false
                        Constants.SALES_DESIGNATION = 0
                        Constants.SALES_DESIGNATION_Name = ""
                    } else {
                        designationAdapter.list[i].isSelected = true
                        Constants.SALES_DESIGNATION = m.id
                        Constants.SALES_DESIGNATION_Name = m.name
                    }
                } else {
                    designationAdapter.list[i].isSelected = false
                }
                designationAdapter.notifyItemChanged(i)
            }
        }
        binding.rvDesignation.adapter = designationAdapter
        designationAdapter.list = data
    }


    private lateinit var vanAdapter: SimpleRecyclerViewAdapter<SKUs, HolderSalesVanFiltersBinding>
    private fun initVanAdapter() {
        vanAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_sales_van_filters, BR.bean
        ) { v, m, pos ->
            if (m.isSelected) {
                m.isSelected = false
                Constants.SALES_VAN_USER = false
            } else {
                m.isSelected = true
                Constants.SALES_VAN_USER = true
            }
            vanAdapter.notifyItemChanged(pos)
        }
        binding.rvSalesUser.adapter = vanAdapter
        vanAdapter.list = prepareList1()
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<SKUs, HolderMainHeadingFiltersBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_main_heading_filters, BR.bean
        ) { v, m, pos ->
            val listUpdated = prepareList()
            for (i in 0 until adapter.list.size) {
                if (pos == i) {
                    if (!listUpdated[i].isSelected) {
                        listUpdated[i].isSelected = true
                    }
                } else {
                    listUpdated[i].isSelected = false
                }
            }
            adapter.list = listUpdated
            adapter.notifyDataSetChanged()

            when (pos) {
                0 -> { // designation
                    binding.llDesignation.visibility = View.VISIBLE
                    binding.llSalesUser.visibility = View.GONE
                    binding.llManager.visibility = View.GONE
                }

                1 -> { // manager
                    binding.llDesignation.visibility = View.GONE
                    binding.llSalesUser.visibility = View.GONE
                    binding.llManager.visibility = View.VISIBLE
                }

                2 -> { // van sales user
                    binding.llDesignation.visibility = View.GONE
                    binding.llSalesUser.visibility = View.VISIBLE
                    binding.llManager.visibility = View.GONE
                }
            }
        }
        binding.rvMainHeadings.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<SKUs> {
        val list = ArrayList<SKUs>()
        list.add(SKUs("Designation", true))
        list.add(SKUs("Manager"))
        list.add(SKUs("VAN Sales User"))
        return list
    }

    private fun prepareList1(): ArrayList<SKUs> {
        val list = ArrayList<SKUs>()
        list.add(SKUs("VAN Sales Enabled"))
        return list
    }
}