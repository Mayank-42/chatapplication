package com.example.chatapplication.Data.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.local.tables.userLoginInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.jvm.java

class databaseVM( var reposatory: reposatory): ViewModel() {

    var getallValue: Flow<List<MessageInfo>> = reposatory.getAllValue

    fun getConversation(conversationId: String
    ): Flow<List<MessageInfo>> {
        return reposatory.getConversation(conversationId)
    }

    fun insert(ele: MessageInfo){
        viewModelScope.launch{
            reposatory.insert(ele)
        }
    }
    fun logininsert(ele: userLoginInfo){
        viewModelScope.launch{
            reposatory.loginInsert(ele)
        }
    }
    fun userinsert(ele: userInfo){
        viewModelScope.launch{
            reposatory.userInsert(ele)
        }
    }
    fun delete(ele: MessageInfo){
        viewModelScope.launch{
            reposatory.delte(ele)
        }
    }
    fun logindelete(ele: userLoginInfo){
        viewModelScope.launch{
            reposatory.logindelte(ele)
        }
    }
    fun userdelete(ele: userInfo){
        viewModelScope.launch{
            reposatory.userdelte(ele)
        }
    }

    fun update(ele: MessageInfo){
        viewModelScope.launch{
            reposatory.update(ele)
        }
    }



}
class dataBaseVMfacrory(
    private val repository: reposatory
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(databaseVM::class.java)) {
            return databaseVM(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}