package com.riggle.finza_finza.ui.finza.issuance.urt

import android.app.DatePickerDialog
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.DataXX
import com.riggle.finza_finza.databinding.FragmentUrtBinding
import com.riggle.finza_finza.databinding.HolderUglyIssuenceBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.VerticalPagination
import com.riggle.finza_finza.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class UrtFragment : BaseFragment<FragmentUrtBinding>(), VerticalPagination.VerticalScrollListener {

    private val viewModel: UrtFragmentVM by viewModels()
    private var currentPage: Int = 1
    private var month = ""
    private var year = ""
    private lateinit var verticalPagination: VerticalPagination

    override fun onCreateView(view: View) {
        val calendar = Calendar.getInstance()
        month = (calendar.get(Calendar.MONTH) + 1).toString()
        year = calendar.get(Calendar.YEAR).toString()

        viewModel.getList(
            sharedPrefManager.getToken().toString(), month, year
        )

        viewModel.obrListing.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (currentPage < it.data.data.data.last_page) {
                            verticalPagination.isLoading = false
                        }
                        if (it.data.data.data.current_page == 1) {
                            adapter.list = it.data.data.data.data
                        } else {
                            if (it.data.data.data.data.isNotEmpty()) {
                                adapter.addToList(it.data.data.data.data)
                            }
                        }
                        binding.tvUrtAmount.text = "₹ " + it.data.data.total_amount.toString()
                        binding.tvUrtCount.text = it.data.data.total_count.toString()
                        adapter.list = it.data.data.data.data
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

        initAdapter()
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvMonth -> {
                    showMonthYearPicker()
                }
            }
        }
    }

    private fun showMonthYearPicker() {
        val calendar = Calendar.getInstance()
        val year1 = calendar.get(Calendar.YEAR)
        val month1 = calendar.get(Calendar.MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, _ ->
                month = (selectedMonth + 1).toString()
                year = selectedYear.toString()
                viewModel.getList(
                    sharedPrefManager.getToken().toString(), month, year
                )
            }, year1, month1, 1)

        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.datePicker.init(year1, month1, 1, null)
        datePickerDialog.show()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_urt
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DataXX, HolderUglyIssuenceBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_ugly_issuence, BR.bean
        ) { v, m, pos ->

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
        viewModel.getList(sharedPrefManager.getToken().toString(), month, year)
    }
}