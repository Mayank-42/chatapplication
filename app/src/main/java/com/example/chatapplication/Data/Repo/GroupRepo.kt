package com.example.chatapplication.Data.Repo

import com.example.chatapplication.Data.DAO.GroupOperation
import com.example.chatapplication.Data.local.tables.GroupInfo

class GroupRepo(val work: GroupOperation) {

    suspend fun goupInfoInsert(Info: GroupInfo){
        work.GroupInfoInsert(Info)
    }

       var getAllGroupinfo= work.getAllInfo()

}
//suspend fun goupInfoInsert(GroupId:String,GropName:String,bio:String,memeber:List<String>): GroupInfo{
//    work.GroupInfoInsert(GroupId,GropName,bio,memeber)
//}