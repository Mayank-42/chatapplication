package com.example.chatapplication.Data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.local.tables.userLoginInfo


@Database(
    entities=[MessageInfo::class,userLoginInfo::class, userInfo::class],
    version = 8
)
 abstract class dataBaseLocal: RoomDatabase() {

    abstract fun dataBaseCall(): operation
}