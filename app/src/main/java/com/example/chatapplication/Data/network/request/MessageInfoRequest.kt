package com.example.chatapplication.Data.network.request

import com.google.gson.annotations.SerializedName

data class MessageInfoRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("conversation_id")
    val conversationId: String,
    var message:String,
) {
}