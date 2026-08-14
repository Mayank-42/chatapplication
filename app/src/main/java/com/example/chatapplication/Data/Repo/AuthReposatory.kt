package com.example.chatapplication.Data.Repo

import android.R.attr.data
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.request.loginRequest
import com.example.chatapplication.Data.network.request.signUpRequest
import com.example.chatapplication.Data.network.response.loginResponse
import com.example.chatapplication.Data.network.response.signUpResponse
import retrofit2.Response
class AuthReposatory(private val auth:ApiService)  {

    suspend fun login(email:String,password:String): Response<loginResponse>{
        val request= loginRequest(email=email,password=password)
        return auth.login(request)
    }
    suspend fun signUp(email:String,password:String,name:String,username:String):Response<signUpResponse>{
        val PutingUserInfo= signUpRequest(email=email,password= password,data=mapOf("name" to name,"username" to username))
        return auth.signUp(PutingUserInfo)
    }
}