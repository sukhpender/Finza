package com.riggle.plug.ui.home.sales.salesInsights

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.SalesUserListResponseModel
import com.riggle.plug.databinding.CustomLayoutSalesUserListBinding
import com.riggle.plug.databinding.DialogInfoBinding
import com.riggle.plug.databinding.FragmentSalesUserInsightsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.home.sales.salesInsights.insights.SalesInsightsHeadFragment
import com.riggle.plug.utils.BaseCustomDialog
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesUserInsightsFragment : BaseFragment<FragmentSalesUserInsightsBinding>() {

    private val viewModel: SalesUserInsightsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
        var userID = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_user_insights
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.tvSalesPerson.text = title
        getList()
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivSalesUserBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.ivInfo -> {
                    showDialog()
                }
            }
        }
    }

    private var dialogInfo: BaseCustomDialog<DialogInfoBinding>? = null
    private fun showDialog() {
        dialogInfo = BaseCustomDialog<DialogInfoBinding>(
            requireContext(), R.layout.dialog_info
        ) { view ->
            when (view.id) {
                R.id.tvClose -> {
                    dialogInfo?.dismiss()
                }
            }
        }
        dialogInfo?.setCancelable(false)
        dialogInfo?.show()
    }

    private fun initObservers() {
        viewModel.obrSalesManList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (!it.data.isNullOrEmpty()) {
                        adapter.list = it.data
                        binding.tv8.text = (it.data.size - 1).toString()
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

    private lateinit var adapter: SimpleRecyclerViewAdapter<SalesUserListResponseModel, CustomLayoutSalesUserListBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_sales_user_list, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.clName -> { // Analysis
                    SalesInsightsHeadFragment.userId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToInsightsHeadFragment
                    )
                }
            }
        }
        binding.rvSalesPersonTableView.adapter = adapter
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["user"] = userID
        query["insights"] = "true"
        query["team_insights"] = "true"
        query["start_date"] = "2024-07-01"
        query["type"] = "retailer"

        viewModel.getSalesmanList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company!!.id,
            query
        )
    }
}