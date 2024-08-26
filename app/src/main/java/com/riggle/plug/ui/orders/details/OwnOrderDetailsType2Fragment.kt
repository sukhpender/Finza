package com.riggle.plug.ui.orders.details

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.ChallanModel
import com.riggle.plug.data.model.ProductOwnOrderDetails
import com.riggle.plug.databinding.CustomLayoutDetailsType2Binding
import com.riggle.plug.databinding.FragmentOwnOrderDetailsType2Binding
import com.riggle.plug.databinding.ItemTeamListBinding
import com.riggle.plug.databinding.PopupChallanList1Binding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnOrderDetailsType2Fragment : BaseFragment<FragmentOwnOrderDetailsType2Binding>() {

    private val viewModel: OwnOrderDetailsType2FragmentVM by viewModels()
    lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
        var orderDetailsFor = ""
        var orderId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_own_order_details_type2
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.headerOwnOrderDetails2.tvHeaderTitle.text = title
        binding.headerOwnOrderDetails2.ivHeaderSearch.visibility = View.GONE

        when (orderDetailsFor) {
            "Outstanding" -> {
                binding.ivMoreOptionType2.visibility = View.VISIBLE
                binding.tvUploadInvoice.visibility = View.GONE
            }

            "Completed" -> {
                binding.ivMoreOptionType2.visibility = View.GONE
                binding.tvUploadInvoice.visibility = View.VISIBLE
            }
        }

        initItemsAdapter()
        getList()

        viewModel.obrOrderDetails.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    binding.bean = it.data
                    adapter.list = it.data?.products
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.ivMoreOptionType2 -> {
                    showChallanPopup(it)
                }

                R.id.tvUploadInvoice -> {

                }
            }
        }
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["expand"] = Constants.NETWORK_ORDER_DETAILS_EXPAND
        viewModel.getList(sharedPrefManager.getSessionId()!!, query, orderId.toInt())
    }


    private lateinit var adapter: SimpleRecyclerViewAdapter<ProductOwnOrderDetails, CustomLayoutDetailsType2Binding>
    private fun initItemsAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_details_type_2, BR.bean
        ) { v, m, pos ->

        }

        binding.rvOwnOrderDetailsType2.adapter = adapter
    }


    private lateinit var dataAdapter: SimpleRecyclerViewAdapter<ChallanModel, ItemTeamListBinding>
    private fun showChallanPopup(view: View) {
        val inflater: LayoutInflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding: PopupChallanList1Binding =
            DataBindingUtil.inflate(inflater, R.layout.popup_challan_list1, null, false)
        val addEditWindow = PopupWindow(
            viewBinding.root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT/*500*/,
            true
        )

        dataAdapter = SimpleRecyclerViewAdapter<ChallanModel, ItemTeamListBinding>(
            R.layout.item_team_list, BR.bean
        ) { v, m, pos ->
            when (m.name) {
                "Payment Receipt" -> {

                }
                "Collect Payment" -> {

                }
                "Upload Invoice" -> {

                }
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvDynamicData.layoutManager = layoutManager
        viewBinding.rvDynamicData.adapter = dataAdapter
        dataAdapter.list = prepareData()
        addEditWindow.showAsDropDown(view, 0, 0, Gravity.END)
    }

    private fun prepareData(): ArrayList<ChallanModel> {
        val list = ArrayList<ChallanModel>()
        list.add(ChallanModel(R.drawable.ic_payment, "Payment Receipt"))
        list.add(ChallanModel(R.drawable.ic_collect_payment, "Collect Payment"))
        list.add(ChallanModel(R.drawable.ic_upload, "Upload Invoice"))
        return list
    }
}