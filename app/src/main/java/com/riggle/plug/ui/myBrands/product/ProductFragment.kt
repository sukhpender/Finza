package com.riggle.plug.ui.myBrands.product

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.ProductResult
import com.riggle.plug.databinding.CustomLayoutMyBrandProductBinding
import com.riggle.plug.databinding.FragmentProductBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.VerticalPagination
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: ProductFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var mVerticalPagination: VerticalPagination

    companion object {
        var brand_id = ""
        var brandName = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_product
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerMyBrandProducts.tvHeaderTitle.text = brandName

        initMyBrandProductsAdapter()
        getProductList()

        viewModel.obrBrandProducts.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)

                    if (page == 1) {
                        myBrandProductAdapter.clearList()
                    }
                    if (it.data?.size!! >= 15) {
                        page++
                        mVerticalPagination.isLoading = false
                    } else {
                        page = 1
                        mVerticalPagination.isLoading = true
                    }

                    if (it.data.isNotEmpty()) {
                        myBrandProductAdapter.addToList(it.data)
                        binding.ivNoDataFound.visibility = View.GONE
                        binding.rvMyBrandProducts.visibility = View.VISIBLE
                    } else {
                        binding.ivNoDataFound.visibility = View.VISIBLE
                        binding.rvMyBrandProducts.visibility = View.GONE
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
                    activity?.onBackPressed()
                }
            }
        }
    }

    private lateinit var myBrandProductAdapter: SimpleRecyclerViewAdapter<ProductResult, CustomLayoutMyBrandProductBinding>
    private fun initMyBrandProductsAdapter() {
        myBrandProductAdapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_my_brand_product, BR.bean
        ) { v, m, pos ->
            when (v?.id) {

            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvMyBrandProducts.layoutManager = layoutManager
        binding.rvMyBrandProducts.setItemViewCacheSize(100)
        binding.rvMyBrandProducts.adapter = myBrandProductAdapter

        mVerticalPagination = VerticalPagination(layoutManager, 3)
        mVerticalPagination.setListener(this)
        binding.rvMyBrandProducts.addOnScrollListener(mVerticalPagination)
    }

    private fun getProductList() {
        val query = HashMap<String, String>()
        query["search"] = ""
        query["page_size"] = Constants.PAGE_SIZE
        query["page"] = page.toString()
        query["company"] = sharedPrefManager.getCurrentUser()!!.user.company.id.toString()
        query["brand"] = brand_id
        query["expand"] = "product.sub_category.belongs,brand,banner_image"
        query["fields"] =
            "id,category,product.is_active,product.update_url,product.id,banner_image.image,product.name,product.mrp,product.sub_category,product.expiry_in,product.expiry_unit,product.base_quantity,product.base_unit,product.hsn_code,product.gst,product.description,product_aim"

        viewModel.getBrandProductList(sharedPrefManager.getSessionId()!!, query)
    }

    private var page = 1
    override fun onLoadMore() {
        getProductList()
    }
}