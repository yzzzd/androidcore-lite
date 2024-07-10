package com.nuvyz.core.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonExtension {
    inline fun <reified T> T.toJson(): String? {
        return if (this == null) null else Gson().toJson(this)
    }

    inline fun <reified T> List<T>.toJson(): String? {
        return if (this.isEmpty()) null else Gson().toJson(this)
    }

    inline fun <reified T> String.toObject(): T? {
        return if (isNullOrEmpty()) null else Gson().fromJson(this, T::class.java)
    }

    inline fun <reified T> String.toList(): List<T>? {
        return if (isNullOrEmpty()) null else Gson().fromJson(this, object : TypeToken<ArrayList<T>?>() {}.type)
    }
}