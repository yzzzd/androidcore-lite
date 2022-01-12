package com.nuryazid.core.ui.loading

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.nuryazid.core.R
import com.nuryazid.core.databinding.CoreDialogLoadingBinding

class LoadingDialog(context: Context) {

    private var binding: CoreDialogLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.core_dialog_loading, null, false)
    private var dialog: AlertDialog = AlertDialog.Builder(context).apply {
        setView(binding.root)
    }.create()

    fun show(message: String?, loading: Boolean = true): LoadingDialog {
        binding.loading = loading
        binding.message = message ?: ""
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

}