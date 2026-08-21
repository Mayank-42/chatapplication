package com.example.chatapplication.ui.Screen.GroupChat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.data.Group
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.Data.Viewmodel.MsgVM
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.GroupInfo
import com.example.chatapplication.R
import com.example.chatapplication.ui.Screen.Main.chatScreen

@Composable
fun GroupChatScreen(navControl: NavController,
                    viewMode: databaseVM,
                    userId:String?,
                    msg: MsgVM,
                    tokenManager: TokenManager, save: GroupChatVM){
    val showInfo by save.gettingGroupinfo.collectAsState(initial=emptyList())
    chatScreen(navControl,viewMode,userId,msg,tokenManager)
}

//@Preview (showBackground = true, showSystemUi = true)
//@Composable
//fun show(){
//    GroupChatScreen()
//}