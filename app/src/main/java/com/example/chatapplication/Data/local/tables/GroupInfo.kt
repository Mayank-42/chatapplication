package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class GroupInfo(
    @PrimaryKey
    var GroupId:String,
    var GropName:String,
    var bio:String,
    var createdBy: String,
    var createdAt: String
//    var memeber:List<String>
) {
}