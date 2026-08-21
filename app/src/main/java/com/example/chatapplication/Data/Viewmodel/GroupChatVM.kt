package com.example.chatapplication.Data.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GroupChatVM(): ViewModel(){

    var selectedUserId by  mutableStateOf<List<String>>(emptyList())
    private set

    fun addUser(id:String){
            selectedUserId=selectedUserId+id
    }
    fun removeUser(id:String){
            selectedUserId=selectedUserId-id
    }
}