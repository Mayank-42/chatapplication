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

    @Query("""
        SELECT * FROM CinversationId
        WHERE conversationId = :conversationId
        LIMIT 1
    """)
    suspend fun getConversation(
        conversationId: String
    ): CinversationId?

    @Query("""
        UPDATE CinversationId
        SET lastMessage = :message,
            lastTime = :time,
            last_message_id = :messageId
        WHERE conversationId = :conversationId
    """)
    suspend fun updateLastMessage(
        conversationId: String,
        messageId: String,
        message: String,
        time: String
    )

}






