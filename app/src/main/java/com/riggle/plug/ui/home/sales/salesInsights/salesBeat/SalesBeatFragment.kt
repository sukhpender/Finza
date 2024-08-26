package com.riggle.plug.ui.home.sales.salesInsights.salesBeat

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.ResultSalesBeat
import com.riggle.plug.databinding.FragmentSalesBeatBinding
import com.riggle.plug.databinding.LaySalesBeatBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesBeatFragment : BaseFragment<FragmentSalesBeatBinding>() {

    private val viewModel: SalesBeatFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var name = ""
        var userId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
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

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.tv1.text = name
        binding.headerSaleBeat.tvHeaderTitle.text = "Beat"
        binding.headerSaleBeat.ivHeaderSearch.visibility = View.GONE

        initAdapter()
        viewModel.getSalesmanList(sharedPrefManager.getSessionId()!!, userId.toInt())

        val query = HashMap<String, String>()
        query["month"] = "07"
        query["year"] = "2024"
        query["salesman_id"] = userId
        viewModel.getSalesCount(sharedPrefManager.getSessionId()!!, userId.toInt(), query)


    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_beat
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initObservers() {
        viewModel.obrSalesBeatList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    //showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        adapter.list = it.data.results
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrSalesCount.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.bean = it.data
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


    private lateinit var adapter: SimpleRecyclerViewAdapter<ResultSalesBeat, LaySalesBeatBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.lay_sales_beat, BR.bean
        ) { v, m, pos ->

        }
        binding.rvSalesBeats.adapter = adapter
    }
}