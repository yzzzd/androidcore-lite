package com.nuvyz.app.data.source.remote

import com.nuvyz.core.data.model.response.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // region auth

    @FormUrlEncoded
    @POST("api/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Header("regid") regId: String,
        @Header("platform") platform: String
    ): ApiResponse<Any>
}