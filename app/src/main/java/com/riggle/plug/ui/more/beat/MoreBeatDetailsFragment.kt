package com.riggle.plug.ui.more.beat

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.ResultAssignedBeatDetails
import com.riggle.plug.data.model.ResultUnAssignedBeat
import com.riggle.plug.databinding.CustomLayoutMoreAssignedBeatDetailsBinding
import com.riggle.plug.databinding.CustomLayoutMoreBeatDetailsBinding
import com.riggle.plug.databinding.FragmentMoreBeatDetailsBinding
import com.riggle.plug.databinding.PopupAddressShowBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.more.beat.insights.MoreBeatInsightsFragment
import com.riggle.plug.ui.more.beat.insights.freq.PurchaseFreqFragment
import com.riggle.plug.ui.more.beat.insights.graph.GraphFragment
import com.riggle.plug.ui.more.beat.insights.skus.InsightSKUsFragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MoreBeatDetailsFragment : BaseFragment<FragmentMoreBeatDetailsBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: MoreBeatDetailsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var mVerticalPagination: VerticalPagination

    companion object {
        var beatName = ""
        var selectedCity = ""
        var beatId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_more_beat_details
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerMoreDetailsBeats.tvHeaderTitle.text = beatName

        when (beatName) {
            "UnAssigned Retailers" -> {
                initAdapter()
                getCityBeatsList()
            }

            "Assigned Retailers" -> {
                initAssignedDetailsAdapter()
                getBeatDetails()
            }
        }


        binding.etvSearchRetailer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (beatName == "UnAssigned Retailers") {
                    filterUnAssignedRetailer(charSequence.toString())
                } else {
                    filterAssignedRetailer(charSequence.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    private var assignedRetailersList = ArrayList<ResultAssignedBeatDetails>()
    private fun filterAssignedRetailer(text: String) {
        val filteredNames = ArrayList<ResultAssignedBeatDetails>()
        for (s in assignedRetailersList) {
            if (s.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredNames.add(s)
            }
        }
        adapterDetails.filterList(filteredNames)
    }

    private var unAssignedRetailersList = ArrayList<ResultUnAssignedBeat>()
    private fun filterUnAssignedRetailer(text: String) {
        val filteredNames = ArrayList<ResultUnAssignedBeat>()
        for (s in unAssignedRetailersList) {
            if (s.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredNames.add(s)
            }
        }
        adapter.filterList(filteredNames)
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.ivHeaderSearch -> {
                    binding.etvSearchRetailer.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.obrUnAssignedBeats.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.results?.size != 0) {
                        if (page == 1) {
                            adapter.clearList()
                        }
                        if (it.data?.results?.size!! >= 15) {
                            page++
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                            mVerticalPagination.isLoading = true
                        }
                    }
                    if (!it.data.results.isNullOrEmpty()) {
                        adapter.addToList(it.data.results)
                        unAssignedRetailersList.addAll(it.data.results)
                        // list1 = adapter.list as ArrayList<ResultUnAssignedBeat>

                        binding.rvMoreBeatDetails.visibility = View.VISIBLE
                        binding.llNoDataBeatDetailsOrders.visibility = View.GONE
                    } else {
                        binding.rvMoreBeatDetails.visibility = View.GONE
                        binding.llNoDataBeatDetailsOrders.visibility = View.VISIBLE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrAssignedBeatDetails.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.results?.size != 0) {
                        if (page == 1) {
                            adapterDetails.clearList()
                        }
                        if (it.data?.results?.size!! >= 15) {
                            page++
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                            mVerticalPagination.isLoading = true
                        }
                    }
                    if (!it.data.results.isNullOrEmpty()) {
                        adapterDetails.addToList(it.data.results)
                        assignedRetailersList.addAll(it.data.results)
                        binding.rvMoreBeatDetails.visibility = View.VISIBLE
                        binding.llNoDataBeatDetailsOrders.visibility = View.GONE
                    } else {
                        binding.rvMoreBeatDetails.visibility = View.GONE
                        binding.llNoDataBeatDetailsOrders.visibility = View.VISIBLE
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

    private lateinit var adapter: SimpleRecyclerViewAdapter<ResultUnAssignedBeat, CustomLayoutMoreBeatDetailsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_more_beat_details, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvBeatUserAddress -> {
                    showAddressPopup(v, m.full_address)
                }

                R.id.ivBeatStock -> {
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMoreBeatStockFragment
                    )
                }

                R.id.ivBeatInsights -> {
                    GraphFragment.BEAT_INSIGHTS_FOR = m.id.toString()
                    PurchaseFreqFragment.BEAT_INSIGHTS_FOR = m.id.toString()
                    InsightSKUsFragment.BEAT_INSIGHTS_FOR = m.id.toString()
                    MoreBeatInsightsFragment.title = m.name
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMoreBeatInsightFragment
                    )
                }
            }
        }
        val layoutManager = LinearLayoutManager(context)
        binding.rvMoreBeatDetails.layoutManager = layoutManager
        binding.rvMoreBeatDetails.setItemViewCacheSize(100)
        binding.rvMoreBeatDetails.adapter = adapter

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvMoreBeatDetails.addOnScrollListener(mVerticalPagination)
    }

    private lateinit var adapterDetails: SimpleRecyclerViewAdapter<ResultAssignedBeatDetails, CustomLayoutMoreAssignedBeatDetailsBinding>
    private fun initAssignedDetailsAdapter() {
        adapterDetails = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_more_assigned_beat_details, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvBeatUserAddress -> {
                    showAddressPopup(v, m.full_address)
                }

                R.id.ivBeatStock -> {
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMoreBeatStockFragment
                    )
                }

                R.id.ivBeatInsights -> {
                    GraphFragment.BEAT_INSIGHTS_FOR = m.id.toString()
                    PurchaseFreqFragment.BEAT_INSIGHTS_FOR = m.id.toString()
                    InsightSKUsFragment.BEAT_INSIGHTS_FOR = m.id.toString()
                    MoreBeatInsightsFragment.title = m.name
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMoreBeatInsightFragment
                    )
                }
            }
        }
        val layoutManager = LinearLayoutManager(context)
        binding.rvMoreBeatDetails.layoutManager = layoutManager
        binding.rvMoreBeatDetails.setItemViewCacheSize(100)
        binding.rvMoreBeatDetails.adapter = adapterDetails

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvMoreBeatDetails.addOnScrollListener(mVerticalPagination)
    }

    private lateinit var addEditWindow: PopupWindow
    private fun showAddressPopup(view: View, address: String) {
        val inflater: LayoutInflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding: PopupAddressShowBinding =
            DataBindingUtil.inflate(inflater, R.layout.popup_address_show, null, false)
        addEditWindow =
            PopupWindow(viewBinding.root, 600, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)
        viewBinding.tvAddressValue.text = address
        addEditWindow.showAsDropDown(view, 0, 0, Gravity.START)
    }


    private fun getCityBeatsList() {
        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = page.toString()
        query["city"] = selectedCity
        viewModel.getCitiesList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private fun getBeatDetails() {
        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = page.toString()
        query["city"] = selectedCity
        query["beatName"] = selectedCity + "Beat"
        viewModel.getAssignedBeatDetails(sharedPrefManager.getSessionId()!!, beatId.toInt(), query)
    }

    private var page = 1
    override fun onLoadMore() {
        when (beatName) {
            "UnAssigned Retailers" -> {
                getCityBeatsList()
            }

            "Assigned Retailers" -> {
                getBeatDetails()
            }
        }
    }
}