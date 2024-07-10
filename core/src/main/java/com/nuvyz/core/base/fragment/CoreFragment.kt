package com.nuvyz.core.base.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.nuvyz.core.base.activity.CoreActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class CoreFragment<VB: ViewDataBinding>(@LayoutRes private val layoutRes: Int): Fragment() {

    protected lateinit var binding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    protected fun snackBarMsg(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, duration).show()
    }

    fun startActivityFResult(intent: Intent) {
        (activity as CoreActivity<*>).startActivityFResult.launch(intent)
    }

    fun startIntentFResult(intent: IntentSenderRequest) {
        (activity as CoreActivity<*>).startIntentFResult.launch(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onResult(result: ActivityResult) {

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}