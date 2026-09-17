package com.example.chatapplication.Data.DI

import com.example.chatapplication.Data.DAO.operation
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.UserInfoReposatory
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.local.dataBaseLocal
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.clients.retroFitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object userDependecies {

    @Provides
    @Singleton
    fun provideApiService(
        retroTask: retroFitClient
    ): ApiService{
        return retroTask.apiService
    }

    @Provides
    @Singleton
    fun provideUserInfoRepo(
        task: ApiService,
        retroSupa: SupabaseClient
    ): UserInfoReposatory{
        return UserInfoReposatory(task,retroSupa)
    }
//    @Provides
//    @Singleton
//    fun provideOperation(
//        locaDB: dataBaseLocal
//    ): operation {
//        return locaDB.dataBaseCall()
//    }
    @Provides
    @Singleton
    fun provideUserRepo(
        userRepo:UserInfoReposatory,
        msgTask: MessageRepo
    ):UserInfo{
        return UserInfo(userRepo,msgTask)
    }
}