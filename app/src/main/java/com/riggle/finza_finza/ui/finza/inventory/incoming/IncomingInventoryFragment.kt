package com.riggle.finza_finza.ui.finza.inventory.incoming

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.DispatchUsersData
import com.riggle.finza_finza.data.model.ForwardUsersData
import com.riggle.finza_finza.data.model.InventryData
import com.riggle.finza_finza.databinding.FragmentIncomingInventoryBinding
import com.riggle.finza_finza.databinding.HolderIncomgInventoryBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.ui.finza.avtivation.ActivationFragment
import com.riggle.finza_finza.ui.finza.inventory.available.AvailableInventoryFragment
import com.riggle.finza_finza.ui.finza.inventory.forwarded.ForwardedDetailsActivity
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingInventoryFragment : BaseFragment<FragmentIncomingInventoryBinding>() {

    private val viewModel: IncomingInventoryFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initAdapter()
        initOnClick()

        //  Status for  Forwarded list 0 , Status for Incoming 1 , 2 for in-hand inventory , 3 = old inventory
        viewModel.getInventory(sharedPrefManager.getToken().toString(), "4")

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
                 //   showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                  //  showErrorToast(it.message.toString())
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
                    //  showHideLoader(false)
                    viewModel.getInventory(sharedPrefManager.getToken().toString(), "4")
                    ActivationFragment.isUpdatesAvailable.value = true
                  //  AvailableInventoryFragment.isUpdatesAvailable.value = true
                    showSuccessToast(it.data?.message.toString())
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

    private lateinit var adapter: SimpleRecyclerViewAdapter<ForwardUsersData, HolderIncomgInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_incomg_inventory, BR.bean
        ) { v, m, pos ->
            when(v?.id){
                R.id.tvClaimNow -> {
                    DispatchDetailsActivity.userID = m.user_id.toString()
                    startActivity(DispatchDetailsActivity.newIntent(requireActivity()))
                }
            }
//            when (v?.id) {
//                R.id.tvReject -> { // status = 0
//                    showHideLoader(true)
//                    viewModel.acceptReject(
//                        sharedPrefManager.getToken().toString(), m.inventory_id.toString(), "0"
//                    )
//                }
//
//                R.id.tvAccept -> { // status = 1
//                    showHideLoader(true)
//                    viewModel.acceptReject(
//                        sharedPrefManager.getToken().toString(), m.inventory_id.toString(), "1"
//                    )
//                }
//            }
        }
        binding.rvHomeDrawer.adapter = adapter
    }
}