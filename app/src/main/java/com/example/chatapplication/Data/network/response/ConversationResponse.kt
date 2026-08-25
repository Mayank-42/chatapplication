package com.example.chatapplication.Data.network.response

data class ConversationResponse(
    val conversation_id: String,
    var type:String,
    var name:String?,
    var lastMessage:String?,
    var lastTime:String?,
    var Image:String?,
    var last_message_id: String?,
    var unread_count: Int
)
