package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class GroupInfo(
    @PrimaryKey
    var GroupId:String,
    var GropName:String,
    var bio:String,
    var last_message:String,
    var last_Message_Time:String
//    var memeber:List<String>
) {
}