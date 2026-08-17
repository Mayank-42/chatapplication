package com.example.chatapplication.Data.Viewmodel


import kotlinx.serialization.json.jsonPrimitive
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.network.response.MessageInfoResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import com.example.chatapplication.Data.network.response.WholeMessageResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MsgVM(
    private val gettingmsg: MessageRepo,
    private val realtimeRepo: RealTimeRepo,
    private val dbrepo: reposatory,
    private val tokenManager: TokenManager
): ViewModel() {
    private var realtimeStarted = false

    var localmsgList by mutableStateOf<List<WholeMessageResponse>>(emptyList())


    fun startRealtime() { // every new inserted row will come to local db

        if (realtimeStarted) {
            println("REALTIME: Already started")
            return
        }

        realtimeStarted = true

        viewModelScope.launch {
            val currentUserId = tokenManager.getUserId() ?: ""

            val flow =
                realtimeRepo.messageInsertFlow() //geting all the event flow in variable flow so ya

            launch {

                flow.collectLatest { event -> //tells that if any new event happaned execute this block

                    println("REALTIME: NEW MESSAGE RECEIVED")

                    val record = event.record
                    val senderId = record["sender_id"]!!.jsonPrimitive.content
                    val receiverId = record["receiver_id"]!!.jsonPrimitive.content

                    if (
                        senderId != currentUserId &&
                        receiverId != currentUserId
                    ) {
                        println("REALTIME: IGNORING UNRELATED MESSAGE")
                        return@collectLatest  //if senderId(user) is not involved then ignore the event
                    }

                    val messageInfo = MessageInfo(
                        id = record["id"]!!.jsonPrimitive.content,
                        sender_Id = record["sender_id"]!!.jsonPrimitive.content,
                        reciver_Id = record["receiver_id"]!!.jsonPrimitive.content,
                        message = record["message"]!!.jsonPrimitive.content,
                        date = record["message_timestamp"]!!.jsonPrimitive.content
                    )

                    println("ROOM MESSAGE = $messageInfo")
                    dbrepo.insert(messageInfo)

                }
            }
//         realtimeRepo.UnSubscriber()

            realtimeRepo.subscribe()
        }
    }


    fun insertingLocaly(
        myId: String,
        userId: String
    ) { //whole postgress db comes to localDB but we have to call
        viewModelScope.launch {
            var time = dbrepo.getTimeId(myId, userId)
            gettingmsg.converting(time?.date ?: "", time?.id ?: "")
        }
    }

    fun storeMsg(receiverId: String, message: String) {  //send the message

        println("STORE MSG: FUNCTION CALLED")
        println("STORE MSG: receiverId = $receiverId")
        println("STORE MSG: message = $message")

        viewModelScope.launch {
            try {

                println("STORE MSG: CALLING API")

                val response = gettingmsg.putMessage(
                    reciver_id = receiverId,
                    msg = message
                )

                if (response.isSuccessful) {
                    println("STORE MSG: API SUCCESS")
//            gettingmsg.converting()
                }
                println("STORE MSG: STATUS = ${response.code()}")
                println("STORE MSG: BODY = ${response.body()}")
                println("STORE MSG: ERROR = ${response.errorBody()?.string()}")

            } catch (e: Exception) {
                println("STORE MSG: EXCEPTION = ${e.message}")
            }
        }
    }

    //    fun getingmsg(){  //taking the response from post respose (Not use full)
//        viewModelScope.launch{
//            try {
//
//            val response= gettingmsg.getingmessage()
//
//                println("SEARCH: STATUS = ${response.code()}")
//                println("SEARCH: BODY = ${response.body()}")
//                println("SEARCH: ERROR = ${response.errorBody()?.string()}")
//
//            if(response.isSuccessful){
//                localmsgList = response.body() ?: emptyList()
//            }
//            } catch (e: Exception) {
//                println("SEARCH: EXCEPTION = ${e.message}")
//            }
//        }
//    }
//}
    class MsgVMFactory(
        private val messageRepo: MessageRepo,
        private val realtimeRepo: RealTimeRepo,
        private val roomRepo: reposatory,
        private val tokenManager: TokenManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(MsgVM::class.java)) {
                return MsgVM(
                    messageRepo,
                    realtimeRepo,
                    roomRepo,
                    tokenManager
                ) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}