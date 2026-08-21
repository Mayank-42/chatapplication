package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity

@Entity(

    primaryKeys = ["GroupId", "GroupMemberId"]
)
data class groupMember(
    var GroupId:String,
    var GroupMemberId:String
) {
}