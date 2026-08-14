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

//    val token = TokenManager(this)
    private lateinit var token: TokenManager
    fun initialize(context: Context) {
        token = TokenManager(context.applicationContext)
    }
    private val client=OkHttpClient.Builder()
        .addInterceptor { chain ->
            val accessToken = runBlocking {
                token.getAccessToken()
            }
    println("TOKEN BEING SENT = $accessToken")
            val request= chain.request()
                .newBuilder()
                .addHeader("apikey",constant.supaBaseKey)
                .addHeader("Authorization" ,"Bearer ${accessToken} ")
                .build()
            chain.proceed(request)
        }
        .build()
    private val retrofit= Retrofit.Builder()
        .baseUrl(constant.Base_url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}