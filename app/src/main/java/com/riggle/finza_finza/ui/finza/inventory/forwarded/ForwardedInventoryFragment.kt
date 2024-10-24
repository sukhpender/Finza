package com.riggle.finza_finza.ui.finza.inventory.forwarded

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.DispatchUsersData
import com.riggle.finza_finza.data.model.ForwardUsersData
import com.riggle.finza_finza.data.model.InventryData
import com.riggle.finza_finza.databinding.FragmentForwardedInventoryBinding
import com.riggle.finza_finza.databinding.HolderForwardedInventoryBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.event.SingleLiveEvent
import com.riggle.finza_finza.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForwardedInventoryFragment : BaseFragment<FragmentForwardedInventoryBinding>() {

    private val viewModel: ForwardedInventoryFragmentVM by viewModels()
    private var nextCount : String?  = ""
   // var art = arrayOf("Select next 10","Select next 20","Select next 30","Select next 40","Select next 50")

    companion object{
        var isUpdatesAvailable = SingleLiveEvent<Boolean>()
    }

    override fun onCreateView(view: View) {
//    val adapter1 = ArrayAdapter(
//        requireActivity(),
//        android.R.layout.simple_spinner_dropdown_item,art
//    )
//    binding.spForward.adapter = adapter1
//
//        binding.spForward.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                view?.let {}
//                nextCount = parent.getItemAtPosition(position).toString()
//            }
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }

        initAdapter()
        initOnClick()

        isUpdatesAvailable.observe(viewLifecycleOwner){ it ->
            if (it){
                viewModel.getInventory(sharedPrefManager.getToken().toString(), "5")
            }
        }

        // Status for  Forwarded list 0 , Status for Incoming 1 , 2 for in-hand inventory , 3 = old inventory
        viewModel.getInventory(sharedPrefManager.getToken().toString(), "5")

        viewModel.obrInverntory.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        if (it.data.data.isNotEmpty()) {
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
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_forwarded_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DispatchUsersData, HolderForwardedInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_forwarded_inventory, BR.bean
        ) { v, m, pos ->
            when(v.id){
                R.id.tvClaimNow -> {
                    ForwardedDetailsActivity.userID = m.user_id.toString()
                    startActivity(ForwardedDetailsActivity.newIntent(requireActivity()))
                }
            }
        }
        binding.rvHomeDrawer.adapter = adapter
    }
}