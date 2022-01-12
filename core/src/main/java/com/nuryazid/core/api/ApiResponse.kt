package com.nuryazid.core.api

import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

/**
 * Created by @yzzzd on 4/22/18.
 */

open class ApiResponse(
    var status: ApiStatus = ApiStatus.SUCCESS,
    var message: String? = null,
    var flag: String? = null,
    var toast: Boolean = false
) {
    fun responseSuccess(message: String? = null, flag: String? = null): ApiResponse {
        this.status = ApiStatus.SUCCESS
        this.message = message
        this.flag = flag
        return this
    }

    fun responseLoading(message: String? = null, flag: String? = null): ApiResponse {
        this.status = ApiStatus.LOADING
        this.message = message
        this.flag = flag
        return this
    }

    fun responseError(error: Throwable? = null, flag: String? = null): ApiResponse {
        this.status = ApiStatus.ERROR
        this.message = error?.message
        this.flag = flag

        error?.let {
            if (it is HttpException) {
                it.response()?.errorBody()?.string()?.let { errorBody ->
                    message = try {
                        val responseJson = JSONObject(errorBody)
                        responseJson.getString("message")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        e.message
                    }
                }
            }
        }

        return this
    }
}