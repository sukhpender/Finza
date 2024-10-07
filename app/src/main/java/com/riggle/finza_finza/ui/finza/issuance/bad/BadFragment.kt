package com.riggle.finza_finza.ui.finza.issuance.bad

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.finza_finza.BR
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.UglyIssuence
import com.riggle.finza_finza.databinding.FragmentBadBinding
import com.riggle.finza_finza.databinding.HolderUglyIssuenceBinding
import com.riggle.finza_finza.ui.base.BaseFragment
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.base.SimpleRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadFragment : BaseFragment<FragmentBadBinding>() {

    private val viewModel: BadFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initAdapter()
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvMonth -> {
                    selectMonth()
                }
            }
        }
    }

    private fun selectMonth() {


    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_bad
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