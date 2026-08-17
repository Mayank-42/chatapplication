package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.local.operation
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.request.GetMessageRequest
import com.example.chatapplication.Data.network.request.MessageInfoRequest
import com.example.chatapplication.Data.network.response.MessageInfoResponse
import com.example.chatapplication.Data.network.response.WholeMessageResponse
import com.example.chatapplication.Data.network.response.loginResponse
import retrofit2.Response

class MessageRepo(
    private val getMessage: ApiService,
    private val localWork: operation
){
    suspend fun putMessage(reciver_id:String,msg:String):Response<Unit>{
        val request= MessageInfoRequest(reciver_id,msg)
        return getMessage.storeMessage(request)
    }
//    suspend fun getingmessage(): Response<List<WholeMessageResponse>>{
//        return getMessage.getingMessage()
//    }
    suspend fun converting(time:String,id:String){
    val request = GetMessageRequest(
        last_timestamp = if (time.isBlank()) null else time,
        last_id = if (id.isBlank()) null else id
    )
        val response=getMessage.getingMessage(request)
        var serverList= response.body()
        var localMsgList=serverList?.map{
            MessageInfo(
                id = it.id,
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
}