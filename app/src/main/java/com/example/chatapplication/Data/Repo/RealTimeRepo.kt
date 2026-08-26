package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.network.clients.SupaBaseClient
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow

class RealTimeRepo {

    private val messageChannel =
        SupaBaseClient.supabase.channel("message-realtime")

    private val conversationMemberChannel =
        SupaBaseClient.supabase.channel("conversation-member-realtime")

    private var messageSubscribed = false
    private var conversationMemberSubscribed = false


    fun messageInsertFlow(): Flow<PostgresAction.Insert> {

        return messageChannel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "message"
        }
    }


    fun conversationMemberInsertFlow(): Flow<PostgresAction.Insert> {
        return conversationMemberChannel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "conversation_member"
        }
    }


    suspend fun subscribeMessages() {
        if (messageSubscribed) {
            println("REALTIME: MESSAGE CHANNEL ALREADY SUBSCRIBED")
            return
        }
        println("REALTIME: SUBSCRIBING MESSAGE CHANNEL")
        messageChannel.subscribe(blockUntilSubscribed = true)
        messageSubscribed = true
        println("REALTIME: MESSAGE CHANNEL SUBSCRIBED")
    }


    suspend fun subscribeConversationMembers() {
        if (conversationMemberSubscribed) {
            println("REALTIME: CONVERSATION MEMBER CHANNEL ALREADY SUBSCRIBED")
            return
        }
        println("REALTIME: SUBSCRIBING CONVERSATION MEMBER CHANNEL")
        conversationMemberChannel.subscribe(blockUntilSubscribed = true)
        conversationMemberSubscribed = true
        println("REALTIME: CONVERSATION MEMBER CHANNEL SUBSCRIBED")
    }


    suspend fun unsubscribeMessages() {

        messageChannel.unsubscribe()
        messageSubscribed = false
    }


    suspend fun unsubscribeConversationMembers() {

        conversationMemberChannel.unsubscribe()
        conversationMemberSubscribed = false
    }
}