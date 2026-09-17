package com.example.chatapplication.Data.DI

import com.example.chatapplication.Data.DAO.GroupOperation
import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.Repo.GroupRepo
import com.example.chatapplication.Data.local.dataBaseLocal
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.clients.retroFitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object groupDependencies {

   @Provides
   @Singleton
   fun provideGroupOperation(
       gDBtask: dataBaseLocal
   ): GroupOperation {
       return gDBtask.Groupcall()
   }
    @Provides
    @Singleton
    fun provideApiService(
        retroTask: retroFitClient
    ): ApiService{
        return retroTask.apiService
    }
    @Provides
    @Singleton
    fun provideConversationId(
        convoTask: dataBaseLocal
    ): conversationId {
        return convoTask.ConvoInfo()
    }
    @Provides
    @Singleton
    fun provideGroupRepo(
        workTask: GroupOperation,
        apiTask: ApiService,
        convoTask: conversationId
    ): GroupRepo {
        return GroupRepo(workTask,apiTask,convoTask)
    }

}