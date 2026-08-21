package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class GroupInfo(
    @PrimaryKey(autoGenerate = true)
    var GroupId:Int=0,
    var GropName:String,
    var bio:String,
//    var memeber:List<String>
) {
}