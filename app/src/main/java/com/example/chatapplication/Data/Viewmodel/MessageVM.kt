package com.example.chatapplication.Data.Viewmodel

import android.R.id.message
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.network.response.MessageInfoResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import com.example.chatapplication.Data.network.response.WholeMessageResponse
import kotlinx.coroutines.launch

class MsgVM(private val gettingmsg: MessageRepo): ViewModel() {

    var msgList by mutableStateOf<List<MessageInfoResponse>>(emptyList())
    var localmsgList by mutableStateOf<List<WholeMessageResponse>>(emptyList())

//    fun storeMsg(request_id:String,message:String){
//        viewModelScope.launch{
//          var response=  gettingmsg.putMessage(reciver_id=request_id,msg=message)
//            if(response.isSuccessful){
//                msgList = response.body() ?:emptyList()
//            }
//        }
//    }

    fun insertingLocaly(){
        viewModelScope.launch{
            gettingmsg.converting()
        }
    }
fun storeMsg(receiverId: String, message: String) {

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
            gettingmsg.converting()
        }
            println("STORE MSG: STATUS = ${response.code()}")
            println("STORE MSG: BODY = ${response.body()}")
            println("STORE MSG: ERROR = ${response.errorBody()?.string()}")

        } catch (e: Exception) {
            println("STORE MSG: EXCEPTION = ${e.message}")
        }
    }
}
    fun getingmsg(){
        viewModelScope.launch{
            try {

            val response= gettingmsg.getingmessage()

                println("SEARCH: STATUS = ${response.code()}")
                println("SEARCH: BODY = ${response.body()}")
                println("SEARCH: ERROR = ${response.errorBody()?.string()}")

            if(response.isSuccessful){
                localmsgList = response.body() ?: emptyList()
            }
            } catch (e: Exception) {
                println("SEARCH: EXCEPTION = ${e.message}")
            }
        }
    }
}
class MsgVMFactory(
    private val messageRepo: MessageRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MsgVM::class.java)) {
            return MsgVM(messageRepo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}