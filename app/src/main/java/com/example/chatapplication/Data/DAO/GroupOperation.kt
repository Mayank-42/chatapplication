package com.example.chatapplication.Data.DAO

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.chatapplication.Data.local.tables.GroupInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupOperation {

    @Insert
    suspend fun  GroupInfoInsert(Info: GroupInfo)

    @Query("select*from GroupInfo")
    fun getAllInfo(): Flow<List<GroupInfo>>

}