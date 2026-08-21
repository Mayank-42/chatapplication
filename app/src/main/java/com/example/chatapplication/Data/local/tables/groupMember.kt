package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity

@Entity(

    primaryKeys = ["groupId", "groupMemberId"]
)
data class groupMember(
    var GroupId:String,
    var GroupMemberId:String
) {
}