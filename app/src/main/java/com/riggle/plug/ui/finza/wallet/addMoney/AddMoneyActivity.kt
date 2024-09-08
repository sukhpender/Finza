package com.riggle.plug.ui.finza.wallet.addMoney

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.riggle.plug.R
import com.riggle.plug.databinding.ActivityAddMoneyBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.utils.showErrorToast
import com.riggle.plug.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class AddMoneyActivity : BaseActivity<ActivityAddMoneyBinding>(), PaymentResultWithDataListener {

    private val viewModel: AddMoneyActivityVM by viewModels()

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
        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_test_Nqy8gmPWtyPySL")

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
                    } else if (binding.amount != "" && binding.amount!!.toInt() < 200) {
                        showErrorToast("Minimum amount required 200")
                    } else if (binding.amount != "" && binding.amount!!.toInt() > 10000) {
                        showErrorToast("Maximum amount 10000")
                    } else {
                        sharedPrefManager.getCurrentUser()?.let { it1 ->
                            viewModel.createCustomer(
                                sharedPrefManager.getToken().toString(),
                                it1.full_name,
                                it1.email,
                                it1.phone_number)
                        }
//                        val totalAmount = binding.amount!!.toInt() * 1000
//                        initPayment(totalAmount.toString())
                    }
                    //  startActivity(SelectPaymentMethodActivity.newIntent(this))
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
            prefill.put("email", "sukhpenderpanghal123@gmail.com")
            prefill.put("contact", "9813301662")

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            showErrorToast("Error in payment: " + e.message)
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Log.e("PaymentLoad", p1?.data.toString())
        showSuccessToast(p1!!.paymentId)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        showErrorToast("Payment failed")
    }
}