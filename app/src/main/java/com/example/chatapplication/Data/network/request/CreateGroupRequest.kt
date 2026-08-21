package com.example.chatapplication.Data.network.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    @SerializedName("p_name")
    val name: String,

    @SerializedName("p_member_ids")
    val memberIds: List<String>
)