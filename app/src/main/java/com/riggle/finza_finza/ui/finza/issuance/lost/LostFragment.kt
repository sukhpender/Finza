package com.riggle.finza_finza.ui.finza.issuance.lost

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.UglyIssuence
import com.riggle.finza_finza.databinding.FragmentLostBinding
import com.riggle.finza_finza.databinding.HolderUglyIssuenceBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LostFragment : BaseFragment<FragmentLostBinding>() {

    private val viewModel: LostFragmentVM by viewModels()

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
        return R.layout.fragment_lost
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<UglyIssuence, HolderUglyIssuenceBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_ugly_issuence, BR.bean
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
        list.add(UglyIssuence("25-08-2024", "4ASD12345678", "Bajaj"))
        list.add(UglyIssuence("25-08-2024", "5ASD12345678", "Bajaj"))
        list.add(UglyIssuence("25-08-2024", "6ASD12345678", "Bajaj"))
        list.add(UglyIssuence("25-08-2024", "7ASD12345678", "Bajaj"))
        return list
    }
}