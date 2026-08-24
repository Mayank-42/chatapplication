package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.local.tables.CinversationId

class convoInfoRepo(
    private var work: conversationId
) {
    var getingConvoInfo=work.getAllConvoInfo()
    suspend fun insertConvoInfo(info: CinversationId){
        work.putingInfo(info)
    }

}