package com.riggle.plug.ui.finza.inventory.available

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.InventryData
import com.riggle.plug.data.model.UsersListData
import com.riggle.plug.databinding.FragmentAvailableInventoryBinding
import com.riggle.plug.databinding.HolderAvailableInventoryBinding
import com.riggle.plug.databinding.HolderUsersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showInfoToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableInventoryFragment : BaseFragment<FragmentAvailableInventoryBinding>() {

    private val viewModel: AvailableInventoryFragmentVM by viewModels()
    private var isUserSelected = false
    private var usersList = ArrayList<UsersListData>()
    private var inventoryId = 0
    private var assignToId = 0

    override fun onCreateView(view: View) {
        initAdapter()
        initUserAdapter()
        initOnClick()

        //  Status for  Forwarded list 0 , Status for Incoming 1 , 2 for in-hand inventory , 3 = old inventory
        viewModel.getInventory(sharedPrefManager.getToken().toString(), "2")
        viewModel.getUsersList(sharedPrefManager.getToken().toString())

        viewModel.obrInverntory.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.data.size != 0) {
                            adapter.list = it.data.data
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        }else{
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

        viewModel.obrUsers.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        userAdapter.list = it.data.data
                        usersList = it.data.data as ArrayList<UsersListData>
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

        viewModel.obrAssignInventory.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        showSuccessToast(it.data.message.toString())
                        binding.llUserView.visibility = View.GONE
                        binding.rvHomeDrawer.visibility = View.VISIBLE
                        inventoryId = 0
                        assignToId = 0
                        viewModel.getInventory(sharedPrefManager.getToken().toString(), "2")
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
                R.id.tvCancel -> {
                    binding.llUserView.visibility = View.GONE
                    binding.rvHomeDrawer.visibility = View.VISIBLE
                }

                R.id.tvAssign -> {
                    if (isUserSelected) {
                        viewModel.assignInventory(
                            sharedPrefManager.getToken().toString(),
                            inventoryId.toString(),
                            assignToId.toString()
                        )
                    } else {
                        showInfoToast("Please select user")
                    }
                }
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_available_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<InventryData, HolderAvailableInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_available_inventory, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvAssign1 -> {
                    inventoryId = m.inventory_id
                    for (i in 0..usersList.size - 1) {
                        usersList[i].isUserSelected = false
                    }
                    isUserSelected = false
                    binding.llUserView.visibility = View.VISIBLE
                    binding.rvHomeDrawer.visibility = View.GONE
                }
            }
        }
        binding.rvHomeDrawer.adapter = adapter
    }

    private lateinit var userAdapter: SimpleRecyclerViewAdapter<UsersListData, HolderUsersBinding>
    private fun initUserAdapter() {
        userAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_users, BR.bean
        ) { v, m, pos ->
            for (i in 0..usersList.size - 1) {
                usersList[i].isUserSelected = usersList[i].id == m.id
            }
            isUserSelected = true
            userAdapter.list = usersList
            assignToId = m.id

        }
        binding.rvUser.adapter = userAdapter
    }
}