package com.riggle.finza_finza.ui.finza.inventory.old

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.InventoryResponseDataX
import com.riggle.finza_finza.data.model.Transfer
import com.riggle.finza_finza.data.model.TransferRequest
import com.riggle.finza_finza.data.model.UsersListData
import com.riggle.finza_finza.databinding.FilterBinding
import com.riggle.finza_finza.databinding.FragmentOldInventoryBinding
import com.riggle.finza_finza.databinding.HolderOldInventoryBinding
import com.riggle.finza_finza.databinding.HolderUsersBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.ui.finza.avtivation.ActivationFragment
import com.riggle.finza_finza.ui.finza.inventory.forwarded.ForwardedInventoryFragment
import com.riggle.finza_finza.utils.BaseCustomBottomSheet
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.VerticalPagination
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showInfoToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class OldInventoryFragment : BaseFragment<FragmentOldInventoryBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: OldInventoryFragmentVM by viewModels()
    private var art = arrayOf(
        "Select here",
        "Select next 10",
        "Select next 20",
        "Select next 30",
        "Select next 40",
        "Select next 50"
    )
    private val selectedItems = ArrayList<Transfer>()
    private var isUserSelected = false
    private var usersList = ArrayList<UsersListData>()
    private var list1: ArrayList<UsersListData>? = null
    private var currentPage: Int = 1
    private lateinit var verticalPagination: VerticalPagination


    override fun onCreateView(view: View) {
        initBs()
        initUserAdapter()

        val adapter1 = ArrayAdapter(
            requireActivity(), android.R.layout.simple_spinner_dropdown_item, art
        )
        binding.spOld.adapter = adapter1

        binding.spOld.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                view?.let {}
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (adapter.list.size != 0) {
                    when (selectedItem) {
                        "Select next 10" -> {
                            if (adapter.list.size < 10) {
                                showInfoToast("Please select manually")
                                setSelection1(0, false)
                            } else {
                                setSelection1(10, true)
                            }
                        }

                        "Select next 20" -> {
                            if (adapter.list.size < 20) {
                                showInfoToast("Please select manually")
                                setSelection1(0, false)
                            } else {
                                setSelection1(20, true)
                            }
                        }

                        "Select next 30" -> {
                            if (adapter.list.size < 30) {
                                showInfoToast("Please select manually")
                                setSelection1(0, false)
                            } else {
                                setSelection1(30, true)
                            }
                        }

                        "Select next 40" -> {
                            if (adapter.list.size < 40) {
                                showInfoToast("Please select manually")
                                setSelection1(0, false)
                            } else {
                                setSelection1(40, true)
                            }
                        }

                        "Select next 50" -> {
                            if (adapter.list.size < 50) {
                                showInfoToast("Please select manually")
                                setSelection1(0, false)
                            } else {
                                setSelection1(50, true)
                            }
                        }

                        else -> {
                            setSelection1(0, false)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        initAdapter()
        initOnClick()

        binding.etvSearchProject.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //  Status for  Forwarded list 0 , Status for Incoming 1 , 2 for in-hand inventory , 3 = old inventory
        viewModel.getInventory1(sharedPrefManager.getToken().toString(), "3", currentPage)
        viewModel.getUsersList(sharedPrefManager.getToken().toString())

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

        viewModel.obrInverntory1.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (filterBs.isShowing) {
                        filterBs.cancel()
                    }
                    if (it.data != null) {
                        if (currentPage < it.data.data.last_page) {
                            verticalPagination.isLoading = false
                        }
                        if (it.data.data.current_page == 1) {
                            adapter.list = it.data.data.data
                        } else {
                            if (it.data.data.data.isNotEmpty()) {
                                adapter.addToList(it.data.data.data)
                            }
                        }
                        if (it.data.data.data.isNotEmpty()) {
                            //  adapter.list = it.data.data.data
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            binding.tvAssign1.visibility = View.VISIBLE
                            binding.llFilterOld.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE
                            binding.tvAssign1.visibility = View.GONE
                            binding.llFilterOld.visibility = View.GONE
                            binding.ivNoData.visibility = View.VISIBLE
                        }
                    }
                }

                Status.WARN -> {
                    showHideLoader(false)
                   // showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                   // showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrFilterInverntory.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (filterBs.isShowing) {
                            filterBs.cancel()
                        }
                        if (currentPage < it.data.data.last_page) {
                            verticalPagination.isLoading = false
                        }
                        if (it.data.data.current_page == 1) {
                            adapter.list = it.data.data.data
                        } else {
                            if (it.data.data.data.isNotEmpty()) {
                                adapter.addToList(it.data.data.data)
                            }
                        }
                        if (it.data.data.data.isNotEmpty()) {
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            binding.tvAssign1.visibility = View.VISIBLE
                            binding.llFilterOld.visibility = View.VISIBLE

                            binding.llFilterOld.visibility = View.VISIBLE
                            binding.tvAssign1.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE

                            binding.llFilterOld.visibility = View.GONE
                            binding.tvAssign1.visibility = View.GONE
                            binding.tvAssign1.visibility = View.GONE
                            binding.llFilterOld.visibility = View.GONE
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


        viewModel.obrAssignInventory1.observe(viewLifecycleOwner) {
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
                        binding.llFilterOld.visibility = View.VISIBLE
                        binding.tvAssign1.visibility = View.VISIBLE

                        viewModel.getInventory1(
                            sharedPrefManager.getToken().toString(), "3", currentPage
                        )
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

    private lateinit var filterBs: BaseCustomBottomSheet<FilterBinding>
    private fun initBs() {
        filterBs = BaseCustomBottomSheet(
            requireContext(), R.layout.filter, ""
        ) {
            when (it.id) {
                R.id.icClose -> {
                    filterBs.cancel()
                }

                R.id.tvApplyFilter -> {
                    val upperLimit = filterBs.binding.etUpperDigits.text.toString()
                    val lowerLimit = filterBs.binding.etLastDigits.text.toString()
                    if (lowerLimit == "") {
                        showErrorToast("Please enter value")
                    } else if (lowerLimit.length != 9) {
                        showErrorToast("Please enter valid limit")
                    } else if (upperLimit == "") {
                        showErrorToast("Please enter value")
                    } else if (upperLimit.length != 9) {
                        showErrorToast("Please enter valid limit")
                    } else { // 1-0622921     // 1-0622864
                        currentPage = 1
                        viewModel.getFilterInventory1(
                            sharedPrefManager.getToken().toString(),
                            "2",
                            lowerLimit,
                            upperLimit,
                            currentPage
                        )
                    }
                }

                R.id.tvClearFilter -> {
                    currentPage = 1
                    viewModel.getInventory1(
                        sharedPrefManager.getToken().toString(), "3", currentPage
                    )
                }
            }
        }
        filterBs.setCancelable(false)
        //filterBs.show()
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<UsersListData> = ArrayList()
        list1.let { it1 ->
            if (it1 != null) {
                for (item in it1) {
                    if (item.first_name.lowercase(Locale.getDefault())
                            .contains(text.lowercase(Locale.getDefault()))
                    ) {
                        filteredList.add(item)
                    }
                }
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

    private lateinit var userAdapter: SimpleRecyclerViewAdapter<UsersListData, HolderUsersBinding>
    private fun initUserAdapter() {
        userAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_users, BR.bean
        ) { v, m, pos ->
            for (i in 0 until usersList.size) {
                usersList[i].isUserSelected = usersList[i].id == m.id
            }
            isUserSelected = true
            userAdapter.list = usersList

            for (i in 0 until selectedItems.size) {
                selectedItems[i].assigned_to = m.id
            }
        }
        binding.rvUser.adapter = userAdapter
    }

    private fun setSelection1(count: Int, select: Boolean) {
        if (select) {
            for (i in 0 until count) {
                adapter.list[i].isSelected = true
                adapter.notifyItemChanged(i)
            }
        } else {
            for (i in 0 until adapter.list.size) {
                adapter.list[i].isSelected = false
                adapter.notifyItemChanged(i)
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvAssign1 -> {
                    selectedItems.clear()
                    if (adapter.list.size != 0) {
                        for (i in 0 until adapter.list.size) {
                            if (adapter.list[i].isSelected) {
                                selectedItems.add(
                                    Transfer(
                                        adapter.list[i].inventory_id, adapter.list[i].assigned_to.id
                                    )
                                )
                            }
                        }
                    }
                    if (selectedItems.size == 0) {
                        showInfoToast("Please select tag")
                    } else {
                        for (i in 0 until usersList.size) {
                            usersList[i].isUserSelected = false
                        }
                        binding.rvHomeDrawer.visibility = View.GONE
                        binding.llFilterOld.visibility = View.GONE
                        binding.tvAssign1.visibility = View.GONE
                        binding.llUserView.visibility = View.VISIBLE
                    }
                }

                R.id.llFilterOld -> {
                    filterBs.show()
                }

                R.id.tvCancel -> {
                    binding.llUserView.visibility = View.GONE
                    binding.rvHomeDrawer.visibility = View.VISIBLE
                    binding.llFilterOld.visibility = View.VISIBLE
                    binding.tvAssign1.visibility = View.VISIBLE
                }

                R.id.tvAssign -> {
                    for (i in 0 until userAdapter.list.size) {
                        if (userAdapter.list[i].isUserSelected) {
                            isUserSelected = true
                        }
                    }
                    if (isUserSelected) {
//                        viewModel.assignInventory(
//                            sharedPrefManager.getToken().toString(),
//                            inventoryId.toString(),
//                            assignToId.toString()
//                        )
                        val reqBody = TransferRequest(transfers = selectedItems)
                        viewModel.assignInventory1(sharedPrefManager.getToken().toString(), reqBody)
                    } else {
                        showInfoToast("Please select user")
                    }
                }
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_old_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<InventoryResponseDataX, HolderOldInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_old_inventory, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.ivIsChecked -> {
                    m.isSelected = !m.isSelected
                    adapter.notifyItemChanged(pos)
                }
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeDrawer.layoutManager = layoutManager
        binding.rvHomeDrawer.setItemViewCacheSize(50)
        binding.rvHomeDrawer.adapter = adapter
        verticalPagination = VerticalPagination(layoutManager, 3)
        verticalPagination.setListener(this)
        binding.rvHomeDrawer.addOnScrollListener(verticalPagination)
        binding.rvHomeDrawer.adapter = adapter
    }

    override fun onLoadMore() {
        currentPage++
        viewModel.getInventory1(sharedPrefManager.getToken().toString(), "3", currentPage)
    }
}