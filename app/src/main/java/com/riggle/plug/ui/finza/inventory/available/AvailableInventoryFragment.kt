package com.riggle.plug.ui.finza.inventory.available

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.UglyIssuence
import com.riggle.plug.databinding.FragmentAvailableInventoryBinding
import com.riggle.plug.databinding.FragmentBadBinding
import com.riggle.plug.databinding.HolderAvailableInventoryBinding
import com.riggle.plug.databinding.HolderUglyIssuenceBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.inventory.incoming.IncomingInventoryFragmentVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableInventoryFragment : BaseFragment<FragmentAvailableInventoryBinding>() {

    private val viewModel: AvailableInventoryFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initAdapter()
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {

            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_available_inventory
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<UglyIssuence, HolderAvailableInventoryBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_available_inventory, BR.bean
        ) { v, m, pos ->

        }
        binding.rvHomeDrawer.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<UglyIssuence> {
        val list = ArrayList<UglyIssuence>()
        list.add(UglyIssuence("25-08-2024", "0ASD12345678", "Bajaj"))
        list.add(UglyIssuence("25-08-2024", "1ASD12345678", "Bajaj"))
        list.add(UglyIssuence("25-08-2024", "2ASD12345678", "Bajaj"))
        list.add(UglyIssuence("25-08-2024", "3ASD12345678", "Bajaj"))
        return list
    }
}