package com.example.chatapplication.Data.network.request

import com.google.gson.annotations.SerializedName

data class deleteGroupRequest(
    @SerializedName("p_conversation_id")
    val conversationId:String
)
