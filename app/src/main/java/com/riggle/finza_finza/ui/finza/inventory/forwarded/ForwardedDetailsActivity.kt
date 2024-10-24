package com.riggle.finza_finza.ui.finza.inventory.forwarded

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.CancelRequest
import com.riggle.finza_finza.data.model.Cancelled1
import com.riggle.finza_finza.data.model.InventoryResponseDataX
import com.riggle.finza_finza.databinding.ActivityForwardedDetailsBinding
import com.riggle.finza_finza.databinding.FilterBinding
import com.riggle.finza_finza.databinding.HolderAvailableInventoryBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.utils.BaseCustomBottomSheet
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.VerticalPagination
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showInfoToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForwardedDetailsActivity : BaseActivity<ActivityForwardedDetailsBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: ForwardedDetailsActivityVM by viewModels()
    private lateinit var verticalPagination: VerticalPagination
    private var art = arrayOf(
        "Select here",
        "Select next 10",
        "Select next 20",
        "Select next 30",
        "Select next 40",
        "Select next 50"
    )
    private var currentPage: Int = 1
    private val selectedItems = ArrayList<Cancelled1>()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ForwardedDetailsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

        var userID = ""
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_forwarded_details
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initBs()

        val adapter1 = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, art
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
        initView()
        initOnClick()
    }

    private fun initView() {
        initAdapter()
        viewModel.getInventory1(
            sharedPrefManager.getToken().toString(),
            "0",
            currentPage,
            userID.toInt()
        )

        viewModel.obrInverntory1.observe(this) {
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
                            binding.tvCancel123.visibility = View.VISIBLE
                            binding.llFilterInHand.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE
                            binding.tvCancel123.visibility = View.GONE
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

        viewModel.obrFilterInverntory.observe(this) {
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
                        if (it.data.data.data.size != 0) {
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                            binding.tvCancel123.visibility = View.VISIBLE
                            binding.llFilterInHand.visibility = View.VISIBLE
                            binding.llFilterInHand.visibility = View.VISIBLE
                            binding.tvCancel123.visibility = View.VISIBLE
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE
                            binding.llFilterInHand.visibility = View.GONE
                            binding.tvCancel123.visibility = View.GONE
                            binding.tvCancel123.visibility = View.GONE
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

        viewModel.obrCancel.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        showSuccessToast(it.data.message.toString())
                        currentPage = 1
                        viewModel.getInventory1(
                            sharedPrefManager.getToken().toString(),
                            "0",
                            currentPage,
                            userID.toInt()
                        )
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
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }

                R.id.llFilterInHand -> {
                    filterBs.show()
                }

                R.id.tvCancel123 -> {
                    selectedItems.clear()
                    if (adapter.list.size != 0) {
                        for (i in 0 until adapter.list.size) {
                            if (adapter.list[i].isSelected) {
                                selectedItems.add(
                                    Cancelled1(
                                        adapter.list[i].inventory_id, userID.toInt()
                                    )
                                )
                            }
                        }
                    }
                    if (selectedItems.size == 0) {
                        showInfoToast("Please select tag")
                    } else {
                        val req = CancelRequest(cancelled = selectedItems)
                        viewModel.cancellInven(sharedPrefManager.getToken().toString(), req)
                    }
                }
            }
        }
    }

    private lateinit var filterBs: BaseCustomBottomSheet<FilterBinding>
    private fun initBs() {
        filterBs = BaseCustomBottomSheet(
            this, R.layout.filter, ""
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
                            "0",
                            lowerLimit,
                            upperLimit,
                            currentPage
                        )
                    }
                }

                R.id.tvClearFilter -> {
                    currentPage = 1
                    viewModel.getInventory1(
                        sharedPrefManager.getToken().toString(), "0", currentPage, userID.toInt()
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
        val layoutManager = LinearLayoutManager(this)
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
        viewModel.getInventory1(
            sharedPrefManager.getToken().toString(),
            "0",
            currentPage,
            userID.toInt()
        )
    }
}