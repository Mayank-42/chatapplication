package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.local.tables.CinversationId
import com.example.chatapplication.Data.network.ApiService

class convoInfoRepo(
    private var work: conversationId,
    private var api: ApiService
) {
    var getingConvoInfo=work.getAllConvoInfo()

//    suspend fun insertConvoInfo(info: CinversationId){
//        work.putingInfo()
//    }

    suspend fun syncConversations() {
        val response = api.getConvoInfo()
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