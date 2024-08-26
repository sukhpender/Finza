package com.riggle.plug.ui.channelPartner

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.ResultActiveCP
import com.riggle.plug.data.model.ResultLeadCP
import com.riggle.plug.databinding.CustomLayoutPartnerActiveCpsBinding
import com.riggle.plug.databinding.CustomLayoutPartnerLeadCpsBinding
import com.riggle.plug.databinding.FragmentChannelPartnerBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.channelPartner.insights.CpInsightsFragment
import com.riggle.plug.ui.channelPartner.profile.ProfileFragment
import com.riggle.plug.ui.channelPartner.remarks.RemarksFragment
import com.riggle.plug.ui.channelPartner.stock.CpStockFragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelPartnerFragment : BaseFragment<FragmentChannelPartnerBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: ChannelPartnerFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var mVerticalPagination: VerticalPagination
    private var whoSelected = true

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_channel_partner
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerChannelPartner.tvHeaderTitle.text = getString(R.string.channel_partner)


        initActiveCpAdapter()
        initLeadCpAdapter()

        when (whoSelected) {
            true -> {
                getActiveCPList()
            }

            false -> {
                binding.rvActiveCps.visibility = View.GONE
                binding.view1.visibility = View.INVISIBLE
                binding.rvLeadCps.visibility = View.VISIBLE
                binding.view2.visibility = View.VISIBLE

                getLeadCPList()
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.tvChannelActiveCPs -> {
                    whoSelected = true
                    binding.rvActiveCps.visibility = View.VISIBLE
                    binding.rvLeadCps.visibility = View.GONE
                    binding.view2.visibility = View.INVISIBLE
                    binding.view1.visibility = View.VISIBLE

                    getActiveCPList()
                }

                R.id.tvChannelLeadCPs -> {
                    whoSelected = false
                    binding.rvActiveCps.visibility = View.GONE
                    binding.view1.visibility = View.INVISIBLE
                    binding.rvLeadCps.visibility = View.VISIBLE
                    binding.view2.visibility = View.VISIBLE

                    getLeadCPList()
                }

                R.id.tvChannelActiveCPRemark -> {

                }

                R.id.tvChannelLeadCPProfile -> {

                }
            }
        }
    }

    private fun initObservers() {
        viewModel.obrActiveCP.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (page == 1) {
                        activeCpAdapter.clearList()
                    }
                    if (it.data?.results?.size!! >= 15) {
                        page++
                        mVerticalPagination.isLoading = false
                    } else {
                        page = 1
                        mVerticalPagination.isLoading = true
                    }
                    binding.llNoDataFoundActiveCP.visibility = View.GONE
                    binding.rvActiveCps.visibility = View.VISIBLE

                    activeCpAdapter.list = it.data.results
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundActiveCP.visibility = View.VISIBLE
                }

                else -> {}
            }
        }

        viewModel.obrLeadCP.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (page == 1) {
                        leadCpAdapter.clearList()
                    }
                    if (it.data?.results?.size!! >= 15) {
                        page++
                        mVerticalPagination.isLoading = false
                    } else {
                        page = 1
                        mVerticalPagination.isLoading = true
                    }
                    if (it.data.results.isNotEmpty()){
                        binding.llNoDataFoundActiveCP.visibility = View.GONE
                        binding.rvLeadCps.visibility = View.VISIBLE

                        leadCpAdapter.list = it.data.results
                    }else{
                        binding.llNoDataFoundActiveCP.visibility = View.VISIBLE
                        binding.rvLeadCps.visibility = View.GONE

                    }

                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.llNoDataFoundActiveCP.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    private lateinit var activeCpAdapter: SimpleRecyclerViewAdapter<ResultActiveCP, CustomLayoutPartnerActiveCpsBinding>
    private fun initActiveCpAdapter() {
        activeCpAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_partner_active_cps, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvChannelActiveCPProfile -> {/*ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToMyBrandProductChannelPartnerProfileFragment
                    )*/
                }

                R.id.tvChannelActiveCPStock -> {
                    CpStockFragment.name = m.name
                    CpStockFragment.fullAddress = m.full_address
                    CpStockFragment.companyId = m.id.toString()
                    /*if (sharedPrefManager.getCurrentUser()?.user?.brands?.size != 0){
                        ViewCpStockFragment.brandName = sharedPrefManager.getCurrentUser()?.user!!.brands[0].name
                        ViewCpStockFragment.brandId = sharedPrefManager.getCurrentUser()?.user!!.brands[0].id.toString()
                    }*/
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToCPStockFragment
                    )
                }

                R.id.tvChannelActiveCPInsights -> {
                    CpInsightsFragment.title = m.name
                    CpInsightsFragment.companyId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToCPInsightsFragment
                    )
                }
            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvActiveCps.layoutManager = layoutManager
        binding.rvActiveCps.setItemViewCacheSize(100)
        binding.rvActiveCps.adapter = activeCpAdapter

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvActiveCps.addOnScrollListener(mVerticalPagination)
    }

    private lateinit var leadCpAdapter: SimpleRecyclerViewAdapter<ResultLeadCP, CustomLayoutPartnerLeadCpsBinding>
    private fun initLeadCpAdapter() {
        leadCpAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_partner_lead_cps, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvChannelActiveCPRemark -> {
                    RemarksFragment.companyId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToMyBrandProductChannelPartnerRemarksFragment
                    )
                }

                R.id.tvChannelLeadCPProfile -> {
                    ProfileFragment.model = m
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(),
                        R.id.navigateToMyBrandProductChannelPartnerProfileFragment
                    )
                }
            }
        }
        binding.rvLeadCps.adapter = leadCpAdapter
    }

    private fun getActiveCPList() {
        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = "1"
        query["belongs"] = ""
        query["expand"] = "admin,extra"

        viewModel.getActiveCPList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private fun getLeadCPList() {
        val query = HashMap<String, String>()
        query["leads"] = "true"
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = "1"
        query["expand"] = Constants.BEAT_LEAD_EXPAND
        query["fields"] = Constants.BEAT_LEAD_FIELDS

        viewModel.getLeadCPList(
            sharedPrefManager.getSessionId()!!, query
        )
    }

    private var page = 1
    override fun onLoadMore() {
        when (whoSelected) {
            true -> {
                getActiveCPList()
            }

            false -> {
                getLeadCPList()
            }
        }
    }

}