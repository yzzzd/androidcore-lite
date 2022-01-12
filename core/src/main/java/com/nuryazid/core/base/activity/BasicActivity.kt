package com.nuryazid.core.base.activity

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.nuryazid.core.api.ApiResponse
import com.nuryazid.core.ui.loading.LoadingDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by @yzzzd on 4/22/18.
 */

abstract class BasicActivity<VB: ViewDataBinding>(@LayoutRes private val layoutRes: Int): AppCompatActivity() {

    protected lateinit var binding: VB

    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(apiResponse: ApiResponse) {
        if (apiResponse.toast) {
            Toast.makeText(this, apiResponse.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}