package com.nuvyz.app.base.fragment

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.nuvyz.core.base.fragment.CoreFragment

abstract class BaseFragment<VB: ViewDataBinding, VM: ViewModel>(@LayoutRes private val layoutRes: Int): CoreFragment<VB>(layoutRes) {

    protected abstract val viewModel: VM

    protected var reloadData = false
}