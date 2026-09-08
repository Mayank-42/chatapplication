package com.example.chatapplication.Data.network.response

data class getGroupInfoResponse(
    var Gname:String,
//    var Gbio:String,
    var createdById:String,
    var createdByName:String,
    var member:List<String>
){
}
