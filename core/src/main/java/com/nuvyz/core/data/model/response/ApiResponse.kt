package com.nuvyz.core.data.model.response

open class ApiResponse<T>(
    var status: Int = ApiStatus.ERROR,
    var message: String = "",
    val data: T? = null
) {
    val isSuccessful get() = status == ApiStatus.SUCCESS
}