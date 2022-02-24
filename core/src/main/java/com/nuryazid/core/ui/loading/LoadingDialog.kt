package com.nuryazid.core.ui.loading

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.nuryazid.core.R
import com.nuryazid.core.databinding.CoreDialogLoadingBinding

/**
 * Created by @yzzzd on 4/22/18.
 */

class LoadingDialog(context: Context) {

    private var binding = CoreDialogLoadingBinding.inflate(LayoutInflater.from(context))
    private var dialog: AlertDialog = AlertDialog.Builder(context).apply {
        setView(binding.root)
        setOnDismissListener { dismissListener?.invoke(this@LoadingDialog) }
    }.create()

    private var dismissListener: ((LoadingDialog) -> Unit)? = null

    fun show(message: String?, loading: Boolean = true): LoadingDialog {
        binding.progressBar.isVisible = loading
        binding.tvMessage.text = message ?: ""
        dialog.setCancelable(!loading)
        show()
        return this
    }

    private fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun onDismiss(dismissListener: (dialog: LoadingDialog) -> Unit): LoadingDialog {
        this.dismissListener = dismissListener
        return this
    }

}