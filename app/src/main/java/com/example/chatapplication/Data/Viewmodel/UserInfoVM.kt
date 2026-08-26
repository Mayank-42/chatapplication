package com.example.chatapplication.Data.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.UserInfoReposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
import kotlin.jvm.java

class UserInfo(
    private val infovm: UserInfoReposatory,
    private val messageRepo: MessageRepo
): ViewModel() {

//    var userInfo:List<TakingUsernameResponse> = emptyList()
//        private set
var userInfo by mutableStateOf<List<TakingUsernameResponse>>(emptyList())
    private set
    var UserExsist by mutableStateOf<UserNameExistResponse?>(null)

    var companyUsers by mutableStateOf<List<TakingUsernameResponse>>(emptyList())
        private set
    fun getCompanyUsers() {
        viewModelScope.launch {
            try {
                println("COMPANY USERS: Calling API")
                val response = infovm.getCompanyUsers()
                println("COMPANY USERS: STATUS = ${response.code()}")
                println("COMPANY USERS: BODY = ${response.body()}")
                println("COMPANY USERS: ERROR = ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    companyUsers = response.body() ?: emptyList()
                    println("COMPANY USERS: LIST SIZE = ${companyUsers.size}")
                }
            } catch (e: Exception) {
                println("COMPANY USERS: EXCEPTION = ${e.message}")
            }
        }
    }
fun getinfo() {
    viewModelScope.launch {
        try {
            println("USER INFO: Calling API")

            val response = infovm.takingUserName()

            println("USER INFO: STATUS = ${response.code()}")
            println("USER INFO: BODY = ${response.body()}")
            println("USER INFO: ERROR = ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                userInfo = response.body() ?: emptyList()

                println("USER INFO: LIST SIZE = ${userInfo.size}")
            }

        } catch (e: Exception) {
            println("USER INFO: EXCEPTION = ${e.message}")
        }
    }
}
    fun isExsist(name:String){
        viewModelScope.launch{
            try {
                println("SEARCH: username = $name")

                val response = infovm.isExist(name)

                println("SEARCH: STATUS = ${response.code()}")
                println("SEARCH: BODY = ${response.body()}")
                println("SEARCH: ERROR = ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    UserExsist = response.body()
                }

            } catch (e: Exception) {
                println("SEARCH: EXCEPTION = ${e.message}")
            }
        }
    }
    fun openConversation(
        otherUserId: String,
        onConversationReady: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                println("CONVERSATION: Other user = $otherUserId")
                val response =
                    messageRepo.getOrCreateConversation(otherUserId)
                println("CONVERSATION: STATUS = ${response.code()}")
                println("CONVERSATION: BODY = ${response.body()}")
                println("CONVERSATION: ERROR = ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    val conversationId =
                        response.body()
                            ?.firstOrNull()
                            ?.conversation_id
                    if (conversationId != null) {
                        println("CONVERSATION: ID = $conversationId")
                        onConversationReady(conversationId)
                    } else {
                        println("CONVERSATION: No conversation ID returned")
                    }
                }
            } catch (e: Exception) {
                println("CONVERSATION: ERROR = ${e.message}")
            }
        }
    }
    fun uploadImg(id:String,bytes:ByteArray){
        viewModelScope.launch{
//        infovm.uploadIma(id,bytes)
            try {
                println("UPLOAD: ID = $id")
                println("UPLOAD: BYTES = ${bytes.size}")

                infovm.uploadIma(id, bytes)

                println("UPLOAD: SUCCESS")
                 getinfo()
            } catch (e: Exception) {
                println("UPLOAD: FAILED")
                println("UPLOAD: ERROR = ${e.message}")
                e.printStackTrace()
            }
        }
    }

}
class UserInfoFactory(
    private val repository: UserInfoReposatory,
    private val messageRepo: MessageRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass == UserInfo::class.java) {

            @Suppress("UNCHECKED_CAST")

            return UserInfo(
                repository,
                messageRepo
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}