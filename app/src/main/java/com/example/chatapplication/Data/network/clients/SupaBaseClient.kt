package com.example.chatapplication.Data.network.clients

import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.network.constant
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.okhttp.OkHttp
import io.github.jan.supabase.storage.Storage
//import io.ktor.client.engine.android.Android

object SupaBaseClient {

    lateinit var supabase: SupabaseClient
        private set

    fun initialize(tokenManager: TokenManager) {

        supabase = createSupabaseClient(
            supabaseUrl = constant.Supabase_url,
            supabaseKey = constant.supaBaseKey
        ) {
            httpEngine = OkHttp.create()

            accessToken = {
                tokenManager.getAccessToken() ?: ""
            }

            install(Realtime)
            install(Storage)
        }
    }
}