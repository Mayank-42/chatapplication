package com.example.chatapplication.Data.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.UserInfoReposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
import kotlin.jvm.java

class UserInfo(
    private val infovm: UserInfoReposatory
): ViewModel() {

//    var userInfo:List<TakingUsernameResponse> = emptyList()
//        private set
var userInfo by mutableStateOf<List<TakingUsernameResponse>>(emptyList())
    private set
    var UserExsist by mutableStateOf<UserNameExistResponse?>(null)

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
    private val repository: UserInfoReposatory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass == UserInfo::class.java) {

            @Suppress("UNCHECKED_CAST")
            return UserInfo(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}