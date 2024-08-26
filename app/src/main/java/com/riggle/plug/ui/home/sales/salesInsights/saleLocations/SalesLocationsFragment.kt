package com.riggle.plug.ui.home.sales.salesInsights.saleLocations

import android.content.Intent
import android.icu.util.Calendar
import android.location.Location
import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.ResultSalesLocation
import com.riggle.plug.databinding.FragmentSalesLocationsBinding
import com.riggle.plug.databinding.LaySalesLocationsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.sales.map.SalesMapActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SalesLocationsFragment : BaseFragment<FragmentSalesLocationsBinding>() {

    private val viewModel: SalesLocationsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
        var id1 = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerSaleLocations.ivHeaderSearch.visibility = View.GONE
        binding.headerSaleLocations.tvHeaderTitle.text = title

        getList()
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    SalesMapActivity.id1 = id1
                    startActivity(Intent(requireContext(), SalesMapActivity::class.java))
                }
            }
        }
    }

    private fun getList() {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val query = HashMap<String, String>()
        query["id"] = id1/*
        query["date"] = format.format(cal.time)
*/
        query["date"] = "2024-06-03"
        query["expand"] = "user"
        query["fields"] = "created_at,id,latitude,longitude,timestamp,type"

        viewModel.getSalesLocations(
            sharedPrefManager.getSessionId()!!, query
        )
    }

    private var list = ArrayList<ResultSalesLocation>()
    private fun initObservers() {

        viewModel.obrSalesLocations.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    if (it.data?.results?.isNotEmpty() == true) {
                        list = it.data.results as ArrayList<ResultSalesLocation>
                        it.data.results.reverse()
                        for (i in 0 until it.data.results.size) {
                            if (it.data.results.size >= i + 2) {
                                val locationA = Location("point $i")
                                locationA.latitude = it.data.results[i].latitude
                                locationA.longitude = it.data.results[i].longitude

                                val locationB = Location("point $i+1")
                                locationB.latitude = it.data.results[i + 1].latitude
                                locationB.longitude = it.data.results[i + 1].longitude

                                val distance = (locationA.distanceTo(locationB).toInt()/1000)
                                list[i].distance = distance.toString()

                            }
                        }
                        showHideLoader(false)
                        adapter.list = list
                        binding.llNoDataSalesLive.visibility = View.GONE
                        binding.rvSaleLocations.visibility = View.VISIBLE
                    } else {
                        binding.llNoDataSalesLive.visibility = View.VISIBLE
                        binding.rvSaleLocations.visibility = View.GONE
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

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_locations
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<ResultSalesLocation, LaySalesLocationsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.lay_sales_locations, BR.bean) { v, m, pos ->

        }
        binding.rvSaleLocations.adapter = adapter
    }
}