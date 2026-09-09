package com.example.chatapplication


import android.app.Application
import androidx.room3.Room
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.local.dataBaseLocal


class dataBaseBuilder : Application() {

    val database: dataBaseLocal by lazy {
        Room.databaseBuilder(
            applicationContext,
            dataBaseLocal::class.java,
            "Chat_DataBase"
        ).fallbackToDestructiveMigration(true)
            .build()
    }
        val realtimeRepo: RealTimeRepo by lazy {
            RealTimeRepo()
        }

}