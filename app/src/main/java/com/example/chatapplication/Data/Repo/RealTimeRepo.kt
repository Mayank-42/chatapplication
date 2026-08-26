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

    private val conversationChannel =
        SupaBaseClient.supabase.channel("conversation-realtime")

    private var messageSubscribed = false
    private var conversationMemberSubscribed = false
    private var conversationChannelSubscribed = false


    fun messageInsertFlow(): Flow<PostgresAction.Insert> {

        println("REALTIME: CREATING MESSAGE INSERT FLOW")
        println("REALTIME: SCHEMA = public")
        println("REALTIME: TABLE = message")

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

    fun conversationInsertFlow(): Flow<PostgresAction.Insert> {
        return conversationChannel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "conversation"
        }
    }


    suspend fun unsubscribeMessages() {
        messageChannel.unsubscribe()
        messageSubscribed = false
    }

    suspend fun unsubscribeConversationMembers() {
        conversationMemberChannel.unsubscribe()
        conversationMemberSubscribed = false
    }
    suspend fun subscribeConversations() {
        if (conversationChannelSubscribed) {
            println("REALTIME: CONVERSATION CHANNEL ALREADY SUBSCRIBED")
            return
        }
        println("REALTIME: SUBSCRIBING CONVERSATION CHANNEL")

        conversationChannel.subscribe(
            blockUntilSubscribed = true
        )
        conversationChannelSubscribed = true
        println("REALTIME: CONVERSATION CHANNEL SUBSCRIBED")
    }
    suspend fun unsubscribeConversations() {
        conversationChannel.unsubscribe()
        conversationChannelSubscribed = false
    }
}