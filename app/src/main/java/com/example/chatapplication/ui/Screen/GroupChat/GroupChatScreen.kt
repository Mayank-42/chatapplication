package com.example.chatapplication.ui.Screen.GroupChat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.chatapplication.Data.Viewmodel.convoVM
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.GroupInfo
import com.example.chatapplication.R
import com.example.chatapplication.ui.Screen.Main.chatScreen

@Composable
fun GroupChatScreen(navControl: NavController,
                    viewMode: databaseVM,
                    conversationId:String?,
                    msg: MsgVM,
                  tokenManager: TokenManager, save: GroupChatVM,convo:convoVM){
    chatScreen(navControl,viewMode,conversationId?:"",msg,tokenManager,convo)
//    val showInfo by save.gettingGroupinfo.collectAsState(initial=emptyList())
//    var currentUserId by rememberSaveable { mutableStateOf("") }
//
//
//    LaunchedEffect(Unit) {
//        currentUserId = tokenManager.getUserId() ?: ""
//        msg.startRealtime()
//    }
//
////    val task by viewMode.getallValue.collectAsState(initial = emptyList())
//    val task by viewMode.getConversation(currentUserId, userId ?: "").collectAsState(initial = emptyList())
//    LaunchedEffect(Unit) {
//        msg.insertingLocaly(currentUserId,userId?:"")
//    }
//
//    var textingg by rememberSaveable{mutableStateOf("")}
//
//
//    Box(modifier= Modifier.fillMaxSize().background(Color.Black)) {
//        Column() {
//            Box(
//                modifier = Modifier.fillMaxWidth().background(Color.White).height(100.dp),
//                contentAlignment = Alignment.BottomEnd
//            ) {
//                Row(verticalAlignment = Alignment.Bottom) {
//                    Image(
//                        painter = painterResource(R.drawable.example),
//                        contentDescription = null,
//                        modifier = Modifier.size(50.dp)
//                            .border(1.dp, MaterialTheme.colorScheme.primary)
//                    )
//                    Spacer(modifier = Modifier.width(30.dp),)
//
//
//                    Text(text = "Mayank", color = Color.Black,
//                        fontSize = 25.sp,
//                        modifier=Modifier.clickable(onClick = {})
//                    )
//
//                    Spacer(modifier = Modifier.width(30.dp))
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.BottomEnd
//                    ) {
//
//                        Text(text = "online", color = Color.Green, fontSize = 25.sp)
//                    }
//
//                }
//            }
//            Box(
//                modifier = Modifier.fillMaxWidth().weight(1f).padding(end = 4.dp),
//                contentAlignment = Alignment.BottomEnd
//            ) {
//                LazyColumn(
//                    reverseLayout = true
//                ) {
//                    items(task.reversed()) { ele ->
//                        if (ele.sender_Id == currentUserId) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.End
//                            ) {
//                                Image(
//                                    painter = painterResource(R.drawable.example),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(20.dp)
//                                        .clip(shape = CircleShape)
//                                        .border(1.dp, Color.Green, CircleShape)
//                                )
//                                Column() {
//                                Surface(
//                                    color = Color.White,
//                                    shape = RoundedCornerShape(
//                                        topStart = 10.dp,
//                                        topEnd = 10.dp,
//                                        bottomStart = 8.dp
//                                    ),
//                                    modifier = Modifier.padding(10.dp).combinedClickable(
//                                        onClick = {},
//                                        onLongClick = { viewMode.delete(ele) }
//                                    )
//                                ) {
//                                    Column(){
//                                     Text(text= "Name",fontsize=4.sp)
//                                    Text(text = ele.message, modifier = Modifier.padding(10.dp))
//                                    }
//                                }
//                                    Text(text=ele.date)
//                             }
//                            }
//                        } else {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.Start
//                            ) {
//                                Surface(
//                                    color = Color.White,
//                                    shape = RoundedCornerShape(
//                                        topStart = 10.dp,
//                                        topEnd = 10.dp,
//                                        bottomStart = 8.dp
//                                    ),
//                                    modifier = Modifier.padding(10.dp).combinedClickable(
//                                        onClick = {},
//                                        onLongClick = { viewMode.delete(ele) }
//                                    )
//                                ) {
//                                    Text(text = ele.message, modifier = Modifier.padding(10.dp))
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//
//
//            Box(
//                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, bottom = 40.dp)
//                    .imePadding(),
//                contentAlignment = Alignment.BottomCenter
//            ) {
//                Surface(
//                    modifier = Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(18.dp))
//                ) {
//                    Row() {
//                        Text(text = "jdbsvsdhvsb", color = Color.Black)
//                        TextField(
//                            value = textingg,
//                            onValueChange = { textingg = it },
//                            placeholder = { Text(text = "enter the text yooo") },
//                            colors = TextFieldDefaults.colors(
//                                focusedTextColor = Color.Black,
//                                focusedContainerColor = Color.Transparent,
//                                focusedPlaceholderColor = Color.Gray,
//                                unfocusedTextColor = Color.Black,
////                        unfocusedContainerColor = Color.Transparent
//
//                            ),
//                            modifier = Modifier.weight(1f)
//                        )
//                        Button(
//                            onClick = {
////                            if(textingg.length!=0){ //if we do this thaen we can able to put space and then send so ya thats y
//                                if (textingg.isNotBlank()) {
////                            viewMode.insert(
////                                MessageInfo(
////                                      0,
////                                    "",
////                                    "mayank",
////                                    textingg,
////                                    10,
////                                    false
////                                )
////
////                            )
//                                    msg.storeMsg(userId ?: "", textingg)
//                                };
//                                textingg = "";
//                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Send,
//                                contentDescription = null,
//                                tint = Color.White
//
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
}

//@Preview (showBackground = true, showSystemUi = true)
//@Composable
//fun show(){
//    GroupChatScreen()
//}