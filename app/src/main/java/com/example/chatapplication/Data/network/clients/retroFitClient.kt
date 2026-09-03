package com.example.chatapplication.Data.network.clients

import android.content.Context
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.constant
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retroFitClient {

    private lateinit var tokenManager: TokenManager

    fun initialize(context: Context) {
        tokenManager = TokenManager(context.applicationContext)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val accessToken = runBlocking {
                tokenManager.getAccessToken()
            }

            val requestBuilder = chain.request()
                .newBuilder()
                .addHeader(
                    "apikey",
                    constant.supaBaseKey
                )
                .addHeader(
                    "Content-Type",
                    "application/json"
                )

            if (!accessToken.isNullOrBlank()) {
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer $accessToken"
                )
            }

            println(
                "RETROFIT AUTH: TOKEN PRESENT = ${!accessToken.isNullOrBlank()}"
            )

            chain.proceed(
                requestBuilder.build()
            )
        }
        .build()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(constant.Base_url)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    val apiService =
        retrofit.create(ApiService::class.java)
}