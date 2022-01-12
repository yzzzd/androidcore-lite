package com.nuryazid.core.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * Created by @yzzzd on 4/22/18.
 */

abstract class CoreActivity<VB: ViewDataBinding, VM: ViewModel>(@LayoutRes private val layoutRes: Int): BasicActivity<VB>(layoutRes) {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }
}