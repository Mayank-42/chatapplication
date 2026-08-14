package com.example.chatapplication.Data.network.response

data class MessageInfoResponse(
    val id:String,
    val sender_id:String,
    val reciver_id:String,
    val message:String,
    val timeStamp:String
) {
}