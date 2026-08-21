package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.GroupOperation
import com.example.chatapplication.Data.local.tables.GroupInfo
import com.example.chatapplication.Data.local.tables.groupMember
import com.example.chatapplication.Data.network.ApiService
import com.example.chatapplication.Data.network.clients.retroFitClient.apiService
import com.example.chatapplication.Data.network.request.CreateGroupRequest
import retrofit2.Response

class GroupRepo(
    val work: GroupOperation,
    private val apiService: ApiService
) {

    suspend fun goupInfoInsert(Info: GroupInfo){
        work.GroupInfoInsert(Info)
    }
       var getAllGroupinfo= work.getAllInfo()

    suspend fun GroupMemberInsert(memberInfo: groupMember){
        work.GroupMemberInfo(memberInfo)
    }
    var gettAllMember=work.gatAllmember()

    suspend fun createGroup(name: String, memberIds: List<String>): Response<String> {

        val request = CreateGroupRequest(name = name, memberIds = memberIds)
        return apiService.createGroup(request)
    }


}
//suspend fun goupInfoInsert(GroupId:String,GropName:String,bio:String,memeber:List<String>): GroupInfo{
//    work.GroupInfoInsert(GroupId,GropName,bio,memeber)
//}