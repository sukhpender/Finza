package com.riggle.finza_finza.ui.finza.inventory.old

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.InventryData
import com.riggle.finza_finza.databinding.FragmentOldInventoryBinding
import com.riggle.finza_finza.databinding.HolderOldInventoryBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OldInventoryFragment : BaseFragment<FragmentOldInventoryBinding>() {

    private val viewModel: OldInventoryFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initAdapter()
        initOnClick()

        //  Status for  Forwarded list 0 , Status for Incoming 1 , 2 for in-hand inventory , 3 = old inventory
        viewModel.getInventory(sharedPrefManager.getToken().toString(), "3")

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
        return R.layout.fragment_old_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<InventryData, HolderOldInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_old_inventory, BR.bean
        ) { v, m, pos ->

        }
        binding.rvHomeDrawer.adapter = adapter
    }
}