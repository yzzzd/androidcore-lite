package com.nuvyz.core.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.nuvyz.core.utils.ActivityExtension.compressFileUpload
import java.io.File

class Session(private val context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    @PublishedApi
    internal val sharedPreferences = EncryptedSharedPreferences.create(
        PREF,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun save(key: String, value: Any) {
        val pref = sharedPreferences.edit()
        when(value) {
            is String -> pref.putString(key, value).apply()
            is Int -> pref.putInt(key, value).apply()
            is Boolean -> pref.putBoolean(key, value).apply()
            is Float -> pref.putFloat(key, value).apply()
            is Long -> pref.putLong(key, value).apply()
        }
    }

    inline fun <reified T> get(key: String): T? {
        return when (T::class) {
            String::class -> sharedPreferences.getString(key, null)
            Int::class -> sharedPreferences.getInt(key, 0)
            Boolean::class -> sharedPreferences.getBoolean(key, false)
            Float::class -> sharedPreferences.getFloat(key, 0f)
            Long::class -> sharedPreferences.getLong(key, 0L)
            else -> null
        } as T?
    }

    suspend fun compressFileUpload(file: File): File {
        return context.compressFileUpload(file)
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val PREF = "nuvdata"
    }
}