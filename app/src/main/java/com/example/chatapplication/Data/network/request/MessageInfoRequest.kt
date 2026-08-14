package com.example.chatapplication.Data.network.request

import com.google.gson.annotations.SerializedName

data class MessageInfoRequest(
    @SerializedName("receiver_id")
    var reciver_id:String,
    var message:String,
) {
}