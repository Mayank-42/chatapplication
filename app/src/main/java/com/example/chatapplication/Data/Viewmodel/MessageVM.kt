package com.example.chatapplication.Data.Viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Repo.convoInfoRepo
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.network.response.WholeMessageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.time.Instant
import java.util.UUID


class MsgVM(
    private val gettingmsg: MessageRepo,
    private val realtimeRepo: RealTimeRepo,
    private val dbrepo: reposatory,
    private val tokenManager: TokenManager,
    private val convoRepo: convoInfoRepo
) : ViewModel() {

    private var realtimeStarted = false

    private var activeConversationId: String? = null

    var localmsgList by mutableStateOf<List<WholeMessageResponse>>(emptyList())


    // ============================================================
    // REALTIME
    // ============================================================

    fun startRealtime() {

        if (realtimeStarted) {
            println("REALTIME: ALREADY STARTED")
            return
        }

        realtimeStarted = true

        viewModelScope.launch {
            // ----------------------------------------------------
            // MESSAGE INSERT REALTIME
            // ----------------------------------------------------
            launch {
                realtimeRepo
                    .messageInsertFlow()
                    .collectLatest { event ->
                        println("========== REALTIME MESSAGE ==========")
                        println("REALTIME: NEW MESSAGE RECEIVED")
                        println("REALTIME: EVENT = $event")
                        val record = event.record
                        println("REALTIME: RECORD = $record")
                        // -------------------------------
                        // Get current logged-in user
                        // -------------------------------
                        val myUserId = tokenManager.getUserId()

                        if (myUserId.isNullOrBlank()) {
                            println("REALTIME: MY USER ID IS EMPTY")
                            return@collectLatest
                        }
                        // -------------------------------
                        // Read message fields
                        // -------------------------------
                        val messageId =
                            record["id"]
                                ?.jsonPrimitive
                                ?.content

                        val conversationId =
                            record["conversation_id"]
                                ?.jsonPrimitive
                                ?.content

                        val senderId =
                            record["sender_id"]
                                ?.jsonPrimitive
                                ?.content

                        val receiverId =
                            record["receiver_id"]
                                ?.jsonPrimitive
                                ?.contentOrNull

                        val message =
                            record["message"]
                                ?.jsonPrimitive
                                ?.content

                        val messageTimestamp =
                            record["message_timestamp"]
                                ?.jsonPrimitive
                                ?.content

                        // -------------------------------
                        // Validate required fields
                        // -------------------------------
                        if (
                            messageId == null ||
                            conversationId == null ||
                            senderId == null ||
                            message == null ||
                            messageTimestamp == null
                        ) {
                            println("REALTIME: MESSAGE RECORD IS INVALID")
                            println("REALTIME: id = $messageId")
                            println("REALTIME: conversationId = $conversationId")
                            println("REALTIME: senderId = $senderId")
                            println("REALTIME: message = $message")
                            println("REALTIME: timestamp = $messageTimestamp")
                            return@collectLatest
                        }
                        // -------------------------------
                        // Determine local status
                        // -------------------------------
                        val status =
                            if (senderId == myUserId) {

                                /*
                                 * This is our own message.
                                 *
                                 * The local message should already
                                 * exist as SENT after the API succeeds.
                                 */
                                "SENT"

                            } else {

                                /*
                                 * This message came from another user.
                                 *
                                 * It has reached our device through
                                 * realtime, therefore it is DELIVERED.
                                 */
                                "DELIVERED"
                            }


                        println("REALTIME: MY USER ID = $myUserId")
                        println("REALTIME: SENDER ID = $senderId")
                        println("REALTIME: RECEIVER ID = $receiverId")
                        println("REALTIME: STATUS = $status")
                        // -------------------------------
                        // Create Room object
                        // -------------------------------
                        val messageInfo = MessageInfo(
                            id = messageId,
                            conversationId = conversationId,
                            sender_Id = senderId,
                            reciver_Id = receiverId,
                            message = message,
                            date = messageTimestamp,
                            status = status
                        )
                        println("REALTIME: CONVERTED MESSAGE = $messageInfo")
                        // ------------------------------------------------
                        // IMPORTANT
                        // ------------------------------------------------
                        //
                        // If this is OUR message, it probably already
                        // exists locally.
                        //
                        // If this is OTHER user's message, insert it.
                        //
                        // Your DAO/repository should handle duplicate IDs.
                        // ------------------------------------------------

                        if (senderId == myUserId) {
//                            dbrepo.updateMessageStatus(messageId = messageId, status = "SENT")
                            println("REALTIME: OUR MESSAGE -> LOCAL MESSAGE ALREADY EXISTS")

                        } else {
                            dbrepo.realtimeInsert(messageInfo)
                            println("REALTIME: OTHER USER MESSAGE -> DELIVERED")

                            try {
                                val response = gettingmsg.markMessageDelivered(messageId)

                                println("DELIVERED: SERVER STATUS = ${response.code()}")

                                println(
                                    "DELIVERED: ERROR = ${
                                        response.errorBody()?.string()
                                    }"
                                )
                                if (response.isSuccessful) {
                                    println("DELIVERED: SERVER UPDATED = $messageId")

                                } else {
                                    println("DELIVERED: SERVER UPDATE FAILED")
                                }

                            } catch (e: Exception) {
                                println("DELIVERED: ERROR = ${e.message}")
                                e.printStackTrace()
                            }
                        }
                        // -------------------------------
                        // Update conversation preview
                        // -------------------------------
                        convoRepo.updateLastMessage(
                            conversationId = conversationId,
                            messageId = messageId,
                            message = message,
                            time = messageTimestamp
                        )
                        println("REALTIME: LAST MESSAGE UPDATED")
                        println("======================================")
                    }
            }
            launch {

                realtimeRepo
                    .messageUpdateFlow()
                    .collect{ event ->

                        println("========== REALTIME MESSAGE UPDATE ==========")
                        println("REALTIME: MESSAGE UPDATE RECEIVED")
                        println("REALTIME: UPDATE EVENT = $event")

                        val record = event.record

                        println("REALTIME: UPDATED RECORD = $record")

                        val messageId =
                            record["id"]
                                ?.jsonPrimitive
                                ?.content

                        val status =
                            record["status"]
                                ?.jsonPrimitive
                                ?.content

                        println("REALTIME UPDATE: MESSAGE ID = $messageId")
                        println("REALTIME UPDATE: STATUS = $status")

                        if (
                            messageId.isNullOrBlank()
                        ) {
                            println("REALTIME UPDATE: MESSAGE ID IS EMPTY")
                            return@collect
                        }

                        if (
                            status.isNullOrBlank()
                        ) {
                            println("REALTIME UPDATE: STATUS IS EMPTY")
                            return@collect
                        }

                        when (status.uppercase()) {

                            "DELIVERED",
                            "SEEN",
                            "SENT" -> {

                                println(
                                    "REALTIME UPDATE: ROOM STATUS -> $status"
                                )

                                dbrepo.updateMessageStatus(
                                    messageId = messageId,
                                    status = status.uppercase()
                                )

                                println(
                                    "REALTIME UPDATE: ROOM UPDATED SUCCESSFULLY"
                                )
                            }

                            else -> {

                                println(
                                    "REALTIME UPDATE: UNKNOWN STATUS = $status"
                                )
                            }
                        }

                        println("============================================")
                    }
            }

            // ----------------------------------------------------
            // CONVERSATION REALTIME
            // ----------------------------------------------------

            launch {

                realtimeRepo
                    .conversationInsertFlow()
                    .collect { event ->
                        println("CONVERSATION REALTIME EVENT RECEIVED")
                        println("CONVERSATION EVENT = $event")
                        println("CONVERSATION RECORD = ${event.record}")
                    }
            }
            // ----------------------------------------------------
            // SUBSCRIBE
            // ----------------------------------------------------
            println("REALTIME: ABOUT TO SUBSCRIBE MESSAGE CHANNEL")
            realtimeRepo.subscribeMessages()
            println("REALTIME: ABOUT TO SUBSCRIBE CONVERSATION CHANNEL")
            realtimeRepo.subscribeConversations()
            println("REALTIME: ALL REALTIME SUBSCRIPTIONS STARTED")
        }
    }
    // ============================================================
    // LOAD MESSAGES FROM SERVER
    // ============================================================
    fun insertingLocaly(
        conversationId: String
    ) {

        viewModelScope.launch {

            try {
                val time = dbrepo.getTimeId(conversationId)
                gettingmsg.converting(
                    conversationId = conversationId,
                    time = time?.date ?: "",
                    id = time?.id ?: ""
                )
            } catch (e: Exception) {
                println("LOAD MESSAGE ERROR = ${e.message}")
                e.printStackTrace()
            }
        }
    }

    suspend fun syncAllConversations(
        conversationIds: List<String>
    ) {

        for (conversationId in conversationIds) {

            try {

                val time =
                    dbrepo.getTimeId(conversationId)

                println(
                    "STARTUP MESSAGE SYNC: " +
                            "conversation=$conversationId " +
                            "lastTime=${time?.date} " +
                            "lastId=${time?.id}"
                )

                gettingmsg.converting(
                    conversationId = conversationId,
                    time = time?.date ?: "",
                    id = time?.id ?: ""
                )

                println(
                    "STARTUP MESSAGE SYNC COMPLETE: " +
                            "conversation=$conversationId"
                )

            } catch (e: Exception) {

                println(
                    "STARTUP MESSAGE SYNC ERROR: " +
                            "conversation=$conversationId " +
                            "error=${e.message}"
                )

                e.printStackTrace()
            }
        }
    }
    // ============================================================
    // MARK LOCAL MESSAGES AS READ
    // ============================================================
    fun markMessagesAsRead(conversationId: String, myUserId: String) {
        viewModelScope.launch {
            try {

                // 1. Update local Room
                dbrepo.markMessagesAsRead(
                    conversationId = conversationId,
                    myUserId = myUserId
                )

                println("SEEN: ROOM UPDATED")

                // 2. Update server
                val response = gettingmsg.markConversationSeen(
                    conversationId = conversationId
                )

                println("SEEN: SERVER STATUS = ${response.code()}")
                println("SEEN: SERVER ERROR = ${response.errorBody()?.string()}")

            } catch (e: Exception) {
                println("SEEN ERROR = ${e.message}")
            }
        }
    }
    // ============================================================
    // SEND MESSAGE
    // ============================================================
    @RequiresApi(Build.VERSION_CODES.O)
    fun storeMsg(conversationId: String,message: String
    ) {
        println("STORE MSG: FUNCTION CALLED")
        println("STORE MSG: conversationId = $conversationId")
        println("STORE MSG: message = $message")
        viewModelScope.launch {
            try {
                // ------------------------------------------------
                // Generate message ID
                // ------------------------------------------------
                val messageId = UUID.randomUUID().toString()
                // ------------------------------------------------
                // Get logged-in user
                // ------------------------------------------------
                val myUserId = tokenManager.getUserId()
                if (myUserId.isNullOrBlank()) {
                    println("STORE MSG: USER ID IS EMPTY")
                    return@launch
                }
                // ------------------------------------------------
                // Current timestamp
                // ------------------------------------------------
                val timestamp = Instant.now().toString()
                // ------------------------------------------------
                // STEP 1
                // Save locally as PENDING
                // ------------------------------------------------
                dbrepo.insert(
                    MessageInfo(
                        id = messageId,
                        conversationId = conversationId,
                        sender_Id = myUserId,
                        reciver_Id = null,
                        message = message,
                        date = timestamp,
                        status = "PENDING"
                    )
                )
                println("STORE MSG: LOCAL MESSAGE = PENDING")
                // ------------------------------------------------
                // STEP 2
                // Send to backend
                // ------------------------------------------------
                println("STORE MSG: CALLING API")

                val response =
                    gettingmsg.putMessage(
                        id = messageId,
                        conversationId = conversationId,
                        msg = message
                    )
                println("STORE MSG: HTTP STATUS = ${response.code()}")
                println("STORE MSG: BODY = ${response.body()}")
                println(
                    "STORE MSG: ERROR = ${
                        response.errorBody()?.string()
                    }"
                )

                println("STORE MSG: MESSAGE ID = $messageId")
                // ------------------------------------------------
                // STEP 3
                // API SUCCESS
                // ------------------------------------------------

                if (response.isSuccessful) {

                    println("STORE MSG: API SUCCESS")

                    dbrepo.updateMessageStatus(messageId = messageId, status = "SENT")
                    println("MESSAGE STATUS: $messageId -> SENT")
                } else {
                    // ------------------------------------------------
                    // API FAILED
                    // ------------------------------------------------
                    println("STORE MSG: API FAILED")
                    dbrepo.updateMessageStatus(messageId = messageId, status = "FAILED")
                    println(
                        "MESSAGE STATUS: $messageId -> FAILED"
                    )
                }

            } catch (e: Exception) {

                println(
                    "STORE MSG: EXCEPTION = ${e.message}"
                )

                e.printStackTrace()
            }
        }
    }


    // ============================================================
    // STOP REALTIME
    // ============================================================

    fun stopRealtime() {
        viewModelScope.launch {
            try {
                realtimeRepo.unsubscribeMessages()
                realtimeRepo.unsubscribeConversations()
                realtimeStarted = false
                println(
                    "REALTIME: MESSAGE/CONVERSATION STOPPED"
                )

            } catch (e: Exception) {
                println(
                    "REALTIME STOP ERROR = ${e.message}"
                )
                e.printStackTrace()
            }
        }
    }


    // ============================================================
    // UNREAD COUNT
    // ============================================================

    fun getUnreadCount(
        conversationId: String,
        myUserId: String
    ): Flow<Int> {

        return dbrepo.getUnreadCount(
            conversationId = conversationId,
            myUserId = myUserId
        )
    }
    // ============================================================
    // VIEWMODEL FACTORY
    // ============================================================

    class MsgVMFactory(

        private val messageRepo: MessageRepo,

        private val realtimeRepo: RealTimeRepo,

        private val roomRepo: reposatory,

        private val tokenManager: TokenManager,

        private val convoRepo: convoInfoRepo

    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {

            if (
                modelClass.isAssignableFrom(
                    MsgVM::class.java
                )
            ) {

                return MsgVM(

                    messageRepo,

                    realtimeRepo,

                    roomRepo,

                    tokenManager,

                    convoRepo

                ) as T
            }

            throw IllegalArgumentException(
                "Unknown ViewModel class"
            )
        }
    }
}