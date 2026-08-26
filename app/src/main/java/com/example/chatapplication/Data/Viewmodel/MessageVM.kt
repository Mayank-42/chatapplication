package com.example.chatapplication.Data.Viewmodel


import android.R.id.message
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.UUID
import java .util.*
import kotlinx.serialization.json.jsonPrimitive
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Repo.convoInfoRepo
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.network.response.MessageInfoResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import com.example.chatapplication.Data.network.response.WholeMessageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MsgVM(
    private val gettingmsg: MessageRepo,
    private val realtimeRepo: RealTimeRepo,
    private val dbrepo: reposatory,
    private val tokenManager: TokenManager,
    private val convoRepo: convoInfoRepo
): ViewModel() {
    private var realtimeStarted = false
    private var activeConversationId: String? = null

    var localmsgList by mutableStateOf<List<WholeMessageResponse>>(emptyList())


    fun startRealtime() {

        if (realtimeStarted) {
            println("REALTIME: Already started")
            return
        }

        realtimeStarted = true

        viewModelScope.launch {
            val flow = realtimeRepo.messageInsertFlow()
            launch {
                flow.collectLatest { event ->

                    println("========== REALTIME MESSAGE DEBUG ==========")
                    println("REALTIME: NEW MESSAGE RECEIVED")
                    println("REALTIME: EVENT = $event")

                    val record = event.record

                    println("REALTIME: RECORD = $record")

                    val conversationIdFromEvent =
                        record["conversation_id"]
                            ?.jsonPrimitive
                            ?.content

                    println(
                        "REALTIME: CONVERSATION ID = $conversationIdFromEvent"
                    )

                    val messageInfo = MessageInfo(
                        id = record["id"]!!.jsonPrimitive.content,

                        conversationId =
                            conversationIdFromEvent!!,

                        sender_Id =
                            record["sender_id"]!!.jsonPrimitive.content,

                        reciver_Id =
                            record["receiver_id"]!!.jsonPrimitive.content,

                        message =
                            record["message"]!!.jsonPrimitive.content,

                        date =
                            record["message_timestamp"]!!.jsonPrimitive.content,

                        status = "SENT"
                    )

                    println("REALTIME: CONVERTED MESSAGE = $messageInfo")
                    dbrepo.realtimeInsert(messageInfo)
                    println("REALTIME: INSERTED INTO ROOM")

                    convoRepo.updateLastMessage(
                        conversationId = messageInfo.conversationId,
                        messageId = messageInfo.id,
                        message = messageInfo.message,
                        time = messageInfo.date
                    )

                    println("REALTIME: LAST MESSAGE UPDATE")

                    println("============================================")
                }
            }

            launch {
                realtimeRepo
                    .conversationInsertFlow()
                    .collectLatest { event ->

                        println("CONVERSATION REALTIME EVENT RECEIVED")
                        println("CONVERSATION EVENT = $event")
                        println("CONVERSATION RECORD = ${event.record}")
                    }
            }

            println("REALTIME: ABOUT TO SUBSCRIBE MESSAGE CHANNEL")
            realtimeRepo.subscribeMessages()
            println("REALTIME: ABOUT TO SUBSCRIBE CONVERSATION CHANNEL")
            realtimeRepo.subscribeConversations()
            println("REALTIME: ALL REALTIME SUBSCRIPTIONS STARTED")
        }
    }


    fun insertingLocaly(
        conversationId: String
    ) {
        viewModelScope.launch {
            val time = dbrepo.getTimeId(conversationId)
            gettingmsg.converting(
                conversationId = conversationId,
                time = time?.date ?: "",
                id = time?.id ?: ""
            )
        }
    }
    fun markMessagesAsRead(conversationId: String, myUserId: String) {
        viewModelScope.launch {
            dbrepo.markMessagesAsRead(conversationId = conversationId, myUserId = myUserId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun storeMsg(conversationId: String, message: String) {

        println("STORE MSG: FUNCTION CALLED")
        println("STORE MSG: conversationId = $conversationId")
        println("STORE MSG: message = $message")

        viewModelScope.launch {
            try {

                val messageId = UUID.randomUUID().toString()

                val myUserId = tokenManager.getUserId()

                if (myUserId.isNullOrBlank()) {
                    println("STORE MSG: USER ID IS EMPTY")
                    return@launch
                }
                // 1. Store message locally first
                dbrepo.insert(
                    MessageInfo(
                        id = messageId,
                        conversationId = conversationId,
                        sender_Id = myUserId,
                        reciver_Id = null,
                        message = message,
                        date = java.time.Instant.now().toString(),
                        status = "PENDING"
                    )
                )
                println("STORE MSG: LOCAL MESSAGE = PENDING")
                println("STORE MSG: CALLING API")

                val response = gettingmsg.putMessage(
                    id = messageId,
                    conversationId = conversationId,
                    msg = message
                )
                println("STORE MSG: STATUS = ${response.code()}")
                println("STORE MSG: BODY = ${response.body()}")
                println("STORE MSG: ERROR = ${response.errorBody()?.string()}")
                println("STORE MSG: MESSAGE ID = $messageId")

                if (response.isSuccessful) {
                    println("STORE MSG: API SUCCESS")
                    dbrepo.updateMessageStatus(messageId = messageId, status = "SENT")
                    println("MESSAGE STATUS: $messageId -> SENT")
                } else {
                    println("STORE MSG: API FAILED")
                }
            } catch (e: Exception) {
                println("STORE MSG: EXCEPTION = ${e.message}")
            }
        }
    }

    fun getUnreadCount(conversationId: String, myUserId: String): Flow<Int> {
        return dbrepo.getUnreadCount(conversationId = conversationId, myUserId = myUserId)
    }
    class MsgVMFactory(
        private val messageRepo: MessageRepo,
        private val realtimeRepo: RealTimeRepo,
        private val roomRepo: reposatory,
        private val tokenManager: TokenManager,
        private val convoRepo: convoInfoRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MsgVM::class.java)) {
                return MsgVM(
                    messageRepo,
                    realtimeRepo,
                    roomRepo,
                    tokenManager,
                    convoRepo
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}