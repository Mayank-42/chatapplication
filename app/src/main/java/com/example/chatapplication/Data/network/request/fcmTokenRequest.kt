package com.example.chatapplication.Data.network.request

import com.google.gson.annotations.SerializedName

data class fcmTokenRequest(
    @SerializedName("v_user_id")
    var CurrentuserId:String,
    @SerializedName("p_fcm_token")
    var CurrentUserFcmToken:String
){

}
