package com.riggle.plug.ui.hr

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.GetLeaveData
import com.riggle.plug.databinding.FragmentHRApprovalsBinding
import com.riggle.plug.databinding.HolderLeaveListBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.hideLoading
import com.riggle.plug.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HRApprovalsFragment : BaseFragment<FragmentHRApprovalsBinding>() {

    private val viewModel: HRApprovalsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var listAdapter: SimpleRecyclerViewAdapter<GetLeaveData, HolderLeaveListBinding>? = null

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_h_r_approvals
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.type = 1

        binding.toolbar.ivHeaderSearch.visibility = View.GONE
        binding.toolbar.tvHeaderTitle.text = "Approvals"

        initRecyclerView()
        binding.type?.let {
            apiCall(it)
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.tvPending -> {
                    binding.type = 1
                    listAdapter?.clearList()
                    binding.type?.let {
                        apiCall(it)
                    }
                }

                R.id.tvApproved -> {
                    binding.type = 2
                    listAdapter?.clearList()
                    binding.type?.let {
                        apiCall(it)
                    }
                }

                R.id.tvRejected -> {
                    binding.type = 3
                    listAdapter?.clearList()
                    binding.type?.let {
                        apiCall(it)
                    }
                }
            }
        }
    }

    private fun initObservers() {

        viewModel.obrLeaveList.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    binding.loader.showLoading()
                }

                Status.SUCCESS -> {
                    binding.loader.hideLoading()
                    if (it.data != null) {
                        it.data.results.let {
                            listAdapter?.list = it
                        }
                    }
                    showErrorMessage("There are no applied leave")
                }

                Status.ERROR -> {
                    binding.loader.hideLoading()
                    showErrorMessage(it.message.toString())
                }

                else -> {
                    binding.loader.hideLoading()
                }
            }
        }

        /*
                viewModel.obrStatusUpdate.observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.LOADING -> {
                            showHideLoader(true)
                        }

                        Status.SUCCESS -> {
                            showHideLoader(false)
                            successToast(it.message.toString())
                            binding.type?.let {
                                apiCall(it)
                            }
                        }

                        Status.ERROR -> {
                            binding.loader.hideLoading()
                            showErrorMessage(it.message.toString())
                        }

                        else -> {
                            binding.loader.hideLoading()
                        }
                    }
                }
        */

    }

    private fun apiCall(type: Int) {
        val request = HashMap<String, String>()
        request["search"] = ""
        request["page_size"] = "100"
        request["page"] = "1"
        request["plug"] = "True"
        request["expand"] = "user,approved_by,rejected_by"
        when (type) {
            1 -> {
                request["pending"] = "true"
            }

            2 -> {
                request["approved"] = "true"
            }

            3 -> {
                request["rejected"] = "true"
            }
        }
        //  request["no-pagination"] = "true"
        viewModel.getLeaveList(sharedPrefManager.getSessionId()!!, request)
    }

    private fun showErrorMessage(message: String) {
        binding.emptyView.tvMessage.text = message
        if (listAdapter?.list?.isNotEmpty() == true) {
            binding.emptyView.main.visibility = View.GONE
        } else {
            binding.emptyView.main.visibility = View.VISIBLE
        }
    }

    private fun initRecyclerView() {
        listAdapter = SimpleRecyclerViewAdapter<GetLeaveData, HolderLeaveListBinding>(
            R.layout.holder_leave_list, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.tvReject -> {/*showUpdateStatus(
                                    "Are you sure you want to reject leave request?",
                                    2,
                                    m?.id
                                )*/
                }

                R.id.tvApprove -> {/*showUpdateStatus(
                                    "Are you sure you want to approve leave request?",
                                    1,
                                    m?.id
                                )*/
                }
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvData.layoutManager = layoutManager
        binding.rvData.adapter = listAdapter
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireContext(), R.color.dark_grey_txt_color15
                )
            )
        )
        binding.rvData.addItemDecoration(dividerItemDecoration)
    }

    /*
        private fun showUpdateStatus(title: String, type: Int, id: Int?) {
            val dialog =
                BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
            val viewBinding = BsUpdateLeaveStatusBinding.inflate(layoutInflater)
            viewBinding.tvTitleTwo.text = title
            if (type == 1) {
                viewBinding.tvTitleTwo.setTextColor(ContextCompat.getColor(this, R.color.orange))
            } else {
                viewBinding.tvTitleTwo.setTextColor(ContextCompat.getColor(this, R.color.rejected))
            }
            viewBinding.ivCross.setOnClickListener {
                dialog.dismiss()
            }
            viewBinding.btnConfirmOne.setOnClickListener {
                if (!TextUtils.isEmpty(viewBinding.tvRemarks.text?.trim().toString())) {
                    if (type == 1) {
                        id?.let {
                            viewModel.updateLeaveStatus(
                                getAuthorization(), it,
                                LeaveStatusRequest(
                                    approved_by = SharedPrefManager.getCurrentUser()?.id,
                                    manager_remarks = viewBinding.tvRemarks.text?.trim().toString(),
                                    status = "approved"
                                )
                            )
                        }
                    } else {
                        id?.let {
                            viewModel.updateLeaveStatus(
                                getAuthorization(), it, LeaveStatusRequest(
                                    rejected_by = SharedPrefManager.getCurrentUser()?.id,
                                    manager_remarks = viewBinding.tvRemarks.text?.trim().toString(),
                                    status = "rejected"
                                )
                            )
                        }
                    }
                    dialog.dismiss()
                } else {
                    viewBinding.tvRemarks.error = "Enter Beat Name"
                }
            }

            dialog.setCancelable(false)
            dialog.setContentView(viewBinding.root)
            dialog.show()
        }
    */

}