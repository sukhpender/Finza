package com.riggle.plug.ui.more.myAccount

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.CoOwnersListResponseModel
import com.riggle.plug.databinding.CustomLayoutCoOwnersBinding
import com.riggle.plug.databinding.FragmentMyAccountBinding
import com.riggle.plug.databinding.PopupDeleteBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ImageUtils
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment : BaseFragment<FragmentMyAccountBinding>() {

    private val viewModel: MyAccountFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_my_account
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity
        binding.headerMyAccount.tvHeaderTitle.text = getString(R.string.my_account)
        binding.headerMyAccount.ivHeaderSearch.visibility = View.GONE

        initAdapter()
        getProfileDetails()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivHeaderBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.ivOwnerInfo -> {
                    if (binding.clOwnerInfo.visibility == View.VISIBLE) {
                        binding.clOwnerInfo.visibility = View.GONE
                        binding.ivOwnerInfo.rotation = 0f
                    } else {
                        binding.clOwnerInfo.visibility = View.VISIBLE
                        binding.ivOwnerInfo.rotation = 180f
                    }
                }

                R.id.ivEditOwnInfo -> {
                    EditOwnInfoFragment.userDetails = binding.bean
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToEditAccountFragment
                    )
                }

                R.id.ivEditBusinessInfo -> {
                    EditBusinessFragment.companyModel = binding.bean?.company
                    ImageUtils.navigateWithSlideAnimations(
                        findNavController(), R.id.navigateToEditBusinessFragment
                    )
                }

                R.id.ivBusinessInfo -> {
                    if (binding.clBusinessInfo.visibility == View.VISIBLE) {
                        binding.clBusinessInfo.visibility = View.GONE
                        binding.ivBusinessInfo.rotation = 0f
                    } else {
                        binding.clBusinessInfo.visibility = View.VISIBLE
                        binding.ivBusinessInfo.rotation = 180f
                    }
                }

                R.id.tvUploadQRCode -> {

                }
            }
        }
    }

    private fun getCoOwners() {
        val query = HashMap<String, String>()
        query["role"] = "company_admin"
        query["no-pagination"] = "true"
        query["fields"] = "id,designation,full_name,first_name,last_name,mobile,update_url"
        viewModel.getCoOwners(
            sharedPrefManager.getSessionId()!!, query
        )
    }

    private fun getProfileDetails() {
        val query = HashMap<String, String>()
        query["expand"] = "company.users,company.extra"
        query["fields"] =
            "update_url,company.update_url,image,company.logo,full_name,first_name,last_name,email,mobile,company.name,company.city,company.state,company.pincode,company.full_address,company.locality,company.extra.proof_file,company.extra.pan_card,company.extra.gst_number,company.extra.pan_number,company.extra.fssai_expiry_date,company.extra.fssai"
        viewModel.getCitiesList(
            sharedPrefManager.getSessionId()!!, sharedPrefManager.getCurrentUser()!!.user.id, query
        )
    }

    private fun deleteCoOwner(id: Int) {
        viewModel.deleteCoOwner(sharedPrefManager.getSessionId()!!, id)
    }

    private fun initObservers() {
        viewModel.obrProfile.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    //showHideLoader(false)
                    if (it.data != null) {
                        binding.bean = it.data
                        getCoOwners()
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrCoOwner.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    val list = ArrayList<CoOwnersListResponseModel>()
                    if (it.data != null) {
                        for (i in 0 until it.data.size) {
                            if (it.data[i].designation != null) {
                                list.add(it.data[i])
                            }
                        }
                        adapter.list = list
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.obrDeleteCoOwner.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    showSuccessToast("Deleted successfully")
                    if (positionDeleted != null) {
                        adapter.removeItem(positionDeleted!!)
                    }
                    addEditWindow.dismiss()
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<CoOwnersListResponseModel, CustomLayoutCoOwnersBinding>
    private var positionDeleted: Int? = null
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.custom_layout_co_owners, BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.ivEditOtherInfo -> {

                }

                R.id.ivEditOtherDelete -> {
                    positionDeleted = pos
                    showDeletePopup(v, m.id)
                }
            }
        }
        binding.rvCoOwners.adapter = adapter
    }

    private lateinit var addEditWindow: PopupWindow
    private fun showDeletePopup(view: View, id: Int) {
        val inflater: LayoutInflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding: PopupDeleteBinding =
            DataBindingUtil.inflate(inflater, R.layout.popup_delete, null, false)
        addEditWindow = PopupWindow(
            viewBinding.root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        viewBinding.tvDeleteCoOwner.setOnClickListener {
            deleteCoOwner(id)
        }

        viewBinding.tvCancelCoOwner.setOnClickListener {
            if (addEditWindow.isShowing) {
                addEditWindow.dismiss()
            }
        }
        addEditWindow.showAsDropDown(view, 0, -15, Gravity.NO_GRAVITY)
    }
}