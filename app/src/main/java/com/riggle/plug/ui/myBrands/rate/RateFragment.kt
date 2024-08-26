package com.riggle.plug.ui.myBrands.rate

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.BrandRateResult
import com.riggle.plug.databinding.CustomLayoutBrandRateBinding
import com.riggle.plug.databinding.FragmentRateBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateFragment : BaseFragment<FragmentRateBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: RateFragmentVM by viewModels()
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
        return R.layout.fragment_rate
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        initRateAdapter()
        getBrandRateList()
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

    private fun getBrandRateList() {
        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = page.toString()
        query["company"] = sharedPrefManager.getCurrentUser()!!.user.company.id.toString()
        query["brand"] = brand_id
        query["expand"] = Constants.BRAND_RATE_EXPAND
        query["fields"] = Constants.BRAND_RATE_FIELDS

        viewModel.getBrandRateList(sharedPrefManager.getSessionId()!!, query)

    }

    private fun initObservers() {
        viewModel.obrBrandRate.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data?.size != 0) {
                        if (page == 1) {
                            productRateAdapter.clearList()
                        }
                        if (it.data?.size!! >= 15) {
                            page++
                            mVerticalPagination.isLoading = false
                        } else {
                            page = 1
                            mVerticalPagination.isLoading = true
                        }
                        productRateAdapter.addToList(it.data)
                        binding.hsvRate.visibility = View.VISIBLE
                        binding.rvProductRate.visibility = View.VISIBLE
                        binding.ivNoDataFoundRate.visibility = View.GONE
                    }else{
                        binding.hsvRate.visibility = View.GONE
                        binding.rvProductRate.visibility = View.GONE
                        binding.ivNoDataFoundRate.visibility = View.VISIBLE
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

    private lateinit var productRateAdapter: SimpleRecyclerViewAdapter<BrandRateResult, CustomLayoutBrandRateBinding>
    private fun initRateAdapter() {
        productRateAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_brand_rate, BR.bean
        ) { v, m, pos ->
            when (v?.id) {

            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvProductRate.layoutManager = layoutManager
        binding.rvProductRate.setItemViewCacheSize(100)
        binding.rvProductRate.adapter = productRateAdapter

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvProductRate.addOnScrollListener(mVerticalPagination)
    }


    private var page = 1
    override fun onLoadMore() {
        getBrandRateList()
    }
}