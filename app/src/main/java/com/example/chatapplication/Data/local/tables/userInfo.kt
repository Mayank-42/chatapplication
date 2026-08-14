package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class userInfo(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var name:String,
    var userName:String,
) {
}