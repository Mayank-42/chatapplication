package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.local.tables.CinversationId
import com.example.chatapplication.Data.network.ApiService

class convoInfoRepo(
    private var work: conversationId,
    private var api: ApiService
) {
    var getingConvoInfo=work.getAllConvoInfo()
    suspend fun insertConvoInfo(info: CinversationId){
        work.putingInfo(info)
    }

    suspend fun putingInfoLocaly(info: CinversationId){
        api.getConvoInfo()
    }

}