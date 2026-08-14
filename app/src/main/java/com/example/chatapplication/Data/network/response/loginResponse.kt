package com.example.chatapplication.Data.network.response

data class loginResponse(
    val access_token: String,
    val refresh_token: String,
    val expires_in: Int,
    val token_type: String
) {
}