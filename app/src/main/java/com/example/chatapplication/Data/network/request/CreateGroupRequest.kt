package com.example.chatapplication.Data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    @SerialName("p_name")
    val name: String,

    @SerialName("p_member_ids")
    val memberIds: List<String>
)