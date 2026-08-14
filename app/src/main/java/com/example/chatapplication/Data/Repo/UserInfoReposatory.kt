package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.request.UserNameExistRequest
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import retrofit2.Response

class UserInfoReposatory(
    private val userInfoRepo: ApiService

) {
    suspend fun takingUserName():Response<List<TakingUsernameResponse>>{
        return userInfoRepo.takingUserName()
    }
    suspend fun isExist(name:String): Response<UserNameExistResponse>{
        val request= UserNameExistRequest(username_input=name)
        return userInfoRepo.isExsist(request)
    }
}