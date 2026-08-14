package com.example.chatapplication.Data.network.clients

import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.constant
import com.example.chatapplication.Data.network.constant.Retro_Base_url
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetroFitClient {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .addHeader("apikey", constant.supaBaseKey)
                .addHeader("Content-Type", "application/json")
                .build()

            chain.proceed(request)
        }
        .build()
    private val AuthRetroFit= Retrofit.Builder()
        .baseUrl(Retro_Base_url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val AuthApiService=AuthRetroFit.create(ApiService::class.java)
}