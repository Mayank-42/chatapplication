package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.clients.SupaBaseClient
import com.example.chatapplication.Data.network.request.ConversationRequest
import com.example.chatapplication.Data.network.request.ConversationSeenRequest
import com.example.chatapplication.Data.network.request.GetMessageRequest
import com.example.chatapplication.Data.network.request.MessageInfoRequest
import com.example.chatapplication.Data.network.request.MessageStatusRequest
import com.example.chatapplication.Data.network.response.ConversationResponse
import com.example.chatapplication.Data.network.response.MessageInfoResponse
import io.github.jan.supabase.auth.auth
import retrofit2.Response

class MessageRepo(
    private val getMessage: ApiService,
    private val localWork: operation
){
    suspend fun putMessage(id:String ,conversationId:String, msg:String):Response<Unit>{
        val request= MessageInfoRequest(id, conversationId,msg)
        return getMessage.storeMessage(request)
    }

    suspend fun converting(conversationId: String,time:String,id:String){
    val request = GetMessageRequest(
        conversation_id = conversationId,
        last_timestamp = if (time.isBlank()) null else time,
        last_id = if (id.isBlank()) null else id
    )
        val response=getMessage.getingMessage(request)
    println("SYNC: STATUS = ${response.code()}")
    println("SYNC: BODY = ${response.body()}")
    println("SYNC: ERROR = ${response.errorBody()?.string()}")
        var serverList= response.body()
        val localMsgList = serverList?.map {
            MessageInfo(
                id = it.id,
                conversationId = it.conversationId,
                sender_Id = it.senderId,
                reciver_Id = it.receiverId,
                message = it.message,
                date = it.timeStamp,
                status = it.status
            )
        }

        if (localMsgList != null) {

            println("ROOM SYNC: INSERTING ${localMsgList.size} MESSAGES")

            localWork.localInsert(localMsgList)

            println("ROOM SYNC: INSERT COMPLETE")

            // ------------------------------------------------
            // Mark newly synced incoming messages DELIVERED
            // ------------------------------------------------
            for (message in localMsgList) {

                if (
                    message.sender_Id !=
                    SupaBaseClient.supabase.auth.currentUserOrNull()?.id &&
                    message.status == "SENT"
                ) {

                    try {

                        println(
                            "SYNC DELIVERED: MESSAGE ID = ${message.id}"
                        )

                        val deliveredResponse =
                            markMessageDelivered(message.id)

                        println(
                            "SYNC DELIVERED: SERVER STATUS = ${deliveredResponse.code()}"
                        )

                        println(
                            "SYNC DELIVERED: ERROR = ${
                                deliveredResponse.errorBody()?.string()
                            }"
                        )

                    } catch (e: Exception) {

                        println(
                            "SYNC DELIVERED: ERROR = ${e.message}"
                        )

                    }
                }
            }
        }
    }
    suspend fun getOrCreateConversation(
        otherUserId: String
    ): Response<List<ConversationResponse>> {

        val request = ConversationRequest(
            other_user_id = otherUserId
        )

        return getMessage.getOrCreateConversation(request)
    }
    suspend fun markMessageDelivered(
        messageId: String
    ): Response<Unit> {

        return getMessage.markMessageDelivered(
            MessageStatusRequest(
                p_message_id = messageId
            )
        )
    }
    suspend fun markConversationSeen(
        conversationId: String
    ): Response<Unit> {

        return getMessage.markConversationSeen(
            ConversationSeenRequest(
                p_conversation_id = conversationId
            )
        )
    }
}