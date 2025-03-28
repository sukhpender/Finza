package com.riggle.finza_finza.ui.finza.issuance.urt

import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kal.rackmonthpicker.RackMonthPicker
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener
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
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class UrtFragment : BaseFragment<FragmentUrtBinding>(), VerticalPagination.VerticalScrollListener {

    private val viewModel: UrtFragmentVM by viewModels()
    private var currentPage: Int = 1
    private var month = ""
    private var year = ""
    private lateinit var verticalPagination: VerticalPagination

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(view: View) {
        binding.tvMonth.text = getCurrentMonthAndYear()

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentMonthAndYear(): String {
        val currentDate = LocalDate.now() // Get the current date
        val formatter = DateTimeFormatter.ofPattern("MMM yyyy") // Define the pattern for formatting
        return currentDate.format(formatter) // Format the date to "MMM yyyy"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getShortMonthName(month: Int): String {
        return try {
            Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        } catch (e: Exception) {
            "Invalid month"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvMonth -> {
                    val picker = RackMonthPicker(requireContext()).setLocale(Locale.ENGLISH)
                        .setPositiveButton { month, startDate, endDate, year, monthLabel ->
                            binding.tvMonth.text = getShortMonthName(month) + " " + year.toString()
                            viewModel.getList(
                                sharedPrefManager.getToken().toString(),
                                month.toString(),
                                year.toString()
                            )
                        }.setNegativeButton(object : DialogInterface.OnClickListener,
                            OnCancelMonthDialogListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                // Dismiss the dialog using DialogInterface when the negative button is clicked
                                dialog.dismiss()
                            }

                            override fun onCancel(dialog: AlertDialog?) {
                                dialog?.dismiss()
                            }
                        })
                    picker.setColorTheme(
                        ContextCompat.getColor(
                            requireContext(), R.color.app_color
                        )
                    )
                    picker.show()

                }
            }
        }
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