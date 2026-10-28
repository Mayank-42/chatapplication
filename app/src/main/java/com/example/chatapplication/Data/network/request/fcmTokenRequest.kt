package com.example.chatapplication.Data.network.request

import com.google.gson.annotations.SerializedName

data class fcmTokenRequest(
    @SerializedName("user_id")
    var CurrentuserId:String,
    @SerializedName("fcm_token")
    var CurrentUserFcmToken:String
){

}
