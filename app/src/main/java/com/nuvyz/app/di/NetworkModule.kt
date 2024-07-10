package com.nuvyz.app.di

import com.nuvyz.core.data.Session
import com.nuvyz.app.data.source.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("BASE_URL/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())

        return retrofit.build().create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(session: Session): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()

                val local = session.get<String>(LOCALIZATION) ?: "en"
                requestBuilder.header(LOCALIZATION, local)

                session.get<String>(TOKEN)?.let { token ->
                    requestBuilder.header(AUTHORIZATION, token)
                }

                val request = requestBuilder.build()
                it.proceed(request)
            }
            .build()
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val TOKEN = "token"
        const val LOCALIZATION = "X-localization"
    }
}