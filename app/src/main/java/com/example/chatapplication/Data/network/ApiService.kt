package com.example.chatapplication.Data.network

import com.example.chatapplication.Data.network.request.GetMessageRequest
import com.example.chatapplication.Data.network.request.MessageInfoRequest
import com.example.chatapplication.Data.network.request.RefreshTokenRequest
import com.example.chatapplication.Data.network.request.UserNameExistRequest
import com.example.chatapplication.Data.network.request.loginRequest
import com.example.chatapplication.Data.network.request.signUpRequest
import com.example.chatapplication.Data.network.response.MessageInfoResponse
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import com.example.chatapplication.Data.network.response.WholeMessageResponse
import com.example.chatapplication.Data.network.response.loginResponse
import com.example.chatapplication.Data.network.response.signUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("token?grant_type=password")
    suspend fun login(
        @Body request: loginRequest
    ): Response<loginResponse>

    @POST("token?grant_type=refresh_token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<loginResponse>
    @POST("signup")
        suspend fun signUp(
            @Body request: signUpRequest
        ):Response<signUpResponse>

    @GET("profile")
    suspend fun takingUserName(): Response<List<TakingUsernameResponse>>

    @POST("rpc/user_isexsist")
        suspend fun isExsist(
            @Body request: UserNameExistRequest
        ): Response<UserNameExistResponse>

        @POST("message")
        suspend fun storeMessage(
        @Body request: MessageInfoRequest
//        ): Response<List<MessageInfoResponse>>
    ): Response<Unit>

//    @GET("rpc/get_message")
//    suspend fun getingMessage():Response<List<WholeMessageResponse>>
      @POST("rpc/get_message")
      suspend fun getingMessage(
          @Body request: GetMessageRequest
      ): Response<List<WholeMessageResponse>>

}