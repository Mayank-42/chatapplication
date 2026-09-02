package com.example.chatapplication.Data.network.response

import com.google.gson.annotations.SerializedName

data class WholeMessageResponse(
    val id: String,
    @SerializedName("conversation_id")
    val conversationId: String,
    @SerializedName("sender_id") // to avoid conflict from the sql
    val senderId: String,
    @SerializedName("receiver_id")
    val receiverId: String,
    val message: String,
    @SerializedName("message_timestamp")
    val timeStamp: String,
    @SerializedName("status")
    val status: String
) {
}