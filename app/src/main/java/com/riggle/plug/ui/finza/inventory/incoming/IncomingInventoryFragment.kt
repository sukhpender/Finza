package com.riggle.plug.ui.finza.inventory.incoming

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.InventryData
import com.riggle.plug.databinding.FragmentIncomingInventoryBinding
import com.riggle.plug.databinding.HolderIncomgInventoryBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingInventoryFragment : BaseFragment<FragmentIncomingInventoryBinding>() {

    private val viewModel: IncomingInventoryFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initAdapter()
        initOnClick()

        //  Status for  Forwarded list 0 , Status for Incoming 1 , 2 for in-hand inventory , 3 = old inventory
        viewModel.getInventory(sharedPrefManager.getToken().toString(), "1")

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
                            binding.ivNoData.visibility = View.GONE
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                        } else {
                            binding.ivNoData.visibility = View.VISIBLE
                            binding.rvHomeDrawer.visibility = View.GONE
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

        viewModel.obrAcceptReject.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
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
        return R.layout.fragment_incoming_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<InventryData, HolderIncomgInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_incomg_inventory, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvReject -> {// status = 1
                    viewModel.acceptReject(
                        sharedPrefManager.getToken().toString(), m.inventory_id.toString(), "1"
                    )
                }

                R.id.tvAccept -> { // status = 0
                    viewModel.acceptReject(
                        sharedPrefManager.getToken().toString(), m.inventory_id.toString(), "0"
                    )
                }
            }
        }
        binding.rvHomeDrawer.adapter = adapter
    }
}