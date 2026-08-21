package com.example.chatapplication.Data.network.request

class GetMessageRequest(
    val conversation_id: String,
    val last_timestamp: String?,
    val last_id: String?
) {
}