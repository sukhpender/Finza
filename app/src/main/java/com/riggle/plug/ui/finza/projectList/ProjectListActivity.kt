package com.riggle.plug.ui.finza.projectList

import android.app.Activity
import android.content.Intent
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.Drawer
import com.riggle.plug.databinding.ActivityProjectListBinding
import com.riggle.plug.databinding.HolderProjectListBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.FinzaHomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectListActivity : BaseActivity<ActivityProjectListBinding>() {
    private val viewModel: ProjectListActivityVM by viewModels()

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

    private lateinit var adapter: SimpleRecyclerViewAdapter<Drawer, HolderProjectListBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_project_list, BR.bean
        ) { v, m, pos ->
            startActivity(FinzaHomeActivity.newIntent(this))
            finish()
        }
        binding.rvHomeDrawer.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<Drawer> {
        val list = ArrayList<Drawer>()
        list.add(Drawer(R.drawable.wallet, "10765"))
        list.add(Drawer(R.drawable.wallet, "05247"))
        list.add(Drawer(R.drawable.wallet, "15845"))
        list.add(Drawer(R.drawable.wallet, "14245"))
        list.add(Drawer(R.drawable.wallet, "10765"))
        list.add(Drawer(R.drawable.wallet, "05247"))
        list.add(Drawer(R.drawable.wallet, "15845"))
        list.add(Drawer(R.drawable.wallet, "14245"))
        list.add(Drawer(R.drawable.wallet, "10765"))
        list.add(Drawer(R.drawable.wallet, "05247"))
        list.add(Drawer(R.drawable.wallet, "15845"))
        list.add(Drawer(R.drawable.wallet, "14245"))
        return list
    }
}