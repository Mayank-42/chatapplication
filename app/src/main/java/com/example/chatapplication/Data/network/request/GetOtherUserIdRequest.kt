package com.example.chatapplication.Data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class GetOtherUserIdRequest(
    val p_conversation_id: String
)
