package com.riggle.plug.ui.finza.inventory.forwarded

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.InventryData
import com.riggle.plug.databinding.FragmentForwardedInventoryBinding
import com.riggle.plug.databinding.HolderForwardedInventoryBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForwardedInventoryFragment : BaseFragment<FragmentForwardedInventoryBinding>() {

    private val viewModel: ForwardedInventoryFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initAdapter()
        initOnClick()

        //  Status for  Forwarded list 0 , Status for Incoming 1 , 2 for in-hand inventory , 3 = old inventory
        viewModel.getInventory(sharedPrefManager.getToken().toString(), "0")

        viewModel.obrInverntory.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.data.isNotEmpty()) {
                            adapter.list = it.data.data
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE
                            binding.ivNoData.visibility = View.VISIBLE
                        }
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

                else -> {}
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_forwarded_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<InventryData, HolderForwardedInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_forwarded_inventory, BR.bean
        ) { v, m, pos ->

        }
        binding.rvHomeDrawer.adapter = adapter
    }
}