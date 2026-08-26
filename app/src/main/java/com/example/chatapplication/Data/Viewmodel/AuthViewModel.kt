package com.example.chatapplication.Data.Viewmodel

import android.R.attr.data
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
import com.example.chatapplication.Data.network.clients.SupaBaseClient
import com.example.chatapplication.Data.network.response.loginResponse

import kotlinx.coroutines.launch
import retrofit2.Response

class loginVM( private val reposatory: AuthReposatory,
               private val tokenManager: TokenManager
): ViewModel(){

    var email:String=""
    var password:String=""

    var userLoggedIn by mutableStateOf(false)

    private fun getUserIdFromToken(accessToken: String): String {
        val payload = accessToken.split(".")[1]

        val decoded = android.util.Base64.decode(
            payload,
            android.util.Base64.URL_SAFE
        )

        val json = String(decoded)

        return org.json.JSONObject(json).getString("sub")
    }
    fun login(email:String,password:String,onResult: (Response<loginResponse>,String?) -> Unit){
    viewModelScope.launch{
        println("LOGIN: calling API")
        val response = reposatory.login(
            email,
            password
        )
//        println("LOGIN STATUS: ${response.code()}")
        var userId: String? = null
        if (response.isSuccessful) {
//            userLoggedIn = true
            val data=response.body()
            if(data!=null){
                tokenManager.saveTokens(
                    data.access_token,
                    data.refresh_token
                )
                SupaBaseClient.initialize(
                    tokenManager
                )
                val payload = data.access_token.split(".")[1]

                val decoded = android.util.Base64.decode(
                    payload,
                    android.util.Base64.URL_SAFE
                )

                val json = String(decoded)

                userId =
                    org.json.JSONObject(json).getString("sub")
            }
            }
        onResult(response,userId)



    }

    }
    fun sigUp(
        email: String,
        password: String,
        name: String,
        username: String,
        role:String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            val signupResponse = reposatory.signUp(
                email = email,
                password = password,
                name = name,
                username = username,
                role
            )

            if (!signupResponse.isSuccessful) {

                println("SIGNUP FAILED: ${signupResponse.code()}")
                println(
                    "ERROR: ${signupResponse.errorBody()?.string()}"
                )

                onResult(false)
                return@launch
            }

            // Signup successful.
            // Now login to obtain access + refresh tokens.
            val loginResponse = reposatory.login(
                email,
                password
            )

            if (!loginResponse.isSuccessful) {

                println("LOGIN AFTER SIGNUP FAILED: ${loginResponse.code()}")
                println(
                    "ERROR: ${loginResponse.errorBody()?.string()}"
                )

                onResult(false)
                return@launch
            }

            loginResponse.body()?.let {

                tokenManager.saveTokens(
                    it.access_token,
                    it.refresh_token
                )

                // IMPORTANT:
                // Supabase must be initialized BEFORE
                // MainActivity creates RealTimeRepo.
                SupaBaseClient.initialize(
                    tokenManager
                )
            }

            println("SIGNUP + LOGIN SUCCESS")

            // Only now tell MainActivity that authentication is ready.
            onResult(true)
        }
    }
    fun refreshSession() {
        viewModelScope.launch {
            val refreshToken = tokenManager.getRefreshToken() ?: return@launch

            val response = reposatory.refreshToken(refreshToken)

            if (response.isSuccessful) {
                response.body()?.let {
                    tokenManager.saveTokens(
                        it.access_token,
                        it.refresh_token
                    )

                }
            } else {
                println("REFRESH FAILED: ${response.code()}")
                println("ERROR: ${response.errorBody()?.string()}")
            }
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