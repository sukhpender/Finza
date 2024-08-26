package com.riggle.plug.ui.finza.language

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.Drawer
import com.riggle.plug.data.model.Language
import com.riggle.plug.databinding.ActivityLanguageBinding
import com.riggle.plug.databinding.HolderDrawerBinding
import com.riggle.plug.databinding.HolderLanguageBinding
import com.riggle.plug.databinding.HolderLanguageHeadingsBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.finza.FinzaHomeActivityVM
import com.riggle.plug.ui.finza.checkVehicleStatus.CheckVehicleStatusActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {

    private val viewModel: LanguageActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, LanguageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_language
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
        initHeadingsAdapter()
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


    private lateinit var headingsdapter: SimpleRecyclerViewAdapter<Language, HolderLanguageHeadingsBinding>
    private fun initHeadingsAdapter() {
        headingsdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_language_headings, BR.bean
        ) { v, m, pos ->
            when(pos){

            }
        }
        binding.rvLanHeadings.adapter = headingsdapter
            headingsdapter.list = prepareHeadingsList()
    }

    private lateinit var languageAdapter: SimpleRecyclerViewAdapter<Language, HolderLanguageBinding>
    private fun initAdapter() {
        languageAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_language, BR.bean
        ) { v, m, pos ->
            when(pos){

            }
        }
        binding.rvLanguage.adapter = languageAdapter
        languageAdapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<Language> {
        val list = ArrayList<Language>()
        list.add(Language("English","English"))
        list.add(Language( "Hindi","हिंदी"))
        return list
    }

    private fun prepareHeadingsList(): ArrayList<Language> {
        val list = ArrayList<Language>()
        list.add(Language("Choose a preferred language to continue.",""))
        list.add(Language( "जारी रखने के लिए पसंदीदा भाषा चुनें",""))
        return list
    }
}