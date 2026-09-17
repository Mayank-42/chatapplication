package com.example.chatapplication.Data.DI

import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.Repo.MessageRepo
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
object Dependecies {

    @Provides
    @Singleton
    fun provideApiService(
        task: retroFitClient
    ): ApiService{
        return task.apiService
    }
    @Provides
    @Singleton
    fun provideOperation(
        task: dataBaseLocal
    ): operation {
        return task.dataBaseCall()
    }

    @Provides
    @Singleton
    fun provideMsgRepo(
        task: operation,
        apiTask: ApiService
    ): MessageRepo{
        return MessageRepo(apiTask,task)
    }

}