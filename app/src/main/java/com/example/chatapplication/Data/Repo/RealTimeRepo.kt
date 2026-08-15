package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.network.clients.SupaBaseClient
import com.example.chatapplication.Data.network.clients.SupaBaseClient.supabase
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RealTimeRepo( private val tokenManager: TokenManager) {

    private val channel =
        SupaBaseClient.supabase.channel("message-realtime")

    fun messageInsertFlow(): Flow<PostgresAction.Insert> {
        return channel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "message"
        }
    }

    suspend fun subscribe() {
        channel.subscribe(blockUntilSubscribed = true)
    }
    suspend fun UnSubscriber(){
        channel.unsubscribe()
    }
    }
