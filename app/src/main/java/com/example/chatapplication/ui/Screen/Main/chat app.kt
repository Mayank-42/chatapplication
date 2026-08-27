package com.example.chatapplication.ui.Screen.Main

import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.MsgVM
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.R
import com.example.chatapplication.ui.Screen.Auth.surface
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@Composable
fun chatScreen(navControl: NavController,
               viewMode: databaseVM,
//               userId:String?,
               conversationId:String,
               msg: MsgVM,
               tokenManager: TokenManager
         ){

    var currentUserId by rememberSaveable { mutableStateOf("") }
    var dragDistance by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current
    val dragThreshold = with(density) {
        200.dp.toPx()
    }
    if(dragDistance>=dragThreshold){
        navControl.popBackStack("Home",false)
    }

//    LaunchedEffect(conversationId) {
//        currentUserId = tokenManager.getUserId() ?: ""
//        msg.startRealtime(conversationId)
//        msg.insertingLocaly(conversationId)
//    }
    LaunchedEffect(conversationId) {
        println("CHAT SCREEN: conversationId = $conversationId")
        try {
            currentUserId = tokenManager.getUserId() ?: ""
            println("CHAT SCREEN: currentUserId = $currentUserId")
//            msg.startRealtime(conversationId)
            println("CHAT SCREEN: realtime started")
            msg.insertingLocaly(conversationId)
            println("CHAT SCREEN: local sync started")
            if (currentUserId.isNotBlank()) {
                msg.markMessagesAsRead(
                    conversationId = conversationId,
                    myUserId = currentUserId
                )
            }
        } catch (e: Exception) {
            println("CHAT SCREEN ERROR: ${e.message}")
            e.printStackTrace()
        }
    }

//    val task by viewMode.getallValue.collectAsState(initial = emptyList())
    val task by viewMode.getConversation(conversationId).collectAsState(initial = emptyList())
    LaunchedEffect(task) {
        println("CHAT UI: ROOM FLOW EMITTED")
        println("CHAT UI: MESSAGE COUNT = ${task.size}")

        task.forEach {
            println(
                "CHAT UI MESSAGE: id=${it.id}, message=${it.message}, status=${it.status}"
            )
        }
    }
    val lastSentMessageId = task
        .lastOrNull { it.sender_Id == currentUserId }
        ?.id
    LaunchedEffect(task.size) {

        if (currentUserId.isNotBlank()) {
            msg.markMessagesAsRead(
                conversationId = conversationId,
                myUserId = currentUserId
            )
        }
    }
//    LaunchedEffect(conversationId) {
//        msg.insertingLocaly(conversationId)
//    }

    var textingg by rememberSaveable{mutableStateOf("")}

    Box(modifier= Modifier.fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit) {

            detectHorizontalDragGestures(
                onHorizontalDrag = { change, dragAmount ->
                    change.consume()
                    if (dragAmount > 0) {
                        dragDistance += dragAmount
                    }
                    if (dragDistance >= 200f) {
                        navControl.popBackStack()
                    }
                },
                onDragEnd = { dragDistance = 0f },
                onDragCancel = { dragDistance = 0f }
            )
        }

    ) {
        Column() {
            Box(
                modifier = Modifier.fillMaxWidth().background(Color.White).height(100.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Image(
                        painter = painterResource(R.drawable.example),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(30.dp),)


                    Text(text = "Mayank", color = Color.Black, fontSize = 25.sp)

                    Spacer(modifier = Modifier.width(30.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {

                        Text(text = "online", color = Color.Green, fontSize = 25.sp)
                    }

                }
            }
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(end = 4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                LazyColumn(
                    reverseLayout = true
                ) {
                    items(task.reversed()) { ele ->
                        if (ele.sender_Id == currentUserId) {
                            Column() {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        topEnd = 10.dp,
                                        bottomStart = 8.dp
                                    ),
                                    modifier = Modifier.padding(end=10.dp).combinedClickable(
                                        onClick = {},
                                        onLongClick = { viewMode.delete(ele) }
                                    )
                                ) {
                                    Text(text = ele.message, modifier = Modifier.padding(10.dp))
                                }
                            }
                                Box(modifier=Modifier.fillMaxWidth().padding(bottom = 10.dp,end=10.dp), contentAlignment = Alignment.BottomEnd) {
                                    if (ele.id == lastSentMessageId) {
                                        Text(
                                            text = ele.status,
                                            fontSize = 10.sp,
                                            fontStyle = FontStyle.Italic,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        )
                                    }
                                }
                        }
                        }else{
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        topEnd = 10.dp,
                                        bottomStart = 8.dp
                                    ),
                                    modifier = Modifier.padding(10.dp).combinedClickable(
                                        onClick = {},
                                        onLongClick = { viewMode.delete(ele) }
                                    )
                                ) {
                                    Text(text = ele.message, modifier = Modifier.padding(10.dp))
                                }
                            }
                        }
                    }
                }
            }



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 40.dp)
                    .imePadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = textingg,
                            onValueChange = {
                                textingg = it
                            },
                            placeholder = {
                                Text(text = "enter the text yooo")
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedPlaceholderColor = Color.Gray,
                                unfocusedPlaceholderColor = Color.Gray,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 65.dp)
                        )
                        Button(
                            onClick = {
                                if (textingg.isNotBlank()) {
                                    msg.storeMsg(conversationId, textingg)
                                    textingg = ""
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 6.dp, bottom = 6.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue
                            )
                        ) {

                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            }
        }


}
@Composable
fun counterUi(){
        Spacer(modifier=Modifier.height(60.dp))
    Box(modifier=Modifier.fillMaxSize().background(Color.Black)){
        Surface(modifier=Modifier.fillMaxWidth().clip(shape=RoundedCornerShape(20.dp)).height(60.dp)){
            Box(modifier=Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
            Row(){
            Text(text="dbvs")
                Spacer(modifier=Modifier.width(20.dp))
            Text(text=1000.toString(),
                fontStyle = FontStyle.Italic,
                modifier=Modifier
//                    .size(20.dp)
                    .clip(CircleShape)
                    .border(1.dp,Color.Green,CircleShape)
                    .background(Color.Red)
                    .padding(5.dp)
            )
            }
            }
        }
    }

}
@Preview(showSystemUi=true, showBackground =true)
@Composable
fun showww(){
    counterUi()
}
