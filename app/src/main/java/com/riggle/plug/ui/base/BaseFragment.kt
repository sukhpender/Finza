package com.riggle.plug.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.riggle.plug.BR
import com.riggle.plug.data.local.SharedPrefManager
import com.riggle.plug.utils.LoadingDialog
import com.riggle.plug.utils.hideKeyboard
import javax.inject.Inject


abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {
    lateinit var binding: Binding


    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var progressDialogAvl: ProgressDialogAvl
    private var loaderDialog: LoadingDialog? = null

    val parentActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateView(view)
        progressDialogAvl = ProgressDialogAvl(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.inflate(layoutInflater, layout,container,false)
        loaderDialog = LoadingDialog(requireContext())
        val vm = getViewModel()
        binding.setVariable(BR.vm, vm)
        vm.onUnAuth.observe(viewLifecycleOwner, Observer {
            val activity = requireActivity() as BaseActivity<*>
            activity.showUnauthorised()
        })

        return binding.root
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView(view: View)
    override fun onPause() {
        super.onPause()
        activity?.hideKeyboard()
    }


    fun showHideLoader(state: Boolean) {
        if (loaderDialog != null) {
            if (state) loaderDialog?.show()
            else loaderDialog?.dismiss()
        }
    }

    fun showLoading() {
        parentActivity?.showLoading()
    }

    fun hideLoading(){
        parentActivity?.hideLoading()
    }

    fun showToast(msg: String? = "Something went wrong !!") {
        Toast.makeText(requireContext(), msg ?: "Showed null value !!", Toast.LENGTH_SHORT).show()
    }

}