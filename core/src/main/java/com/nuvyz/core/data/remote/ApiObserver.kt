package com.nuvyz.core.data.remote

import com.google.gson.Gson
import com.nuvyz.core.data.model.response.ApiResponse
import com.nuvyz.core.data.model.response.ApiStatus
import org.json.JSONObject
import retrofit2.HttpException

class ApiObserver {
    companion object {
        inline fun <reified T> run(block: () -> T): T {
            return try {
                block.invoke()
            } catch (e: HttpException) {
                var message = ""
                e.response()?.errorBody()?.string()?.let { errorBody ->
                    message = try {
                        val errorJson = JSONObject(errorBody)
                        if (errorJson.has("message")) {
                            errorJson.getString("message")
                        } else {
                            e.localizedMessage ?: ""
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        e.localizedMessage ?: ""
                    }

                }
                create<T>(message)
            } catch (e: Exception) {
                create<T>(e.localizedMessage ?: "")
            }
        }

        inline fun <reified T> create(message: String): T {
            return try {
                val gson = Gson()
                val response = ApiResponse(status = ApiStatus.ERROR, message = message, data = null)
                gson.fromJson(gson.toJson(response), T::class.java)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}

/* "error": "expired_token", */