package com.example.chatapplication.Data.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.GroupRepo
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.tables.GroupInfo
import kotlinx.coroutines.launch

class GroupChatVM(
    var reposatory: GroupRepo
): ViewModel(){

    var gettingGroupinfo=reposatory.getAllGroupinfo
    var selectedUserId by  mutableStateOf<List<String>>(emptyList())
    private set
    fun addUser(id:String){
            selectedUserId=selectedUserId+id
    }
    fun removeUser(id:String){
            selectedUserId=selectedUserId-id
    }
     fun insertGroupInfo(info: GroupInfo){
        viewModelScope.launch{
            reposatory.goupInfoInsert(info)
        }
    }

}
class GroupChatVMfacrory(
    private val repository: GroupRepo
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(GroupChatVM::class.java)) {
            return GroupChatVM(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}