package com.riggle.plug.ui.more.myAccount

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.riggle.plug.R
import com.riggle.plug.data.model.CompanyUserProfile
import com.riggle.plug.databinding.FragmentEditBusinessBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBusinessFragment : BaseFragment<FragmentEditBusinessBinding>(),
    AdapterView.OnItemSelectedListener {

    private val viewModel: EditBusinessFragmentVM by viewModels()
    private lateinit var homeActivity: HomeActivity
    private var proof = arrayOf("GST Certificate", "Shop act license")

    companion object{
        var companyModel: CompanyUserProfile? = null
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_edit_business
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        homeActivity = activity as HomeActivity

        if (companyModel != null){
            binding.bean = companyModel

            if (binding.bean?.full_address != null) {
                val namesList: List<String> = binding.bean!!.full_address.split(",")
                if (namesList[0] != "") {
                    binding.etvAddress.setText(namesList[0])
                }
                if (namesList[1] != "") {
                    binding.etvAddressBuilding.setText(namesList[1])
                }
                if (namesList[2] != "") {
                    binding.etvAddressCity.setText(namesList[2])
                }
                if (namesList[3] != "") {
                    binding.etvAddressState.setText(namesList[3])
                }
                if (namesList[4] != "") {
                    binding.etvAddressPinCode.setText(namesList[4])
                }
            }
        }


        initBusinessProofAdapter()

    }

    private fun initBusinessProofAdapter() {
        binding.spProof.onItemSelectedListener = this
        val aa = ArrayAdapter(homeActivity, android.R.layout.simple_list_item_1, proof)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spProof.adapter = aa
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivEditBusinessInfoBack -> {
                    homeActivity.onBackPressed()
                }

                R.id.tvGstValidate -> {

                }

                R.id.tvUploadBusinessProofDocu -> {

                }

                R.id.tvUploadPanCard -> {

                }

                R.id.tvSaveBusinessAccount -> {

                }
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}