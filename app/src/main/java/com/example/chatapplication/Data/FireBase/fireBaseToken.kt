package com.example.chatapplication.Data.FireBase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class fireBaseToken {

    suspend fun getFcmToken(): String? {
        return try {
            val token = FirebaseMessaging.getInstance()
                .token
                .await()

            Log.d("FCM", "FCM token retrieved successfully")

            token

        } catch (e: Exception) {
            Log.e("FCM", "Failed to retrieve FCM token", e)

            null
        }
    }
}