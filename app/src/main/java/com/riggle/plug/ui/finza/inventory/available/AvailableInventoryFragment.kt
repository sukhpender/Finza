package com.riggle.plug.ui.finza.inventory.available

import android.text.Editable
import android.text.TextWatcher
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
import com.riggle.plug.ui.finza.avtivation.ActivationFragment
import com.riggle.plug.ui.finza.avtivation.ActivationFragment.Companion.isUpdatesAvailable
import com.riggle.plug.ui.finza.inventory.forwarded.ForwardedInventoryFragment
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.event.SingleLiveEvent
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showInfoToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AvailableInventoryFragment : BaseFragment<FragmentAvailableInventoryBinding>() {

    private val viewModel: AvailableInventoryFragmentVM by viewModels()
    private var isUserSelected = false
    private var usersList = ArrayList<UsersListData>()
    private var inventoryId = 0
    private var assignToId = 0
    lateinit var list1: ArrayList<UsersListData>

    companion object{
        var isUpdatesAvailable = SingleLiveEvent<Boolean>()
    }

    override fun onCreateView(view: View) {
        initAdapter()
        initUserAdapter()
        initOnClick()

        isUpdatesAvailable.observe(viewLifecycleOwner){ it ->
            if (it){
                viewModel.getInventory(sharedPrefManager.getToken().toString(), "2")
                viewModel.getUsersList(sharedPrefManager.getToken().toString())
            }
        }

        binding.etvSearchProject.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

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
                        list1 = it.data.data
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
                        ForwardedInventoryFragment.isUpdatesAvailable.value = true
                        ActivationFragment.isUpdatesAvailable.value = true
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

    private fun filter(text: String) {
        val filteredList: ArrayList<UsersListData> = ArrayList()
        for (item in list1) {
            if (item.first_name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            binding.rvUser.visibility = View.GONE
            binding.tvAssign.visibility = View.GONE
            binding.tvCancel.visibility = View.GONE
        } else {
            binding.rvUser.visibility = View.VISIBLE
            binding.tvAssign.visibility = View.VISIBLE
            binding.tvCancel.visibility = View.VISIBLE
            userAdapter.filterList(filteredList)
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
                    for (i in 0..userAdapter.list.size - 1) {
                        if (userAdapter.list[i].isUserSelected) {
                            isUserSelected = true
                        }
                    }
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
                   // isUserSelected = false
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