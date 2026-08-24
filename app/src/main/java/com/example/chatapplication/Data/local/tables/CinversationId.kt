package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class CinversationId(
    @PrimaryKey
    var conversationId:String,
    var type:String,
    var name:String?,
    var lastMessage:String?,
    var lastTime:String?,
    var Image:String?
) {
}