package com.riggle.plug.ui.sales

import android.icu.util.Calendar
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.ActiveSalesmanResponseModel
import com.riggle.plug.databinding.CustomLayoutSalesLiveOfflineBinding
import com.riggle.plug.databinding.FragmentSalesPersonLiveOfflineBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.home.sales.salesInsights.saleLocations.SalesLocationsFragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SalesPersonLiveOfflineFragment : BaseFragment<FragmentSalesPersonLiveOfflineBinding>() {

    private val viewModel: SalesPersonLiveOfflineFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var type = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_person_live_offline
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        getList(type)
        binding.liveSalesHeader.tvHeaderTitle.text = "Salesperson"
        binding.liveSalesHeader.ivHeaderSearch.visibility = View.GONE
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.tvSelectDateSales -> {

                }

                R.id.tvSelectStatusSales -> {

                }

                R.id.tvSelectRmSales -> {

                }
            }
        }
    }

    private fun initObservers() {
        viewModel.obrInActiveSalesman.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.isNotEmpty() == true) {
                        binding.llNoDataSalesLive.visibility = View.GONE
                        binding.clSalesLive.visibility = View.VISIBLE
                        adapter.list = it.data

                        if (type == "Live Users") {
                            binding.tvLiveUsers.text = "Live Users (${it.data.size})"
                        } else {
                            binding.tvLiveUsers.text = "Offline Users (${it.data.size})"
                        }
                    } else {
                        binding.llNoDataSalesLive.visibility = View.VISIBLE
                        binding.clSalesLive.visibility = View.GONE
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

    private fun getList(title1: String) {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val query = HashMap<String, String>()

        if (title1 == "Live Users") {
            query["active"] = "true"
        } else {
            query["in_active"] = "true"
        }
        query["no-pagination"] = "true"
        query["date"] = format.format(cal.time)
        query["search"] = ""

        viewModel.getActiveSalesmanList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company!!.id,
            query
        )
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<ActiveSalesmanResponseModel, CustomLayoutSalesLiveOfflineBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_sales_live_offline, BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.tvFive -> { // view selfie

                }

                R.id.tvSix -> { // location
                    SalesLocationsFragment.id1 = m.id.toString()
                    SalesLocationsFragment.title = m.full_name
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToSaleLocationsFragment
                    )

                }
            }
        }
        binding.rvDataList.adapter = adapter
    }
}