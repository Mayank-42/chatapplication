package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.chatapplication.Data.DAO.conversationId

@Entity
data class GroupInfo(
    @PrimaryKey
    var GroupId:String,
    var GropName:String,
    var bio:String,
    var createdBy: String,
    var createdAt: String,
    var conversationId:String
//    var memeber:List<String>
) {
}