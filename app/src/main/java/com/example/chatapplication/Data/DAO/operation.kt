package com.example.chatapplication.Data.DAO

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.local.tables.userLoginInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface operation {

    @Insert
    suspend fun insert(task: MessageInfo)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun realtimeInsert(message: MessageInfo)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun localInsert(msg: List<MessageInfo>)

    @Insert
    suspend fun logininsert(cred: userLoginInfo)

    @Insert
    suspend fun userinsert(cred: userInfo)


    @Delete
    suspend fun delete(task: MessageInfo)
    @Delete
    suspend fun loginDelete(cred: userLoginInfo)

    @Delete
    suspend fun userDelete(cred: userInfo)

    @Update
    suspend fun update(task: MessageInfo)

    @Query("""
    UPDATE MessageInfo
    SET status = :status
    WHERE id = :messageId
      AND (
          (:status = 'SENT' AND status = 'PENDING')
          OR
          (:status = 'DELIVERED' AND status IN ('PENDING', 'SENT'))
          OR
          (:status = 'READ' AND status IN ('PENDING', 'SENT', 'DELIVERED'))
          OR
          (:status = 'FAILED' AND status = 'PENDING')
      )
""")
    suspend fun updateMessageStatus(messageId: String, status: String)

    @Query("SELECT * FROM MessageInfo" )
    fun getAllTheValue() : Flow<List<MessageInfo>>


    @Query("""
    SELECT * FROM MessageInfo
    WHERE conversationId = :conversationId
    ORDER BY date ASC
""")
    fun getConversation(
        conversationId: String
    ): Flow<List<MessageInfo>>
//@Query("""
//    SELECT * FROM MessageInfo
//    WHERE (sender_Id = :myId AND reciver_Id = :userId)
//       OR (sender_Id = :userId AND reciver_Id = :myId)
//    ORDER BY date ASC
//""")
//fun getConversation(
//    myId: String,
//    userId: String
//): Flow<List<MessageInfo>>


    @Query("""
     SELECT * FROM MessageInfo
    WHERE conversationId = :conversationId
      AND status != 'PENDING'
    ORDER BY date DESC, id DESC
    LIMIT 1
""")
    suspend fun getTimeId(
        conversationId: String
    ): MessageInfo?
//@Query("""SELECT * FROM MessageInfo
//     WHERE (sender_Id = :myId AND reciver_Id = :userId)
//       OR (sender_Id = :userId AND reciver_Id = :myId)
//    ORDER BY date DESC, id DESC
//    LIMIT 1
//
//        """)
//suspend fun getTimeId(
//    myId: String,
//    userId: String
//): MessageInfo?

//    @Query("select*from MessageInfo where sender_id=")

    @Query("""
    SELECT COUNT(*)
    FROM MessageInfo
    WHERE conversationId = :conversationId
      AND sender_Id != :myUserId
      AND status != 'READ'
""")
    fun getUnreadCount(conversationId: String, myUserId: String): Flow<Int>

    @Query("""
    UPDATE MessageInfo
    SET status = 'READ'
    WHERE conversationId = :conversationId
      AND sender_Id != :myUserId
      AND status != 'READ'
""")
    suspend fun markMessagesAsRead(conversationId: String, myUserId: String)


}