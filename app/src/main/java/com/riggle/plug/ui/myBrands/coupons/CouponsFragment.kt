package com.riggle.plug.ui.myBrands.coupons

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.BrandOfferResult
import com.riggle.plug.databinding.CustomLayoutNetworkCouponsBinding
import com.riggle.plug.databinding.FragmentCouponsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CouponsFragment : BaseFragment<FragmentCouponsBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: CouponsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var mVerticalPagination: VerticalPagination

    companion object {
        var brand_id = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_coupons
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerCoupons.ivHeaderSearch.visibility = View.GONE
        binding.headerCoupons.tvHeaderTitle.text = getString(R.string.coupons)

        initCouponsAdapter()
        getCouponsList()

        viewModel.obrBrandOffers.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (page == 1) {
                        couponsAdapter.clearList()
                    }
                    if (it.data?.size!! >= 15) {
                        page++
                        mVerticalPagination.isLoading = false
                    } else {
                        page = 1
                        mVerticalPagination.isLoading = true
                    }
                    Log.e("ApiSuccessMyBrandsList", it.data.toString())
                    if (!it.data.isNullOrEmpty()) {
                        couponsAdapter.list = it.data
                        binding.ivNoDataFound.visibility = View.GONE
                        binding.rvCoupons.visibility = View.VISIBLE
                    } else {
                        binding.ivNoDataFound.visibility = View.VISIBLE
                        binding.rvCoupons.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                    binding.ivNoDataFound.visibility = View.VISIBLE
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
            }
        }
    }

    private lateinit var couponsAdapter: SimpleRecyclerViewAdapter<BrandOfferResult, CustomLayoutNetworkCouponsBinding>
    private fun initCouponsAdapter() {
        couponsAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_network_coupons, BR.bean
        ) { v, m, pos ->

        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvCoupons.layoutManager = layoutManager
        binding.rvCoupons.setItemViewCacheSize(100)
        binding.rvCoupons.adapter = couponsAdapter

        mVerticalPagination =
            VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvCoupons.addOnScrollListener(mVerticalPagination)
    }

    private fun getCouponsList() {
        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = "1"
        query["brand"] = brand_id

        viewModel.getBrandOffersList(sharedPrefManager.getSessionId()!!, query)
    }

    private var page = 1
    override fun onLoadMore() {
        getCouponsList()
    }
}