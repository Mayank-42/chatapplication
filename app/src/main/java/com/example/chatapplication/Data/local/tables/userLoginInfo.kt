package com.example.chatapplication.Data.local.tables

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class userLoginInfo(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    val Email:String,
    var password:String
){

}
