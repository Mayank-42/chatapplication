package com.example.chatapplication.Data.network.response

data class MessageInfoResponse(
    val id: String,
    val sender_id: String,
    val receiver_id: String?,
    val message: String,
    val message_timestamp: String,
    val conversation_id: String,
    val status: String
) {
}