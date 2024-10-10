package com.riggle.finza_finza.ui.finza.wallet.addMoney

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.riggle.finza_finza.R
import com.riggle.finza_finza.data.model.AcquirerData
import com.riggle.finza_finza.data.model.PaymentStoreRequest
import com.riggle.finza_finza.databinding.ActivityAddMoneyBinding
import com.riggle.finza_finza.ui.base.BaseActivity
import com.riggle.finza_finza.ui.base.BaseViewModel
import com.riggle.finza_finza.ui.finza.wallet.WalletActivity
import com.riggle.finza_finza.utils.Status
import com.riggle.finza_finza.utils.showErrorToast
import com.riggle.finza_finza.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@AndroidEntryPoint
class AddMoneyActivity : BaseActivity<ActivityAddMoneyBinding>(), PaymentResultWithDataListener {

    private val viewModel: AddMoneyActivityVM by viewModels()
    private var amount = ""

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, AddMoneyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_add_money
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {

        // RAZORPAY_KEY=rzp_test_xwRyHpFkgGpMMI
        // RAZORPAY_SECRET=SnRc6UhBJrKqAqloH4MhTZty use these in application side
        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_test_xwRyHpFkgGpMMI")

        initView()
        initOnClick()
    }

    private fun initView() {
        binding.amount = "500"

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.amount = s.toString()
//                if (s.toString() != "" && s.toString().toInt() >= 200){
//                    binding.amount = s.toString()
//                }else{
//                    showErrorToast("Minimum amount 200.")
//                }
            }
        })

        viewModel.obrCreateCustomer.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
//                    if (it.data != null) {
//                        it.data.message.let { it1 -> showSuccessToast(it1) }
//                    }
                    it.data?.data?.let { it1 -> sharedPrefManager.saveWalletUser(it1) }
                    val totalAmount = binding.amount!!.toInt() * 1000
                    amount = (totalAmount / 1000).toString()
                    initPayment(totalAmount.toString())
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

        viewModel.obrStorePayment.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    WalletActivity.isMoneyAdded.value = true
                    showHideLoader(false)
                    if (it.data != null) {
                        // it.data.message.let { it1 -> showSuccessToast(it1) }
                    }
                    finish()
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

        viewModel.obrPaymentLinkCreate.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    showSuccessToast(it.data?.message.toString())
                    finish()
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

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv1 -> {
                    finish()
                }

                R.id.ll3 -> { // select payment method
                    // value will ended in subunits like for 1 Ruppe send 1000
                    if (binding.amount == "") {
                        showErrorToast("Enter minimum amount 200")
                    } else {
                        sharedPrefManager.getCurrentUser()?.let { it1 ->
                            val totalAmount = binding.amount!!.toInt() * 1000
                            amount = (totalAmount / 1000).toString()
                            viewModel.createPaymentLink(
                                sharedPrefManager.getToken().toString(),
                                amount
                            )
                            // viewModel.createCustomer(sharedPrefManager.getToken().toString())
                        }
                    }

//                    else if (binding.amount != "" && binding.amount!!.toInt() < 200) {
//                        showErrorToast("Minimum amount required 200")
//                    } else if (binding.amount != "" && binding.amount!!.toInt() > 10000) {
//                        showErrorToast("Maximum amount 10000")
//                   608268 001 0624245 }

                }

                R.id.tv500 -> {
                    binding.amount = "500"
                }

                R.id.tv1000 -> {
                    binding.amount = "1000"
                }

                R.id.tv2000 -> {
                    binding.amount = "2000"
                }

                R.id.tv5000 -> {
                    binding.amount = "5000"
                }

                R.id.tv9 -> { // continue

                }
            }
        }
    }

    private fun initPayment(amount: String) {
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Finza")
            options.put("description", "Add Money in Finza Wallet.")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "http://example.com/image/rzp.jpg")
            options.put("theme.color", "#2432db")
            options.put("currency", "INR")
            // options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount", amount)//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", sharedPrefManager.getCurrentUser()?.email.toString())
            prefill.put("contact", sharedPrefManager.getCurrentUser()?.phone_number.toString())

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            showErrorToast("Error in payment: " + e.message)
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        try {
            val acquireData = AcquirerData(
                rrn = ""
            )
            val requestBody = PaymentStoreRequest(
                acquirer_data = acquireData,
                amount = amount.toInt(),
                amount_refunded = 0,
                bank = "",
                captured = false,
                card_id = "",
                contact = p1?.userContact.toString(),
                created_at = getFormattedCurrentTime(),
                currency = "INR",
                customer_id = sharedPrefManager.getWalletUser()?.razorpay_customer_id.toString(),
                description = "",
                email = sharedPrefManager.getCurrentUser()?.email.toString(),
                entity = "payment",
                error_code = "",
                error_description = "",
                error_reason = "",
                error_source = "",
                error_step = "",
                fee = 0,
                id = p1?.paymentId.toString(),
                international = false,
                invoice_id = "",
                method = "upi",
                notes = emptyList(),
                order_id = "",
                refund_status = "",
                status = "captured",
                tax = 0,
                vpa = "",
                wallet = ""
            )
            viewModel.storePayment(sharedPrefManager.getToken().toString(), requestBody)

        } catch (e: Exception) {
            showErrorToast(e.localizedMessage)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        showErrorToast(p2?.data.toString())
    }

    private fun getFormattedCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dateFormat.timeZone = TimeZone.getDefault()
        return dateFormat.format(Date())
    }
}