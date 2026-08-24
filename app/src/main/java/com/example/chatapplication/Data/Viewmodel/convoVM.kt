package com.example.chatapplication.Data.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Data.Repo.convoInfoRepo
import com.example.chatapplication.Data.local.tables.CinversationId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class convoVM(private var repo: convoInfoRepo): ViewModel(){

    var gettingConvoInfo= repo.getingConvoInfo

//    fun insertConvoInfo(info: CinversationId){
//        viewModelScope.launch{
//            repo.insertConvoInfo(info)
//        }
fun syncConversations() {
    viewModelScope.launch {
        try {
            repo.syncConversations()
        } catch (e: Exception) {
            println(
                "CONVERSATION SYNC ERROR = ${e.message}"
            )
         }
      }
    }
}
class ConvoVMFactory(
    private val repo: convoInfoRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(convoVM::class.java)) {
            return convoVM(repo) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}