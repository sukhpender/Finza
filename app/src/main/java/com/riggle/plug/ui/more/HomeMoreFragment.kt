package com.riggle.plug.ui.more

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.MoreItems
import com.riggle.plug.databinding.CustomLayoutMoreItemsBinding
import com.riggle.plug.databinding.FragmentHomeMoreBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.login.LoginActivity
import com.riggle.plug.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

@AndroidEntryPoint
class HomeMoreFragment : BaseFragment<FragmentHomeMoreBinding>() {

    private val viewModel: HomeMoreFragmentVM by viewModels()

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    private fun initView() {
        binding.tvUserName.text = sharedPrefManager.getCurrentUser()?.user?.full_name
        binding.tvUserShopName.text = sharedPrefManager.getCurrentUser()?.user?.company?.name
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivViewDetailsArrow, R.id.tvUserViewDetails -> {
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMyAccountFragment
                    )
                }

                R.id.clLogout -> {
                    bsLogout()
                }

                R.id.clDeactivateAccount -> { // hided

                }
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home_more
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private var index = 0
    private fun bsLogout() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.CustomBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bs_logout, null)
        val tvLogout = view.findViewById<TextView>(R.id.tvLogout)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val iv = view.findViewById<ImageView>(R.id.iv)
        tvLogout.setOnClickListener {
            sharedPrefManager.clear()
            val intent = LoginActivity.newIntent(requireActivity())
            startActivity(intent)
            activity?.finish()
            dialog.dismiss()
        }
        iv.setOnClickListener {
            if (index < binding.clMain.childCount) {
                binding.clMain.removeViewAt(index)
            }
            dialog.dismiss()
        }
        tvCancel.setOnClickListener {
            if (index < binding.clMain.childCount) {
                binding.clMain.removeViewAt(index)
            }
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
        index = binding.clMain.childCount
        Blurry.with(activity).sampling(1).onto(binding.clMain)
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<MoreItems, CustomLayoutMoreItemsBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_more_items, BR.bean
        ) { v, m, pos ->
            when (pos) {
                0 -> { // business permission

                }

                1 -> { // beat
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToMoreBeatFragment
                    )
                }

                2 -> { // my bills

                }

                3 -> { // faqs

                }

                4 -> { // reports

                }

                5 -> { // riggle care
                    val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+919904499818"))
                    requireActivity().startActivity(intentDial)/*val intentWeb = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfC3XM3bBJ1HzL9OQGlLWuGc3u8Tj585fAkP-XmanTWleudbA/viewform")
                    )
                    startActivity(intentWeb)*/
                }

                6 -> { // training videos

                }
            }
        }
        binding.rvMoreItems.adapter = adapter
        adapter.list = prepareList()
    }

    private fun prepareList(): ArrayList<MoreItems> {
        val dataList = ArrayList<MoreItems>()
        dataList.add(MoreItems("Business Permissions", R.drawable.ic_permissions))
        dataList.add(MoreItems("Beat", R.drawable.ic_location_1))
        dataList.add(MoreItems("My Bills", R.drawable.ic_bills))
        //  dataList.add(MoreItems("Stories", R.drawable.ic_stories1))
        // dataList.add(MoreItems("Arena", R.drawable.ic_arena))
        dataList.add(MoreItems("FAQs", R.drawable.ic_faqs))
        dataList.add(MoreItems("Reports", R.drawable.ic_report))
        dataList.add(MoreItems("Contact Riggle Care", R.drawable.ic_contact_care))
        dataList.add(MoreItems("Training Videos", R.drawable.ic_training_video))
        return dataList
    }
}