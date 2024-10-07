package com.riggle.finza_finza.ui.finza.issueSuperTag.fastagReg

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.riggle.finza_finza.R
import com.riggle.finza_finza.databinding.ActivityFastagRegistrationBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FastagRegistrationActivity : BaseActivity<ActivityFastagRegistrationBinding>() {

    private val viewModel: FastagRegistrationActivityVM by viewModels()
    private val indianStates = listOf(
        "Andhra Pradesh",
        "Arunachal Pradesh",
        "Assam",
        "Bihar",
        "Chhattisgarh",
        "Goa",
        "Gujarat",
        "Haryana",
        "Himachal Pradesh",
        "Jharkhand",
        "Karnataka",
        "Kerala",
        "Madhya Pradesh",
        "Maharashtra",
        "Manipur",
        "Meghalaya",
        "Mizoram",
        "Nagaland",
        "Odisha",
        "Punjab",
        "Rajasthan",
        "Sikkim",
        "Tamil Nadu",
        "Telangana",
        "Tripura",
        "Uttar Pradesh",
        "Uttarakhand",
        "West Bengal"
    )
    private val vehicleStatus = listOf(
        "Active",
        "InActive")
    private val vehicleCategories = listOf(
        "LMV - Light Motor Vehicle",
        "HMV - Heavy Motor Vehicle",
        "MCV - Medium Commercial Vehicle",
        "HCV - Heavy Commercial Vehicle",
        "LCV - Light Commercial Vehicle"
    )
    private val vehicleColors = listOf(
        "White",
        "Black",
        "Silver",
        "Gray",
        "Red",
        "Blue",
        "Green",
        "Yellow",
        "Brown",
        "Beige",
        "Maroon",
        "Orange",
        "Purple",
        "Pink",
        "Gold",
        "Bronze",
        "Copper",
        "Teal",
        "Turquoise",
        "Turquoise Blue",
        "Metallic Blue",
        "Metallic Gray",
        "Pearl White",
        "Champagne",
        "Dark Blue",
        "Light Blue",
        "Dark Green",
        "Light Green",
        "Burgundy",
        "Plum",
        "Ivory",
        "Charcoal"
    )
    private val vehicleTypes = listOf(
        "Passenger Car",
        "Bus",
        "Truck",
        "Van",
        "Construction Vehicle",
        "Agricultural Vehicle",
        "Off-Road Vehicle",
        "Emergency Vehicle",
        "Luxury Vehicle",
        "Sports Car",
        "Delivery Vehicle",
        "Tow Truck"
    )

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, FastagRegistrationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_fastag_registration
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initView() {
        initStatesSP()
        initVehicleType()
        initVehicleStatus()
        initVehicleCategory()
        initVehicleColors()
    }

    private fun initStatesSP() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            indianStates
        )
        binding.spStates.adapter = adapter

        binding.spStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initVehicleType() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            vehicleTypes
        )
        binding.spType.adapter = adapter

        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initVehicleStatus() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            vehicleStatus
        )
        binding.spStatus.adapter = adapter
        binding.spStatus1.adapter = adapter

        binding.spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initVehicleCategory() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            vehicleCategories
        )
        binding.spCategory.adapter = adapter

        binding.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initVehicleColors() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            vehicleColors
        )
        binding.spColors.adapter = adapter

        binding.spColors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }
                R.id.tvRegister -> {
                    showSuccessToast("Registered successfully")
                    finish()
                }
            }
        }
    }
}