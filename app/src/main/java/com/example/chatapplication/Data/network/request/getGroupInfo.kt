package com.example.chatapplication.Data.network.request

import com.google.gson.annotations.SerializedName

data class getGroupInfo(
   @SerializedName("p_conversation_id")
   var conversationId:String
){
}