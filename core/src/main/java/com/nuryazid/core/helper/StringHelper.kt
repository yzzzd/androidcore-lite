package com.nuryazid.core.helper

import android.util.Base64

object StringHelper {
    fun generateBasicAuth(username: String, password: String) = "Basic ${Base64.encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP)}"

    fun generateBearerAuth(token: String) = "Bearer $token"
}