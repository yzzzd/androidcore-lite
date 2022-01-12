package com.nuryazid.core.api

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 * Created by @yzzzd on 4/22/18.
 */

open class ApiObserver(block: suspend () -> String, toast: Boolean = false, onSuccess: (response: JSONObject) -> Unit) {

    private var onErrorThrowable: ((Throwable) -> Unit)? = null

    init {
        val exception = CoroutineExceptionHandler { coroutineContext, throwable ->
            EventBus.getDefault().post(ApiResponse(toast = toast).responseError(throwable))
            onErrorThrowable?.invoke(throwable)
        }

        CoroutineScope(exception + Dispatchers.IO).launch {
            val response = block()
            val responseJson = JSONObject(response)
            onSuccess(responseJson)
        }
    }

    fun onError(onError: (e: Throwable) -> Unit) {
        onErrorThrowable = onError
    }
}