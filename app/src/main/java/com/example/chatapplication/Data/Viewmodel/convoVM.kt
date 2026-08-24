package com.example.chatapplication.Data.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.convoInfoRepo
import com.example.chatapplication.Data.local.tables.CinversationId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class convoVM(private var repo: convoInfoRepo): ViewModel(){

    var gettingConvoInfo= repo.getingConvoInfo

    fun insertConvoInfo(info: CinversationId){
        viewModelScope.launch{
            repo.insertConvoInfo(info)
        }
    }
}