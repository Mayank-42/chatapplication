package com.example.chatapplication.Data.FireBase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class fireBaseToken {
    fun fetchToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task->
                if (!task.isSuccessful) {
                    Log.e(
                        "FCM",
                        "Fetching FCM token failed",
                        task.exception
                    )
                    return@addOnCompleteListener
                }
                val token = task.result

                Log.d("FCM", "Registration token received")
                Log.d("FCM", token)
            }

    }
}