package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.local.operation
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.local.tables.userLoginInfo
import kotlinx.coroutines.flow.Flow

class reposatory( private val work: operation) {

    val getAllValue=work.getAllTheValue()

    fun getConversation(myId: String, userId: String
    ): Flow<List<MessageInfo>> {
        return work.getConversation(myId, userId)
    }

    suspend fun insert(task: MessageInfo){
        work.insert(task)
    }

    suspend fun loginInsert(cred: userLoginInfo){
        work.logininsert(cred)
    }
    suspend fun userInsert(cred: userInfo){
        work.userinsert(cred)
    }
    suspend fun delte(task: MessageInfo){
        work.delete(task)
    }
    suspend fun logindelte(ele: userLoginInfo){
        work.loginDelete(ele)
    }
    suspend fun userdelte(ele: userInfo){
        work.userDelete(ele)
    }
    suspend fun update(task: MessageInfo){
        work.update(task)
    }
}