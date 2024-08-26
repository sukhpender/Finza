package com.riggle.plug.ui.more.beat

import android.content.Context
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
import com.riggle.plug.data.model.BeatCityResponseModel
import com.riggle.plug.data.model.ResultCityBeats
import com.riggle.plug.databinding.CustomLayoutMoreBeatsBinding
import com.riggle.plug.databinding.FragmentMoreBeatBinding
import com.riggle.plug.databinding.ItemCityListBinding
import com.riggle.plug.databinding.PopupCitiesBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoreBeatFragment : BaseFragment<FragmentMoreBeatBinding>() {

    private val viewModel: MoreBeatFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var cityList = ArrayList<BeatCityResponseModel>()
    private var isLoading = false
    private var totalItemCount = 0
    private var count = 0

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        observers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_more_beat
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerMoreBeats.tvHeaderTitle.text = getString(R.string.beat)
        binding.headerMoreBeats.ivHeaderSearch.visibility = View.GONE
        initAdapter()

        cityList.clear()
        cityList.add(BeatCityResponseModel(0, "Select City", 0))
        getCitiesList()

        if (sharedPrefManager.getSelectedCity() != null) {
            selectedCityName = sharedPrefManager.getSelectedCity()!!.district
            binding.tvMoreBeats.text = sharedPrefManager.getSelectedCity()!!.district

            viewModel.getUnAssignedCount(
                sharedPrefManager.getSessionId().toString(),
                sharedPrefManager.getCurrentUser()!!.user.company.id,
                sharedPrefManager.getSelectedCity()!!.district
            )
            getCityBeatsList()
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                    sharedPrefManager.saveSelectedCity(null)
                }

                R.id.clSpinnerMoreBeats -> {
                    showCityPopup(it, true)
                    dataAdapter.list = cityList
                }

                R.id.tvMoreBeatViewUnassigned -> {
                    MoreBeatDetailsFragment.beatName = "UnAssigned Retailers"
                    MoreBeatDetailsFragment.selectedCity = selectedCityName
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMoreBeatDetailsFragment
                    )
                }
            }
        }
    }

    private fun getCitiesList() {
        val query = HashMap<String, String>()
        query["no-pagination"] = "true"
        query["manager"] = ""
        viewModel.getCitiesList(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()!!.user.company.id,
            query
        )
    }

    private fun observers() {

        viewModel.obrCities.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (!it.data.isNullOrEmpty()) {
                        cityList.clear()
                        cityList.addAll(it.data)
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrUnAssignedCount.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    if (this::addEditWindow.isInitialized) {
                        addEditWindow.dismiss()
                    }
                    showHideLoader(false)
                    binding.tvMoreBeatNameAssignedStatus.text =
                        "Unassigned Retailer (${it.data?.count})"
                    if (it.data?.count == 0) {
                        binding.tvMoreBeatViewUnassigned.visibility = View.GONE
                    } else {
                        binding.tvMoreBeatViewUnassigned.visibility = View.VISIBLE
                    }

                    showCityPopup(binding.clSpinnerMoreBeats, false)
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrCityBeatsList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    isLoading = false

                    count = it.data?.count!!
                    it.data.results?.let {
                        if (it.isNotEmpty() && it.size >= 15) {

                            isLoading = false
                        }
                        if (page == 1) {
                            adapter.list.clear()
                            adapter.list = it
                            totalItemCount = adapter.list.size
                        } else {
                            adapter.addToList(it)
                            totalItemCount = adapter.list.size
                        }

                    }
                    if (!it.data.results.isNullOrEmpty()) {
                        // adapter.addToList(it.data?.results)
                        binding.rvMoreBeats.visibility = View.VISIBLE
                        binding.llNoDataFoundMoreBeats.visibility = View.GONE
                    } else {
                        binding.rvMoreBeats.visibility = View.GONE
                        binding.llNoDataFoundMoreBeats.visibility = View.VISIBLE
                    }
                }

                Status.ERROR -> {
                    isLoading = false
                    showHideLoader(false)
                    if (it.message?.contains("Invalid page.") == false) {
                        showErrorToast(it.message.toString())
                    }
                }

                else -> {}
            }
        }

        binding.nsvBeats.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val totalHeight = binding.clBeats.height
            val scrollViewHeight = binding.nsvBeats.height
            val diff = totalHeight - scrollViewHeight
            val buffer = 100  // Load more buffer

            if (!isLoading && scrollY >= (diff - buffer)) {
                // Reached end of ScrollView, load more data
                onLoadMore()
            }
        }
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<ResultCityBeats, CustomLayoutMoreBeatsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_more_beats, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvMoreBeatView -> {
                    MoreBeatDetailsFragment.beatName = "Assigned Retailers"
                    MoreBeatDetailsFragment.selectedCity = selectedCityName
                    MoreBeatDetailsFragment.beatId = m.id.toString()
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMoreBeatDetailsFragment
                    )
                }
            }
        }
        binding.rvMoreBeats.adapter = adapter
    }

    private lateinit var addEditWindow: PopupWindow
    private lateinit var dataAdapter: SimpleRecyclerViewAdapter<BeatCityResponseModel, ItemCityListBinding>
    private fun showCityPopup(view: View, show: Boolean) {
        val inflater: LayoutInflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding: PopupCitiesBinding =
            DataBindingUtil.inflate(inflater, R.layout.popup_cities, null, false)
        addEditWindow = PopupWindow(
            viewBinding.root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        dataAdapter = SimpleRecyclerViewAdapter<BeatCityResponseModel, ItemCityListBinding>(
            R.layout.item_city_list, BR.bean
        ) { v, m, pos ->
            if (m.district == "Select City") {
                cityList.remove(BeatCityResponseModel(0, "Select City", 0))
            } else {
                sharedPrefManager.saveSelectedCity(m)
                viewModel.getUnAssignedCount(
                    sharedPrefManager.getSessionId().toString(),
                    sharedPrefManager.getCurrentUser()!!.user.company.id,
                    m.district
                )

                selectedCityName = m.district
                binding.tvMoreBeats.text = m.district
                getCityBeatsList()
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rcCity.layoutManager = layoutManager
        viewBinding.rcCity.adapter = dataAdapter

        addEditWindow.showAsDropDown(view, 0, 10, Gravity.START)
    }

    private var selectedCityName = ""
    private fun getCityBeatsList() {
        isLoading = false

        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = page.toString()
        query["city"] = selectedCityName
        query["manager"] = ""
        viewModel.getCityBeatsList(sharedPrefManager.getSessionId()!!, query)
    }

    private var page = 1
    fun onLoadMore() {
        if (totalItemCount + 1 < count) {
            page++
            isLoading = true
            getCityBeatsList()
        }
    }

    override fun onDetach() {
        super.onDetach()
        sharedPrefManager.saveSelectedCity(null)
    }
}