package com.example.chatapplication.Data.network.response

data class UserNameExistResponse(
    var isExsist:Boolean,
    var data: TakingUsernameResponse?
) {
}