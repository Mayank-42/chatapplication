package com.example.chatapplication.Data.Repo


import android.R.attr.path
import com.example.chatapplication.Data.local.operation
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.request.UserNameExistRequest
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import com.example.chatapplication.Data.network.response.UserNameExistResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import retrofit2.Response

class UserInfoReposatory(
    private val userInfoRepo: ApiService,
    private val supabase: SupabaseClient

) {
    suspend fun takingUserName():Response<List<TakingUsernameResponse>>{
        return userInfoRepo.takingUserName()
    }
    suspend fun isExist(name:String): Response<UserNameExistResponse>{
        val request= UserNameExistRequest(username_input=name)
        return userInfoRepo.isExsist(request)
    }
    suspend fun uploadIma(id:String,byte:ByteArray){
        val path = "$id/profile.jpg"
        supabase.storage.from("Profile_pic")
            .upload("$id/profile.jpg",byte){
                upsert=true
            }
        supabase.from("PROFILE")
            .update(
                mapOf(
                    "photo_url" to path
                )
            ) {
                filter {
                    eq("id", id)
                }
            }
    }
}