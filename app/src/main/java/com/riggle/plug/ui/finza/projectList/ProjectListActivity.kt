package com.riggle.plug.ui.finza.projectList

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.ProjectListData
import com.riggle.plug.databinding.ActivityProjectListBinding
import com.riggle.plug.databinding.HolderProjectListBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.FinzaHomeActivity
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProjectListActivity : BaseActivity<ActivityProjectListBinding>() {

    private val viewModel: ProjectListActivityVM by viewModels()
    lateinit var list1: ArrayList<ProjectListData>

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ProjectListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_project_list
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.line_color)

        initView()
        initOnClick()
    }

    private fun initView() {
        initAdapter()
        sharedPrefManager.getToken().let {
            viewModel.getList(it.toString())
        }

        binding.etvSearchProject.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.obrProjectList.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        adapter.list = it.data.data
                        list1 = it.data.data as ArrayList<ProjectListData>
                        if (it.data.data.size != 0) {
                            binding.ivNoData.visibility = View.GONE
                            binding.rvHomeDrawer.visibility = View.VISIBLE
                        } else {
                            binding.ivNoData.visibility = View.VISIBLE
                            binding.rvHomeDrawer.visibility = View.GONE
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

    private fun filter(text: String) {
        val filteredList: ArrayList<ProjectListData> = ArrayList()
        for (item in list1) {
            if (item.title.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            binding.ivNoData.visibility = View.VISIBLE
            binding.rvHomeDrawer.visibility = View.GONE
        } else {
            binding.ivNoData.visibility = View.GONE
            binding.rvHomeDrawer.visibility = View.VISIBLE
            adapter.filterList(filteredList)
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }
            }
        }
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<ProjectListData, HolderProjectListBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_project_list, BR.bean
        ) { v, m, pos ->
            startActivity(FinzaHomeActivity.newIntent(this))
            finish()
        }
        binding.rvHomeDrawer.adapter = adapter
    }
}