package com.nuvyz.core.base.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nuvyz.core.data.model.response.ApiStatus

abstract class BaseBottomSheet<VB: ViewDataBinding>(@LayoutRes private val layoutRes: Int): BottomSheetDialogFragment() {

    protected var binding: VB? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setOnShowListener {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            requireActivity().currentFocus?.clearFocus()
        }
    }

    open fun show(manager: FragmentManager) {
        show(manager, javaClass.name)
    }

    protected fun intString(@StringRes string: Int): String {
        return requireContext().getString(string)
    }

    override fun dismiss() {
        if (isShowing) {
            dialog?.dismiss()
        }
    }

    val isShowing get() = dialog?.isShowing ?: false

    open fun listen(manager: FragmentManager, status: Int) {
        if (status == ApiStatus.LOADING) {
            show(manager)
        } else {
            dismiss()
        }
    }

    protected fun expand() {
        dialog?.setOnShowListener {
            // This is gotten directly from the source of BottomSheetDialog
            // in the wrapInBottomSheet() method
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let {
                // Right here!
                BottomSheetBehavior.from(it).setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }
}