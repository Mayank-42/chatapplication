package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.request.ConversationRequest
import com.example.chatapplication.Data.network.request.GetMessageRequest
import com.example.chatapplication.Data.network.request.MessageInfoRequest
import com.example.chatapplication.Data.network.response.ConversationResponse
import retrofit2.Response

class MessageRepo(
    private val getMessage: ApiService,
    private val localWork: operation
){
    suspend fun putMessage(conversationId:String,msg:String):Response<Unit>{
        val request= MessageInfoRequest(conversationId,msg)
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
        var localMsgList=serverList?.map{
            MessageInfo(
                id = it.id,
                conversationId = it.conversationId,
                sender_Id = it.senderId,
                reciver_Id = it.receiverId,
                message = it.message,
                date = it.timeStamp
            )
        }

    if (localMsgList != null) {
        localWork.localInsert(localMsgList)
    }
    }
    suspend fun getOrCreateConversation(
        otherUserId: String
    ): Response<List<ConversationResponse>> {

        val request = ConversationRequest(
            p_user_id = otherUserId
        )

        return getMessage.getOrCreateConversation(request)
    }
}