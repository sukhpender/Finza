package com.riggle.plug.ui.myBrands

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.Result
import com.riggle.plug.databinding.CustomLayoutMyBrandsBinding
import com.riggle.plug.databinding.FragmentMyBrandsBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.ui.myBrands.coupons.CouponsFragment
import com.riggle.plug.ui.myBrands.network.NetworkFragment
import com.riggle.plug.ui.myBrands.product.ProductFragment
import com.riggle.plug.ui.myBrands.rate.RateFragment
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBrandsFragment : BaseFragment<FragmentMyBrandsBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: MyBrandsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var mVerticalPagination: VerticalPagination

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_my_brands
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        initObservers()
        initAdapter()
        getBrandsList()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.ivHeaderSearch -> {

                }
            }
        }
    }

    private fun initObservers() {
        viewModel.obrCategories.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (page == 1) {
                        myBrandAdapter.clearList()
                    }
                    if (it.data?.size!! >= 15) {
                        page++
                        mVerticalPagination.isLoading = false
                    } else {
                        page = 1
                        mVerticalPagination.isLoading = true
                    }
                    if (it.data != null) {
                        myBrandAdapter.list = it.data
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
    }

    private lateinit var myBrandAdapter: SimpleRecyclerViewAdapter<Result, CustomLayoutMyBrandsBinding>
    private fun initAdapter() {
        myBrandAdapter =
            SimpleRecyclerViewAdapter(R.layout.custom_layout_my_brands, BR.bean) { v, m, pos ->
                when (v?.id) {
                    R.id.llMyBrandProducts -> {
                        ProductFragment.brand_id = m.brand.id.toString()
                        ProductFragment.brandName = m.brand.name
                        ImageUtils.navigateWithSlideAnimations(
                            findNavController(), R.id.navigateToMyBrandProductsFragment
                        )
                    }

                    R.id.llMyBrandRate -> {
                        RateFragment.brand_id = m.brand.id.toString()
                        ImageUtils.navigateWithSlideAnimations(
                            findNavController(), R.id.navigateToMyBrandProductRateFragment
                        )
                    }

                    R.id.llMyBrandNetwork -> {
                        NetworkFragment.brandId = m.brand.id.toString()
                        NetworkFragment.brandName = m.brand.name.toString()
                        ImageUtils.navigateWithSlideAnimations(
                            findNavController(), R.id.navigateToMyBrandProductNetworkFragment
                        )
                    }

                    R.id.llMyBrandOffers -> {
                        CouponsFragment.brand_id = m.brand.id.toString()
                        ImageUtils.navigateWithSlideAnimations(
                            findNavController(), R.id.navigateToMyBrandProductCouponsFragment
                        )
                    }
                }
            }

        val layoutManager = LinearLayoutManager(context)
        binding.rvMyBrands.layoutManager = layoutManager
        binding.rvMyBrands.setItemViewCacheSize(100)
        binding.rvMyBrands.adapter = myBrandAdapter

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvMyBrands.addOnScrollListener(mVerticalPagination)

    }

    private fun getBrandsList() {
        viewModel.getBrandList(
            sharedPrefManager.getSessionId()!!,
            "",
            Constants.PAGE_SIZE_BRAND,
            1,
            Constants.BRAND,
            Constants.FIELDS
        )
    }

    private var page = 1
    override fun onLoadMore() {
        getBrandsList()
    }
}