package com.riggle.plug.ui.home.home.homeLeaderBoard

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.CPLeaderBoardResponseModel
import com.riggle.plug.data.model.HomeLeaderBoardSPResponseModel
import com.riggle.plug.databinding.DialogReportBinding
import com.riggle.plug.databinding.FragmentHomeLeaderBoardBinding
import com.riggle.plug.databinding.HolderLbCpPersonsBinding
import com.riggle.plug.databinding.HolderLbSalesPersonsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.BaseCustomDialog
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeLeaderBoardFragment : BaseFragment<FragmentHomeLeaderBoardBinding>() {

    private val viewModel: HomeLeaderBoardFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var brandsId = ""
    private var month = ""
    private var year = ""

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home_leader_board
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        month = Constants.selectedMonth
        year = Constants.selectedYear

        initAdapter()
        initCpAdapter()

        brandsId = ""
        brandsId = sharedPrefManager.getBrandsId().toString()

        getSalesMen(brandsId, month, year, sharedPrefManager.getOrderType()!!)
        getCP(brandsId, month, year, sharedPrefManager.getOrderType()!!)
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvTargetReport -> {
                    if (sharedPrefManager.getCurrentUser()?.user?.email != null) {
                        viewModel.getReport(sharedPrefManager.getSessionId()!!)
                    } else {
                        showErrorToast("Email Not Found")
                    }
                }

                R.id.tvDesignation -> {

                }

                R.id.tvTargetReport -> {

                }
            }
        }
    }

    private var dialog: BaseCustomDialog<DialogReportBinding>? = null
    private fun showDialog(msg: String) {
        dialog = BaseCustomDialog<DialogReportBinding>(
            requireContext(), R.layout.dialog_report
        ) { view ->
            when (view?.id) {
                R.id.tvClose -> {
                    dialog?.dismiss()
                }
            }
        }
        dialog?.binding?.tv1?.text = msg
        dialog?.setCancelable(false)
        dialog?.show()
    }

    private fun getCP(brands: String, month: String, year: String, orderType: String) {
        val query = HashMap<String, String>()
        query["month"] = month
        query["year"] = year
        query["order_type"] = orderType
        query["brands"] = brands
        query["state"] = ""
        query["city"] = ""
        query["salesman"] = ""
        query["channel_partner"] = ""
        viewModel.getCPLeaderBoardList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toString(),
            query
        )
    }

    private fun getSalesMen(brands: String, month: String, year: String, orderType: String) {
        val query = HashMap<String, String>()
        query["month"] = month
        query["year"] = year
        query["order_type"] = orderType
        query["brands"] = brands
        query["state"] = ""
        query["city"] = ""
        query["salesman"] = ""
        query["channel_partner"] = ""
        viewModel.getHomeLeaderBoardSalesPersons(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun initObservers() {

        viewModel.obrReport.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    it.data?.Message?.let { it1 -> showDialog(it1) }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrCP.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        cpAdapter.list = it.data
                        binding.iv1.visibility = View.GONE
                    } else {
                        binding.iv1.visibility = View.VISIBLE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrSalesPerson.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        adapter.list = it.data
                        binding.iv1.visibility = View.GONE
                    } else {
                        binding.iv1.visibility = View.VISIBLE
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

    private lateinit var adapter: SimpleRecyclerViewAdapter<HomeLeaderBoardSPResponseModel, HolderLbSalesPersonsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_lb_sales_persons, BR.bean
        ) { v, m, pos ->

        }
        binding.rvSalesPersons.adapter = adapter
    }

    private lateinit var cpAdapter: SimpleRecyclerViewAdapter<CPLeaderBoardResponseModel, HolderLbCpPersonsBinding>
    private fun initCpAdapter() {
        cpAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_lb_cp_persons, BR.bean
        ) { v, m, pos ->

        }
        binding.rvSalesPersons1.adapter = cpAdapter
    }
}