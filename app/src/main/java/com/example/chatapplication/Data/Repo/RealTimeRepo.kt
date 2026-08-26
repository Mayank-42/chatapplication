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
    fun conversationMemberInsertFlow(): Flow<PostgresAction.Insert> {
        return channel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "conversation_member"
        }
    }
    /*
     This block establishes the listener for new messages.

 messageInsertFlow() returns a Flow of PostgreSQL INSERT events.
 We specifically listen to INSERT events because we only want to
 know when a new row is inserted into the message table.

 schema = "public" -> PostgreSQL schema
 table = "message" -> table we want to listen to
        below this
 subscribe() actually activates the Realtime channel.
 blockUntilSubscribed = true means the suspend call waits until
the channel has successfully become subscribed.
     */

    suspend fun subscribe() {
        channel.subscribe(blockUntilSubscribed = true)
    }
    suspend fun UnSubscriber(){
        channel.unsubscribe()
    }
    }
