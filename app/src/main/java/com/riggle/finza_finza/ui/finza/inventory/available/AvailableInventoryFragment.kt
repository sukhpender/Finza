package com.riggle.finza_finza.ui.finza.inventory.available

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
import com.riggle.finza_finza.databinding.FragmentAvailableInventoryBinding
import com.riggle.finza_finza.databinding.HolderAvailableInventoryBinding
import com.riggle.finza_finza.databinding.HolderUsersBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.ui.finza.avtivation.ActivationFragment
import com.riggle.finza_finza.ui.finza.inventory.forwarded.ForwardedInventoryFragment
import com.riggle.finza_finza.utils.BaseCustomBottomSheet
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.VerticalPagination
import com.riggle.finza_finza.utils.event.SingleLiveEvent
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showInfoToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AvailableInventoryFragment : BaseFragment<FragmentAvailableInventoryBinding>(),
    VerticalPagination.VerticalScrollListener {
// 1-0626392
        // 1-06258
    private val viewModel: AvailableInventoryFragmentVM by viewModels()
    private var isUserSelected = false
    private var usersList = ArrayList<UsersListData>()
    private var inventoryId = 0
    private var assignToId = 0
    private var list1: ArrayList<UsersListData>? = null
    private var art = arrayOf(
        "Select here",
        "Select next 10",
        "Select next 20",
        "Select next 30",
        "Select next 40",
        "Select next 50"
    )
    private var nextCount: String? = ""
    private val selectedItems = ArrayList<Transfer>()
    private var currentPage: Int = 1
    private lateinit var verticalPagination: VerticalPagination

    companion object {
        var isUpdatesAvailable = SingleLiveEvent<Boolean>()
    }

    override fun onCreateView(view: View) {
        initBs()
        val adapter1 = ArrayAdapter(
            requireActivity(), android.R.layout.simple_spinner_dropdown_item, art
        )
        binding.spInHand.adapter = adapter1

        binding.spInHand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        initUserAdapter()
        initOnClick()

        isUpdatesAvailable.observe(viewLifecycleOwner) { it ->
            if (it) {
                viewModel.getInventory1(sharedPrefManager.getToken().toString(), "2", currentPage)
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
        viewModel.getInventory1(sharedPrefManager.getToken().toString(), "2", currentPage)

        viewModel.getUsersList(sharedPrefManager.getToken().toString())

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
                        if (it.data.data.data.size != 0) {
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            binding.tvAssign123.visibility = View.VISIBLE
                            binding.llFilterInHand.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE
                            binding.tvAssign123.visibility = View.GONE
                            binding.llFilterInHand.visibility = View.GONE
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
                        filterApplied = "true"
                        adapter.list = it.data.data.data
//                        if (currentPage < it.data.data.last_page) {
//                            verticalPagination.isLoading = false
//                        }
//                        if (it.data.data.current_page == 1) {
//                            adapter.list = it.data.data.data
//                        } else {
//                            if (it.data.data.data.isNotEmpty()) {
//                                adapter.addToList(it.data.data.data)
//                            }
//                        }
                        if (it.data.data.data.size != 0) {
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            binding.tvAssign123.visibility = View.VISIBLE
                            binding.llFilterInHand.visibility = View.VISIBLE
                            binding.llFilterInHand.visibility = View.VISIBLE
                            binding.tvAssign123.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE
                            binding.llFilterInHand.visibility = View.GONE
                            binding.tvAssign123.visibility = View.GONE
                            binding.tvAssign123.visibility = View.GONE
                            binding.llFilterInHand.visibility = View.GONE
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

        viewModel.obrAssignInventory1.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    // showSuccessToast(it.data.message.toString())
                    binding.llUserView.visibility = View.GONE
                    binding.rvHomeDrawer.visibility = View.VISIBLE
                    binding.llFilterInHand.visibility = View.VISIBLE
                    binding.tvAssign123.visibility = View.VISIBLE
                    inventoryId = 0
                    assignToId = 0
                    currentPage = 1
                    viewModel.getInventory1(
                        sharedPrefManager.getToken().toString(), "2", currentPage
                    )
                    ForwardedInventoryFragment.isUpdatesAvailable.value = true
                    ActivationFragment.isUpdatesAvailable.value = true

                }

                Status.WARN -> {
                    showHideLoader(false)
                    // showErrorToast(it.message.toString())
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    //  showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    override fun onPause() {
        super.onPause()
        currentPage = 1
    }

    var filterApplied = "false"
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
                    filterApplied = "false"
                    currentPage = 1
                    viewModel.getInventory1(
                        sharedPrefManager.getToken().toString(), "2", currentPage
                    )
                }
            }
        }
        filterBs.setCancelable(false)
        //filterBs.show()
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

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.llFilterInHand -> {
                    filterBs.show()
                }

                R.id.tvAssign123 -> {
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
                        for (i in 0..usersList.size - 1) {
                            usersList[i].isUserSelected = false
                        }
                        binding.llUserView.visibility = View.VISIBLE
                        binding.rvHomeDrawer.visibility = View.GONE
                        binding.llFilterInHand.visibility = View.GONE
                        binding.tvAssign123.visibility = View.GONE
                    }
                }

                R.id.tvCancel -> {
                    binding.llUserView.visibility = View.GONE
                    binding.rvHomeDrawer.visibility = View.VISIBLE
                    binding.llFilterInHand.visibility = View.VISIBLE
                    binding.tvAssign123.visibility = View.VISIBLE
                }

                R.id.tvAssign -> {
                    for (i in 0..userAdapter.list.size - 1) {
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
                        selectedItems.clear()
                        for (i in 0 until adapter.list.size) {
                            if (adapter.list[i].isSelected) {
                                selectedItems.add(
                                    Transfer(
                                        adapter.list[i].inventory_id, assignToId
                                    )
                                )
                            }
                        }
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
        return R.layout.fragment_available_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<InventoryResponseDataX, HolderAvailableInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_available_inventory, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.ivIsChecked -> {
                    m.isSelected = !m.isSelected
                    adapter.notifyItemChanged(pos)
                }
//                R.id.tvAssign1 -> {
//                    inventoryId = m.inventory_id
//                    for (i in 0..usersList.size - 1) {
//                        usersList[i].isUserSelected = false
//                    }
//                   // isUserSelected = false
//                    binding.llUserView.visibility = View.VISIBLE
//                    binding.rvHomeDrawer.visibility = View.GONE
//                }
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

    override fun onLoadMore() {
        if (filterApplied == "false") {
            currentPage++
            viewModel.getInventory1(sharedPrefManager.getToken().toString(), "2", currentPage)
        }
    }
}