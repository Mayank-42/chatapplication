package com.example.chatapplication.Data.network.response

data class TakingUsernameResponse(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val photo_url: String?,
    val created_at: String?
) {

}