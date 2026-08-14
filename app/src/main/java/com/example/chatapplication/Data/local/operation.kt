package com.example.chatapplication.Data.local

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
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

    @Insert
    suspend fun localInsert(msg:List<MessageInfo>)

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

    @Query("SELECT * FROM MessageInfo" )
    fun getAllTheValue() : Flow<List<MessageInfo>>
@Query("""
    SELECT * FROM MessageInfo
    WHERE (sender_Id = :myId AND reciver_Id = :userId)
       OR (sender_Id = :userId AND reciver_Id = :myId)
    ORDER BY date ASC
""")
fun getConversation(
    myId: String,
    userId: String
): Flow<List<MessageInfo>>

//    @Query("select*from MessageInfo where sender_id=")



}