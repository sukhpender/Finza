package com.riggle.plug.ui.stories

import android.view.View
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.databinding.FragmentStoriesBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoriesFragment : BaseFragment<FragmentStoriesBinding>() {

    private val viewModel: StoriesFragmentVM by viewModels()

    override fun onCreateView(view: View) {
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_stories
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }
}