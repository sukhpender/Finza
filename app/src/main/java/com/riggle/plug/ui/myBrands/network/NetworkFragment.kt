package com.riggle.plug.ui.myBrands.network

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.NetworkCPCount1Item
import com.riggle.plug.data.model.NetworkLinkModel
import com.riggle.plug.databinding.CustomLayoutBrandDistributorBinding
import com.riggle.plug.databinding.CustomLayoutNetworkLinkBinding
import com.riggle.plug.databinding.FragmentNetworkBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class NetworkFragment : BaseFragment<FragmentNetworkBinding>() {

    private val viewModel: NetworkFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    var formatter = DecimalFormat("00")
    private val networkLinkList = ArrayList<NetworkLinkModel>()

    companion object {
        var brandId = ""
        var brandName = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_network
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        binding.tvNetworkManufacturerName.text =
            sharedPrefManager.getCurrentUser()?.user?.company?.name
        binding.tvNetworkManufacturerId.text =
            sharedPrefManager.getCurrentUser()?.user?.company?.mobile
        binding.tvNetworkManufacturerSalesPersonValue.text =
            sharedPrefManager.getCurrentUser()?.user?.company?.number_of_salesman.toString()

        binding.headerProductNetwork.tvHeaderTitle.text = resources.getString(R.string.network)
        binding.headerProductNetwork.tvHeaderProductName.text = brandName

        networkLinkList.add(NetworkLinkModel("Sukhi", 0))

        initLinkAdapter()
        initAdapter()
        initObservers()
        getCPCount()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.ivManufacturerArrow -> {
                    getCPCount1(sharedPrefManager.getCurrentUser()!!.user.company.id)
                }
            }
        }
    }

    private fun getCPCount() {
        viewModel.getNetworkCPCount(
            sharedPrefManager.getSessionId()!!,
            brandId.toInt(),
            sharedPrefManager.getCurrentUser()!!.user.company.id
        )
    }

    private fun getCPCount1(brnId: Int) {
        viewModel.getNetworkCPCount1(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            brandId.toInt(),
            brnId
        )
    }

    private fun initObservers() {
        viewModel.obrCPCount.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.direct_channels != 0) {
                        binding.tvNetworkManufacturerDirectCPValue.text =
                            formatter.format(it.data?.direct_channels).toString()
                    } else {
                        binding.tvNetworkManufacturerDirectCPValue.text = "-"
                    }
                    if (it.data?.indirect_channels != 0) {
                        binding.tvNetworkManufacturerIndirectCPValue.text =
                            formatter.format(it.data?.indirect_channels).toString()
                    } else {
                        binding.tvNetworkManufacturerIndirectCPValue.text = "-"
                    }
                    if (it.data?.direct_channels == 0 && it.data.indirect_channels == 0) {
                        binding.ivManufacturerArrow.visibility = View.GONE
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

        viewModel.obrCPCount1.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        binding.llManufacturer.visibility = View.GONE
                        binding.rvNetworkLink.visibility = View.VISIBLE
                        binding.llDistributor.visibility = View.VISIBLE

                        productDistributorAdapter.list = it.data

                        /* if (clickedPos != 0) { */
                        if (userClicked) {
                            for (i in 0 until networkLinkList.size) {
                                if (i >= clickedPos) {
                                    networkLinkAdapter.removeItem(clickedPos)
                                    networkLinkAdapter.notifyDataSetChanged()
                                }
                            }
                        }/*}
                    *//*if (networkLinkList.get(networkLinkList.size-1).name != ""){
                            binding.tvNetworkDistributor.text = networkLinkList.get(networkLinkList.size-1).name + " Buyers"
                        }*/
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
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


    private lateinit var productDistributorAdapter: SimpleRecyclerViewAdapter<NetworkCPCount1Item, CustomLayoutBrandDistributorBinding>
    private fun initAdapter() {
        productDistributorAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_brand_distributor, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.ivDistributorArrow -> {
                    getCPCount1(m.id)
                    networkLinkAdapter.addData(NetworkLinkModel(m.name, m.id))
                    networkLinkAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.rvNetworkDistributor.adapter = productDistributorAdapter
    }

    private var clickedPos = 0
    private var userClicked = false
    private lateinit var networkLinkAdapter: SimpleRecyclerViewAdapter<NetworkLinkModel, CustomLayoutNetworkLinkBinding>
    private fun initLinkAdapter() {
        networkLinkAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_network_link, BR.bean
        ) { v, m, pos ->
            if (pos != 0) {
                userClicked
                clickedPos = pos
                getCPCount1(m.id)
            }
        }
        binding.rvNetworkLink.adapter = networkLinkAdapter
        networkLinkAdapter.list = networkLinkList
    }
}