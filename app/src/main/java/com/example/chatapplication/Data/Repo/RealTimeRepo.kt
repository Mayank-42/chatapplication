package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.network.clients.SupaBaseClient
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.presenceDataFlow
import io.github.jan.supabase.realtime.track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


import kotlinx.serialization.Serializable

@Serializable
data class PresenceState(
    val user_id: String
)
class RealTimeRepo {

    private val messageChannel =
        SupaBaseClient.supabase.channel("message-realtime")

    private val conversationMemberChannel =
        SupaBaseClient.supabase.channel("conversation-member-realtime")

    private val conversationChannel =
        SupaBaseClient.supabase.channel("conversation-realtime")

    private val _onlineUsers =
        MutableStateFlow<Set<String>>(emptySet())


    // Register the postgres flows BEFORE subscribing/joining the channels.
    private val messageFlow =
        messageChannel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "message"
        }

    private val conversationMemberFlow =
        conversationMemberChannel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "conversation_member"
        }

    private val conversationFlow =
        conversationChannel.postgresChangeFlow<PostgresAction.Insert>(
            schema = "public"
        ) {
            table = "conversation"
        }


    private val presenceChannel = SupaBaseClient.supabase.channel("online-users")

    val onlineUsers: StateFlow<Set<String>> get() = _onlineUsers
    private var presenceSubscribed = false

    private var messageSubscribed = false
    private var conversationMemberSubscribed = false
    private var conversationChannelSubscribed = false


    fun messageInsertFlow(): Flow<PostgresAction.Insert> {
        println("REALTIME: RETURNING MESSAGE FLOW")
        return messageFlow
    }


    fun conversationMemberInsertFlow(): Flow<PostgresAction.Insert> {
        println("REALTIME: RETURNING CONVERSATION MEMBER FLOW")
        return conversationMemberFlow
    }


    fun conversationInsertFlow(): Flow<PostgresAction.Insert> {
        println("REALTIME: RETURNING CONVERSATION FLOW")
        return conversationFlow
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


    suspend fun subscribeConversations() {
        if (conversationChannelSubscribed) {
            println("REALTIME: CONVERSATION CHANNEL ALREADY SUBSCRIBED")
            return
        }
        println("REALTIME: SUBSCRIBING CONVERSATION CHANNEL")
        conversationChannel.subscribe(blockUntilSubscribed = true)
        conversationChannelSubscribed = true
        println("REALTIME: CONVERSATION CHANNEL SUBSCRIBED")
    }


    suspend fun unsubscribeMessages() {
        if (!messageSubscribed) {
            return
        }
        println("REALTIME: UNSUBSCRIBING MESSAGE CHANNEL")
        messageChannel.unsubscribe()
        messageSubscribed = false
        println("REALTIME: MESSAGE CHANNEL UNSUBSCRIBED")
    }
    suspend fun unsubscribeConversationMembers() {

        if (!conversationMemberSubscribed) {
            return
        }

        println("REALTIME: UNSUBSCRIBING CONVERSATION MEMBER CHANNEL")

        conversationMemberChannel.unsubscribe()
        conversationMemberSubscribed = false

        println("REALTIME: CONVERSATION MEMBER CHANNEL UNSUBSCRIBED")
    }


    suspend fun unsubscribeConversations() {

        if (!conversationChannelSubscribed) {
            return
        }

        println("REALTIME: UNSUBSCRIBING CONVERSATION CHANNEL")

        conversationChannel.unsubscribe()

        conversationChannelSubscribed = false

        println(
            "REALTIME: CONVERSATION CHANNEL UNSUBSCRIBED"
        )
    }
    suspend fun subscribePresence(userId: String) {
        if (presenceSubscribed) {
            println("PRESENCE: ALREADY SUBSCRIBED")
            return
        }
        println("PRESENCE: SUBSCRIBING")
        presenceChannel.subscribe(
            blockUntilSubscribed = true
        )
        presenceChannel.track(
            mapOf(
                "user_id" to userId
            )
        )
        presenceSubscribed = true
        println("PRESENCE: USER $userId IS ONLINE")
    }
    suspend fun observePresence() {

        presenceChannel
            .presenceDataFlow<PresenceState>()
            .collect { presenceList ->

                val users = presenceList
                    .map { it.user_id }
                    .toSet()

                _onlineUsers.value = users

                println(
                    "PRESENCE: ONLINE USERS = $users"
                )
            }
    }
}