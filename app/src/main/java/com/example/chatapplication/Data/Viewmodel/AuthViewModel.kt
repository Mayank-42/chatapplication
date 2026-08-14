package com.example.chatapplication.Data.Viewmodel

import android.R.attr.password
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable


import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.network.response.loginResponse

import kotlinx.coroutines.launch
import retrofit2.Response

class loginVM( private val reposatory: AuthReposatory,
               private val tokenManager: TokenManager
): ViewModel(){

    var email:String=""
    var password:String=""

    var userLoggedIn by mutableStateOf(false)


    fun login(email:String,password:String,onResult: (Response<loginResponse>) -> Unit){
    viewModelScope.launch{
        println("LOGIN: calling API")
        val response = reposatory.login(
            email,
            password
        )
//        println("LOGIN STATUS: ${response.code()}")

        if (response.isSuccessful) {
//            userLoggedIn = true
            val data=response.body()
            if(data!=null){
                tokenManager.saveTokens(
                    data.access_token,
                    data.refresh_token
                )
            }

//        val data = response.body()

//        println("LOGIN SUCCESS")
//        println("ACCESS TOKEN: ${data?.access_token}")
//        println("REFRESH TOKEN: ${data?.refresh_token}")

    } else {

//        println("LOGIN FAILED")
//        println("ERROR: ${response.errorBody()?.string()}")
    }
        onResult(response)
    }

    }
    fun sigUp(email:String,password: String,name:String,username:String){
        viewModelScope.launch{
            val givingInfo=reposatory.signUp(email=email,password=password,name=name,
                username=username)
            println("STATUS: ${givingInfo.code()}")
            println("BODY: ${givingInfo.body()}")
            println("ERROR: ${givingInfo.errorBody()?.string()}")
        }
    }
}
class AuthViewModelFactory(
    private val repository: AuthReposatory,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(loginVM::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return loginVM(repository,tokenManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}