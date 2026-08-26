package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.local.tables.CinversationId
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.request.ConversationRequest
import com.example.chatapplication.Data.network.request.conversationIdRequest
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.serialization.json.jsonPrimitive

class convoInfoRepo(
    private var work: conversationId,
    private var api: ApiService
) {
    var getingConvoInfo=work.getAllConvoInfo()

//    suspend fun insertConvoInfo(info: CinversationId){
//        work.putingInfo()
//    }

    suspend fun syncConversations(id:String) {
        val request = conversationIdRequest(
            p_user_id = id
        )
        val response = api.getConvoInfo(request)
        if (response.isSuccessful) {
//            val conversations = response.body() ?: emptyList()
            val conversations = response.body() ?: emptyList()
                val localConversation = conversations.map{convo->
                    CinversationId(
                        conversationId = convo.conversation_id,
                        type = convo.type,
                        name = convo.name,
                        lastMessage = convo.lastMessage,
                        lastTime = convo.lastTime,
                        Image = convo.Image,
                        last_message_id  = convo.last_message_id,
                        unread_count= convo.unread_count
                    )
                }
                work.putingInfo(localConversation)
            }
         else {
            println("CONVERSATION SYNC ERROR = ${response.errorBody()?.string()}")
        }
    }
    suspend fun handleConversationMemberEvent(
        event: PostgresAction.Insert,
        myUserId: String
    ) {
        val record = event.record
        val userId =
            record["user_id"]?.jsonPrimitive?.content
        if (userId != myUserId) {
            return
        }
        val conversationId =
            record["conversation_id"]?.jsonPrimitive?.content
                ?: return
        // Get this conversation from backend
        // Convert it to CinversationId
        // Upsert it into Room
    }
        suspend fun updateLastMessage(
            conversationId: String,
            messageId: String,
            message: String,
            time: String
        ) {
            work.updateLastMessage(
                conversationId = conversationId,
                messageId = messageId,
                message = message,
                time = time
            )
        }

}