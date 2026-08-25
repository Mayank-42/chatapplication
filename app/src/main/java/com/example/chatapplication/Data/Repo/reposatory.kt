package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.local.tables.userLoginInfo
import kotlinx.coroutines.flow.Flow

class reposatory( private val work: operation) {

    val getAllValue=work.getAllTheValue()

    fun getUnreadCount(conversationId: String, myUserId: String): Flow<Int> {
        return work.getUnreadCount(
            conversationId = conversationId,
            myUserId = myUserId
        )
    }

    suspend fun realtimeInsert(message: MessageInfo) {
        work.realtimeInsert(message)
    }

    suspend fun markMessagesAsRead(conversationId: String, myUserId: String) {
        work.markMessagesAsRead(
            conversationId = conversationId,
            myUserId = myUserId
        )
    }
    suspend fun getTimeId(conversationId: String): MessageInfo? {
        return work.getTimeId(conversationId)
    }

    fun getConversation(conversationId: String): Flow<List<MessageInfo>> {
        return work.getConversation(conversationId)
    }

    suspend fun updateMessageStatus(messageId: String, status: String) {
        work.updateMessageStatus(messageId = messageId, status = status)
    }




//    suspend fun getTimeId(myId: String, userId: String): MessageInfo? {
//        return work.getTimeId(myId, userId)
//    }

//    fun getConversation(myId: String, userId: String
//    ): Flow<List<MessageInfo>> {
//        return work.getConversation(myId, userId)
//    }

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