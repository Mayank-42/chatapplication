package com.example.chatapplication.Data.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Repo.convoInfoRepo
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.CinversationId
import com.example.chatapplication.Data.network.request.conversationIdRequest
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive

class convoVM(
    private var repo: convoInfoRepo,
    private var token: TokenManager,
    private var messageRepo: reposatory,
    private var msgRepo: MessageRepo,
    private var realTimeRepo: RealTimeRepo
): ViewModel(){

    var gettingConvoInfo= repo.getingConvoInfo

    private var conversationRealtimeStarted = false

    val onlineUsers = realTimeRepo.onlineUsers

    val privateConversations = repo.privateConversations

    val groupConversations = repo.groupConversations

//    fun insertConvoInfo(info: CinversationId){
//        viewModelScope.launch{
//            repo.insertConvoInfo(info)
//        }


fun startConversationRealtime() {
    if (conversationRealtimeStarted) {
    println("GROUP: REALTIME ALREADY STARTED")
    return
}

    conversationRealtimeStarted = true

    viewModelScope.launch {
        launch {
            realTimeRepo.conversationMemberInsertFlow().collect{ event ->
                    println("GROUP REALTIME EVENT RECEIVED")
                println("CONVERSATION MEMBER INSERT RECEIVED")
                    println("GROUP EVENT = ${event.record}")
                    val myUserId = token.getUserId()
                println("MY USER ID = $myUserId")
                    if (myUserId.isNullOrBlank()) {
                        println("GROUP: MY USER ID IS EMPTY")
                        return@collect
                    }
                    val eventUserId =
                        event.record["user_id"]
                            ?.jsonPrimitive
                            ?.content
                    val conversationId =
                        event.record["conversation_id"]
                            ?.jsonPrimitive
                            ?.content
                    println("GROUP EVENT USER = $eventUserId")
                    println("MY USER ID = $myUserId")
                    println("CONVERSATION ID = $conversationId")

                    if (eventUserId != myUserId) {
                        println("GROUP: NOT MY EVENT")
                        return@collect
                    }
                    if (conversationId.isNullOrBlank()) {
                        println("GROUP: CONVERSATION ID IS EMPTY")
                        return@collect
                    }
                    println("GROUP: FETCHING CONVERSATION")
                    repo.handleConversationMemberEvent(
                        event = event,
                        myUserId = myUserId
                    )
                    println("GROUP: HANDLE EVENT FINISHED")
                }
        }
        println("GROUP: ABOUT TO SUBSCRIBE")
        realTimeRepo.subscribeConversationMembers()
        println("GROUP: REALTIME SUBSCRIBED")
    }
}

    fun startPresence() {
        viewModelScope.launch {
            try {

                val userId = token.getUserId()

                if (userId.isNullOrBlank()) {
                    println("PRESENCE: USER ID IS EMPTY")
                    return@launch
                }

                println("PRESENCE: STARTING FOR USER = $userId")

                launch {
                    try {
                        realTimeRepo.observePresence()
                    } catch (e: Exception) {
                        println("PRESENCE OBSERVE ERROR = ${e.message}")
                        e.printStackTrace()
                    }
                }

                realTimeRepo.subscribePresence(userId)

                println("PRESENCE: STARTED")

            } catch (e: Exception) {
                println("PRESENCE START ERROR = ${e.message}")
                e.printStackTrace()
            }
        }
    }

    suspend fun getConversationById(
        conversationId: String
    ): com.example.chatapplication.Data.network.response.ConversationResponse? {
        return repo.getConversationById(conversationId)
            .body()
            ?.firstOrNull()
    }
fun syncConversations() {
    viewModelScope.launch {
        try {
            val userId =
                token.getUserId()
            if (userId.isNullOrBlank()) {
                println("CONVO: USER ID IS EMPTY")
                return@launch
            }
            repo.syncConversations(userId)
        } catch (e: Exception) {
            println(
                "CONVERSATION SYNC ERROR = ${e.message}"
            )
         }
      }
    }
    fun getUnreadCount(
        conversationId: String,
        myUserId: String
    ): Flow<Int> {
        return messageRepo.getUnreadCount(
            conversationId = conversationId,
            myUserId = myUserId
        )
    }
    fun stopConversationRealtime() {
        viewModelScope.launch {
            realTimeRepo.unsubscribeConversationMembers()
            conversationRealtimeStarted = false
            println("GROUP: REALTIME STOPPED")
        }
    }
    fun clearLocalConversations() {
        viewModelScope.launch {
            repo.clearLocalConversations()
            println("CONVO: LOCAL CONVERSATIONS CLEARED")
        }
    }
    suspend fun getOtherUserId(
        conversationId: String
    ): String? {
        return repo.getOtherUserId(conversationId)
    }
    private val conversationUser = MutableStateFlow<Map<String, String>>(emptyMap())

    val conversationUsers = conversationUser

    fun loadOtherUserIds(conversationIds: List<String>) {
        viewModelScope.launch {
            val result = mutableMapOf<String, String>()
            for (conversationId in conversationIds) {
                val userId = repo.getOtherUserId(conversationId)
                if (userId != null) { result[conversationId] = userId }
            }
            conversationUsers.value = result
        }
    }

    fun markMessageDelivered(messageId: String) {

        viewModelScope.launch {
            try {
                val response = msgRepo.markMessageDelivered(messageId)

                println("DELIVERED STATUS = ${response.code()}")

            } catch (e: Exception) {
                println("DELIVERED ERROR = ${e.message}")
            }
        }
    }
    fun markConversationSeen(conversationId: String) {

        viewModelScope.launch {

            try {
                val response = msgRepo.markConversationSeen(conversationId)

                println("SEEN STATUS = ${response.code()}")

            } catch (e: Exception) {
                println("SEEN ERROR = ${e.message}")
            }
        }
    }
}
class ConvoVMFactory(
    private val repo: convoInfoRepo,
    private val tokenManager: TokenManager,
    private var messageRepo: reposatory,
    private var msgRepo: MessageRepo,
    private var realTimeRepo: RealTimeRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(convoVM::class.java)) {

            return convoVM(
                repo,
                tokenManager,
                messageRepo,
                msgRepo,
                realTimeRepo
            ) as T
        }
        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}