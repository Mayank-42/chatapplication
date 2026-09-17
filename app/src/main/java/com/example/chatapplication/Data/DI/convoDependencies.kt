package com.example.chatapplication.Data.DI

import android.content.Context
import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Repo.convoInfoRepo
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.dataBaseLocal
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.clients.retroFitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object convoDependencies {

    @Provides
    @Singleton
    fun provideApiService(
        task: retroFitClient
    ): ApiService{
        return task.apiService
    }

    @Provides
    @Singleton
    fun provideTokenManger(
        @ApplicationContext context: Context
    ): TokenManager{
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideConversationId(
        task: conversationId,
        apiTask: ApiService
    ): convoInfoRepo {
        return convoInfoRepo(task,apiTask)
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
    fun provideReposatory(
        task: operation
    ): reposatory {
        return reposatory(task)
    }

    @Provides
    @Singleton
    fun provideMsgRepo(
        task: operation,
        apiTask: ApiService
    ): MessageRepo{
        return MessageRepo(apiTask,task)
    }

    @Provides
    @Singleton
    fun provideRealTimeRepo(): RealTimeRepo {
        return RealTimeRepo()
    }

}