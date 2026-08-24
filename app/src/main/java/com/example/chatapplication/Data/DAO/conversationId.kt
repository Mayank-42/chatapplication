package com.example.chatapplication.Data.DAO

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.chatapplication.Data.local.tables.CinversationId
import kotlinx.coroutines.flow.Flow

@Dao
interface conversationId {
    @Upsert
    suspend fun putingInfo(convoInfo: List<CinversationId>)

    @Query("select*from CinversationId")
    fun getAllConvoInfo(): Flow<List<CinversationId>>

}






