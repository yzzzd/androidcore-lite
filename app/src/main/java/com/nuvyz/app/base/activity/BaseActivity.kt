package com.nuvyz.app.base.activity

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.nuvyz.core.base.activity.CoreActivity

abstract class BaseActivity<VB: ViewDataBinding, VM: ViewModel>(@LayoutRes layoutRes: Int): CoreActivity<VB>(layoutRes) {
    protected abstract val viewModel: VM
}