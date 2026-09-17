package com.example.chatapplication.Data.DI

import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.dataBaseLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object dbDependecies {

    @Provides
    @Singleton
    fun provideOperation(
        dbTask: dataBaseLocal
    ): operation {
        return dbTask.dataBaseCall()
    }
    @Provides
    @Singleton
    fun provideRepo(
        task: operation
    ): reposatory {
            return reposatory(task)
    }
}