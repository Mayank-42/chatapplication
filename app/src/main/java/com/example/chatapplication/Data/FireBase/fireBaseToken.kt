package com.example.chatapplication.Data.FireBase

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.firebase.messaging.FirebaseMessaging

class fireBaseToken {
    var token =""
    fun fetchToken():String{
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
                token = task.result

                Log.d("FCM", "Registration token received")
                Log.d("FCM", token)
            }
        return token

    }
}