package com.example.chatapplication.Data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.chatapplication.Data.DAO.GroupOperation
import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.local.tables.GroupInfo
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.local.tables.groupMember
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.local.tables.userLoginInfo


@Database(
    entities=[MessageInfo::class,userLoginInfo::class, userInfo::class, GroupInfo::class, groupMember::class],
    version = 14
)
 abstract class dataBaseLocal: RoomDatabase() {

    abstract fun dataBaseCall(): operation
    abstract fun Groupcall(): GroupOperation
}