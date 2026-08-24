package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.local.tables.CinversationId
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.request.ConversationRequest
import com.example.chatapplication.Data.network.request.conversationIdRequest

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
            val conversations = response.body() ?: emptyList()
                val localConversation = conversations.map{convo->
                    CinversationId(
                        conversationId = convo.conversation_id,
                        type = convo.type,
                        name = convo.name,
                        lastMessage = convo.lastMessage,
                        lastTime = convo.lastTime,
                        Image = convo.Image
                    )
                }
                work.putingInfo(localConversation)
            }
         else {
            println("CONVERSATION SYNC ERROR = ${response.errorBody()?.string()}")
        }
    }

}