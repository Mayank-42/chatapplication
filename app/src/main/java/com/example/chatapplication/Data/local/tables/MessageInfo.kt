package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class MessageInfo(
    @PrimaryKey
    var id:String,
    var conversationId: String,
    var sender_Id:String,
    var reciver_Id:String?,
    var message:String,
    var date:String,
//    var isSeen:Boolean
    var status: String = "PENDING"
) {
}