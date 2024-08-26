package com.riggle.plug.ui.home.sales

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.FolderNameData
import com.riggle.plug.data.model.ResultSalesmanListing
import com.riggle.plug.data.model.SalesNetworkResponseModel
import com.riggle.plug.databinding.CustomLayoutNetworkLinkMainBinding
import com.riggle.plug.databinding.CustomLayoutSalesPersonsTableViewBinding
import com.riggle.plug.databinding.FragmentSalesBinding
import com.riggle.plug.databinding.HolderFolderNameBinding
import com.riggle.plug.databinding.PopupSalesBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.home.sales.salesInsights.SalesUserInsightsFragment
import com.riggle.plug.ui.home.sales.salesInsights.salesBeat.SalesBeatFragment
import com.riggle.plug.ui.hr.MyHRFragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SalesFragment : BaseFragment<FragmentSalesBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: SalesFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var mVerticalPagination: VerticalPagination
    private var folderList = ArrayList<FolderNameData>()

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.tvSalesPerson.text = sharedPrefManager.getCurrentUser()?.user?.company?.name

        getList()

        initSalesTableAdapter()
        setUpRecyclerViewFolder()
        initSalesPersonAdapter()

        binding.etvSalesSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (binding.clNetworkView.visibility == View.VISIBLE) {
                    filterSalesNetworkUser(charSequence.toString())
                }
                if (binding.clTableView.visibility == View.VISIBLE) {
                    filterSalesTableUser(charSequence.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    private var salesList = ArrayList<SalesNetworkResponseModel>()
    private fun filterSalesNetworkUser(text: String) {
        val filteredNames = ArrayList<SalesNetworkResponseModel>()
        for (s in salesList) {
            if (s.full_name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredNames.add(s)
            }
        }
        if (filteredNames.size == 0) {
            binding.llNoDataSalesList.visibility = View.VISIBLE
        } else {
            salesAdapter.filterList(filteredNames)
            binding.llNoDataSalesList.visibility = View.GONE
        }
    }

    private var salesTableList = ArrayList<ResultSalesmanListing>()
    private fun filterSalesTableUser(text: String) {
        val filteredNames = ArrayList<ResultSalesmanListing>()
        for (s in salesTableList) {
            if (s.full_name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredNames.add(s)
            }
        }
        if (filteredNames.size == 0) {
            binding.llNoDataSalesList.visibility = View.VISIBLE
        } else {
            salesTableAdapter.filterList(filteredNames)
            binding.llNoDataSalesList.visibility = View.GONE
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivSalesBack -> {
                    if (folderAdapter?.list?.isNotEmpty() == true && folderAdapter?.list?.size!! > 1) {
                        folderAdapter?.list?.let { list ->
                            list.removeAt(list.size - 1)
                            folderAdapter?.notifyDataSetChanged()
                            if (list.size == 1) {
                                getNetworkList("")
                            } else {
                                getNetworkList(list[list.size - 1].id.toString())
                            }
                        }
                    } else {
                        homeActivity.onBackPressed()
                    }
                }

                R.id.ivSalesTargetView -> {
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSalesTargetFragment
                    )
                }

                R.id.ivSaleShare -> {

                }

                R.id.ivSaleFilter -> {
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSalesFilterFragment
                    )
                    /*if (binding.llSalesFilter.visibility == View.VISIBLE) {
                        binding.llSalesFilter.visibility = View.GONE
                    } else {
                        binding.llSalesFilter.visibility = View.VISIBLE
                    }*/
                }

                R.id.ivSalesTableView -> {
                    binding.clTableView.visibility = View.VISIBLE
                    binding.ivSaleFilter.visibility = View.VISIBLE
                    binding.clNetworkView.visibility = View.GONE
                }

                R.id.ivSalesNetworkView -> {
                    getNetworkList("")
                    binding.clTableView.visibility = View.GONE
                    binding.ivSaleFilter.visibility = View.GONE
                    binding.clNetworkView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initObservers() {

        viewModel.obrSalesManList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.results?.size != 0) {
                        if (page == 1) {
                            salesTableAdapter.clearList()
                        }
                        if (it.data?.results?.size!! >= 15) {
                            //page++
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                            mVerticalPagination.isLoading = true
                        }
                    }
                    if (!it.data.results.isNullOrEmpty()) {
                        salesTableAdapter.addToList(it.data.results)
                        salesTableList.addAll(it.data.results as ArrayList<ResultSalesmanListing>)
                        binding.hsvRate.visibility = View.VISIBLE
                        binding.llNoDataSalesList.visibility = View.GONE
                    } else {
                        binding.hsvRate.visibility = View.GONE
                        binding.llNoDataSalesList.visibility = View.VISIBLE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrNetworkList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (!it.data.isNullOrEmpty()) {
                        salesAdapter.list = it.data
                        salesList = it.data as ArrayList<SalesNetworkResponseModel>
                        binding.hsvRate.visibility = View.VISIBLE
                        // binding.clNetworkView.visibility = View.VISIBLE
                        binding.rvNetworkDistributor.visibility = View.VISIBLE
                        binding.tvNoTeamFound.visibility = View.GONE
                    } else {
                        binding.rvNetworkDistributor.visibility = View.GONE
                        //  binding.clNetworkView.visibility = View.GONE
                        binding.hsvRate.visibility = View.GONE
                        binding.tvNoTeamFound.visibility = View.VISIBLE
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

    private var folderAdapter: SimpleRecyclerViewAdapter<FolderNameData, HolderFolderNameBinding>? =
        null

    private fun setUpRecyclerViewFolder() {
        folderAdapter = SimpleRecyclerViewAdapter<FolderNameData, HolderFolderNameBinding>(
            R.layout.holder_folder_name, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvOne -> {
                    val allFolders = ArrayList<FolderNameData>()
                    folderAdapter?.list?.let {
                        allFolders.addAll(it)
                    }
                    if (pos == 0) {
                        getNetworkList("")
                    } else {
                        getNetworkList(folderAdapter?.list?.get(pos)?.id.toString())
                    }
                    folderAdapter?.clearList()
                    for (index in allFolders.withIndex()) {
                        if (index.index <= pos) {
                            folderAdapter?.addData(allFolders[index.index])
                        }
                    }
                    folderAdapter?.notifyDataSetChanged()
                }
            }
        }
        binding.rvFolders.adapter = folderAdapter
        if (folderList.isNotEmpty()) {
            folderAdapter?.list = folderList
            folderAdapter?.list?.let { list ->
                getNetworkList(list[list.size - 1].id.toString())
            }
        } else {
            sharedPrefManager.getCurrentUser()?.let { user ->
                folderAdapter?.addData(FolderNameData(user.user.full_name, user.user.id))
            }
            sharedPrefManager.getCurrentUser()?.let { user ->
                //    getApiData(user.user.id.toString())
            }
        }
        //dataAdapter.list = getDataList()
    }

    private lateinit var salesTableAdapter: SimpleRecyclerViewAdapter<ResultSalesmanListing, CustomLayoutSalesPersonsTableViewBinding>
    private fun initSalesTableAdapter() {
        salesTableAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_sales_persons_table_view, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.iv1 -> { // Insights Click
                    SalesUserInsightsFragment.title = m.full_name
                    SalesUserInsightsFragment.userID = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSalesUserInsightsFragment
                    )
                }

                R.id.iv2 -> { // Beats Click
                    SalesBeatFragment.userId = m.id.toString()
                    SalesBeatFragment.name = m.full_name
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSalesBeatFragment
                    )
                }

                R.id.iv3 -> { // Location Click
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSaleLocationsFragment
                    )
                }

                R.id.iv4 -> { // My HR
                    MyHRFragment.userId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMyHRFragment
                    )
                }
            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvSalesPersonTableView.layoutManager = layoutManager
        binding.rvSalesPersonTableView.setItemViewCacheSize(100)
        binding.rvSalesPersonTableView.adapter = salesTableAdapter
        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvSalesPersonTableView.addOnScrollListener(mVerticalPagination)
    }

    private var salesDesignation = ""
    private var salesDesignationName = ""
    private var salesManager = ""
    private var salesManagerName = ""
    private var vanSalesUser = ""
    private fun getList() {
        if (Constants.SALES_DESIGNATION != 0){
            salesDesignation = Constants.SALES_DESIGNATION.toString()
        }else{
            salesDesignation = ""
        }
        if (Constants.SALES_DESIGNATION_Name != ""){
            salesDesignationName = Constants.SALES_DESIGNATION_Name
        }else{
            salesDesignationName = ""
        }
        if (Constants.SALES_MANAGER != 0){
            salesManager = Constants.SALES_MANAGER.toString()
        }else{
            salesManager = ""
        }
        if (Constants.SALES_MANAGER_NAME != ""){
            salesManagerName = Constants.SALES_MANAGER_NAME
        }else{
            salesManagerName = ""
        }
        if (Constants.SALES_VAN_USER){
            vanSalesUser = "true"
        }else{
            vanSalesUser = ""
        }

        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = page.toString()
        query["role"] = "sales_man"
        query["sales_designation"] = salesDesignation
        query["manager"] = salesManager
        query["van_sales"] = vanSalesUser
        query["designation_name"] = salesDesignationName
        query["manager_name"] = salesManagerName
        query["expand"] = "manager,sales_designation"
        viewModel.getSalesmanList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company!!.id,
            query
        )
    }

    private fun getNetworkList(manager: String) {
        val query = HashMap<String, String>()
        query["role"] = "sales_man"
        query["manager"] = manager
        query["expand"] = Constants.PAGE_SIZE
        viewModel.getSalesNetworkList(
            sharedPrefManager.getSessionId()!!, query
        )
    }

    private var page = 1
    override fun onLoadMore() {
        page++
        getList()
    }

    private lateinit var salesAdapter: SimpleRecyclerViewAdapter<SalesNetworkResponseModel, CustomLayoutNetworkLinkMainBinding>
    private fun initSalesPersonAdapter() {
        salesAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_network_link_main, BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.ivMoreSalesNetwork -> {
                    showPopup(v, m.id, m.full_name)
                }

                R.id.ivDistributorArrow -> {
                    folderAdapter?.addData(FolderNameData(m.full_name, m.id))
                    folderAdapter?.notifyDataSetChanged()

                    try {
                        folderAdapter?.list?.size?.let { size ->
                            if (size > 0) binding.rvFolders.layoutManager?.scrollToPosition(size - 1)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    getNetworkList(m.id.toString())
                }
            }
        }
        binding.rvNetworkDistributor.adapter = salesAdapter
    }

    private lateinit var addEditWindow: PopupWindow
    private fun showPopup(view: View, id: Int, name: String) {
        val inflater: LayoutInflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding: PopupSalesBinding =
            DataBindingUtil.inflate(inflater, R.layout.popup_sales, null, false)
        addEditWindow = PopupWindow(
            viewBinding.root, 400, 280, true
        )
        viewBinding.tvBeat.setOnClickListener {
            SalesBeatFragment.userId = id.toString()
            SalesBeatFragment.name = name
            ImageUtils.navigateWithSlideAnimations(
                findNavController(), R.id.navigateToSalesBeatFragment
            )
            addEditWindow.dismiss()
        }
        viewBinding.tvInsights.setOnClickListener {
            SalesUserInsightsFragment.title = name
            SalesUserInsightsFragment.userID = id.toString()
            ImageUtils.navigateWithSlideAnimations(
                findNavController(), R.id.navigateToSalesUserInsightsFragment
            )
            addEditWindow.dismiss()
        }

        addEditWindow.showAsDropDown(view, -130, -40, Gravity.END)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Constants.SALES_MANAGER = 0
    }

}