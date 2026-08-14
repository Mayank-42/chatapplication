package com.example.chatapplication.Data.network.request

data class signUpRequest(
    var email: String,
    var password:String,
    val data: Map<String,String>
) {
}