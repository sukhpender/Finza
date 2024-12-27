package com.riggle.finza_finza.ui.finza.avtivation.frag

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
import com.riggle.finza_finza.data.model.ActivationsResponseData1
import com.riggle.finza_finza.databinding.FragmentActivationDetailBinding
import com.riggle.finza_finza.databinding.HolderActivationDetailsBinding
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
class ActivationDetailFragment : BaseFragment<FragmentActivationDetailBinding>(),
    VerticalPagination.VerticalScrollListener {

    private val viewModel: ActivationDetailFragmentVM by viewModels()
    private var currentPage: Int = 1
    private lateinit var verticalPagination: VerticalPagination

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(view: View) {
        binding.tvMonth.text = getCurrentMonthAndYear()
        initView()
        initOnClick()
        initObservers()
    }

    private fun initView() {
        initAdapter()
        val calendar = Calendar.getInstance()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val year = calendar.get(Calendar.YEAR).toString()

        viewModel.getActivations(sharedPrefManager.getToken().toString(), "$year-$month")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.llCalender -> {
                    val picker = RackMonthPicker(requireContext()).setLocale(Locale.ENGLISH)
                        .setPositiveButton { month, startDate, endDate, year, monthLabel ->
                            binding.tvMonth.text = getShortMonthName(month) + " " + year.toString()
                            viewModel.getActivations(
                                sharedPrefManager.getToken().toString(),
                                "$year-$month"
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

    private fun initObservers() {
        viewModel.obrActivations.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
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
                            binding.ivNoData.visibility = View.GONE
                        } else {
                            binding.rvHomeDrawer.visibility = View.GONE
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
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<ActivationsResponseData1, HolderActivationDetailsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_activation_details, BR.bean
        ) { v, m, pos ->
            when (v?.id) {

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

    override fun getLayoutResource(): Int {
        return R.layout.fragment_activation_detail
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onLoadMore() {
        currentPage++
        viewModel.getActivations(sharedPrefManager.getToken().toString(), "2024-07")
    }

}