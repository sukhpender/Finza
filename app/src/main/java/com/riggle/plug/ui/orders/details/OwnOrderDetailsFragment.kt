package com.riggle.plug.ui.orders.details

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.ChallanModel
import com.riggle.plug.data.model.ProductOwnOrderDetails
import com.riggle.plug.databinding.CustomLayoutDetailsType1Binding
import com.riggle.plug.databinding.FragmentOwnOrderDetails2Binding
import com.riggle.plug.databinding.ItemTeamListBinding
import com.riggle.plug.databinding.PopupChallanListBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.base.permission.PermissionHandler
import com.riggle.plug.ui.base.permission.Permissions
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.GetFileInfo
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showInfoToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnOrderDetailsFragment : BaseFragment<FragmentOwnOrderDetails2Binding>() {

    private val viewModel: OwnOrderDetailsFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    companion object {
        var title = ""
        var orderDetailsFor = ""
        var orderId = ""
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerOwnOrderDetails.tvHeaderTitle.text = title
        binding.headerOwnOrderDetails.ivHeaderSearch.visibility = View.GONE

        initItemsAdapter()

        when (orderDetailsFor) {
            "Pending" -> {
                binding.tvCancelOrder.visibility = View.VISIBLE
                getList()
            }

            "Confirmed" -> {
                binding.tvCancelOrder.visibility = View.GONE
            }
        }


        viewModel.obrOrderDetails.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    binding.bean = it.data
                    adapter.list =
                        it.data?.products/*binding.plusMinus.tvQuantSet.setText(it.data!!.products[0].ordered_quantity.toString())*/
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
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.ivMoreOption -> {
                    showChallanPopup(it)
                }
            }
        }
    }


    private lateinit var adapter: SimpleRecyclerViewAdapter<ProductOwnOrderDetails, CustomLayoutDetailsType1Binding>
    private fun initItemsAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_details_type_1, BR.bean
        ) { v, m, pos ->

        }

        binding.rvOwnOrderDetailsType1.adapter = adapter
    }

    private fun getList() {
        val query = HashMap<String, String>()
        query["expand"] = Constants.NETWORK_ORDER_DETAILS_EXPAND
        viewModel.getList(sharedPrefManager.getSessionId()!!, query, orderId.toInt())
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_own_order_details2
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


    private var storge_permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var storge_permissions1 = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storge_permissions_33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    fun permissions(): Array<String> {
        val p: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storge_permissions_33
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            storge_permissions1
        } else {
            storge_permissions
        }
        return p
    }

    private lateinit var addEditWindow: PopupWindow
    private lateinit var dataAdapter: SimpleRecyclerViewAdapter<ChallanModel, ItemTeamListBinding>
    private fun showChallanPopup(view: View) {
        val inflater: LayoutInflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding: PopupChallanListBinding =
            DataBindingUtil.inflate(inflater, R.layout.popup_challan_list, null, false)
        addEditWindow = PopupWindow(
            viewBinding.root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        dataAdapter = SimpleRecyclerViewAdapter<ChallanModel, ItemTeamListBinding>(
            R.layout.item_team_list, BR.bean
        ) { v, m, pos ->
            when (m.name) {
                "Challan" -> {
                    /*if (binding.bean?.challan_file != null) {
                        Permissions.check(
                            requireContext(),
                            permissions(),
                            0,
                            null,
                            object : PermissionHandler() {
                                override fun onGranted() {
                                    showHideLoader(true)
                                    downloadChallan(binding.bean?.challan_file!!)

                                }

                                override fun onDenied(
                                    context: Context?, deniedPermissions: ArrayList<String>?
                                ) {
                                    super.onDenied(context, deniedPermissions)
                                    showInfoToast("Accept Storage permission to continue")
                                }
                            })
                    }*/
                }

                "Upload Invoice" -> {

                }
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvDynamicData.layoutManager = layoutManager
        viewBinding.rvDynamicData.adapter = dataAdapter
        dataAdapter.list = prepareData()
        addEditWindow.showAsDropDown(view, 0, 0, Gravity.NO_GRAVITY)
    }

    private fun downloadChallan(pdfUrl: String) {
        // Create the download request
        var filename: String? = null
        try {
            filename = GetFileInfo().execute(pdfUrl).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Create the download request
        val r = DownloadManager.Request(Uri.parse(pdfUrl))
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        val dm = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            @SuppressLint("Range")
            override fun onReceive(context: Context, intent: Intent) {
                context.unregisterReceiver(this)
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val c: Cursor = dm.query(DownloadManager.Query().setFilterById(downloadId))
                if (c.moveToFirst()) {
                    val status: Int = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        showHideLoader(false)
                        showSuccessToast("Download Completed")
                    } else {
                        showHideLoader(false)
                        showErrorToast("Something went wrong")
                    }

                }
                addEditWindow.dismiss()
                c.close()
            }
        }
        requireContext().registerReceiver(
            onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        dm.enqueue(r)
    }

    private fun prepareData(): ArrayList<ChallanModel> {
        val list = ArrayList<ChallanModel>()
        list.add(ChallanModel(R.drawable.ic_challan, "Challan"))
        list.add(ChallanModel(R.drawable.ic_upload, "Upload Invoice"))
        return list
    }
}