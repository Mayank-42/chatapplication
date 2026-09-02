package com.example.chatapplication.ui.Screen.Main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Viewmodel.MsgVM
import com.example.chatapplication.Data.Viewmodel.convoVM
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.local.TokenManager

private val ChatBlack = Color(0xFF000000)
private val ChatWhite = Color(0xFFFFFFFF)
private val ChatBlue = Color(0xFF3B82F6)
private val ChatMuted = Color(0xFF9CA3AF)
private val ChatIncoming = Color(0xFFF4F4F4)
private val ChatInput = Color(0xFFFFFFFF)

@Composable
fun chatScreen(
    navControl: NavController,
    viewMode: databaseVM,
    conversationId: String,
    msg: MsgVM,
    tokenManager: TokenManager,
    convo: convoVM
) {
    var currentUserId by rememberSaveable {
        mutableStateOf("")
    }

    var receiverName by rememberSaveable {
        mutableStateOf("")
    }

    var receiverRole by rememberSaveable {
        mutableStateOf("")
    }

    var receiverImage by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var dragDistance by remember {
        mutableFloatStateOf(0f)
    }

    val density = androidx.compose.ui.platform.LocalDensity.current

    val dragThreshold = with(density) {
        200.dp.toPx()
    }

    var hasNavigated by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(conversationId) {
        try {
            println("CHAT SCREEN: conversationId = $conversationId")

            currentUserId =
                tokenManager.getUserId() ?: ""

            println(
                "CHAT SCREEN: currentUserId = $currentUserId"
            )

            msg.insertingLocaly(
                conversationId
            )

            if (currentUserId.isNotBlank()) {
                msg.markMessagesAsRead(
                    conversationId,
                    currentUserId
                )
            }

            val conversation =
                convo.getConversationById(
                    conversationId
                )

            if (conversation != null) {
                receiverName =
                    conversation.name ?: ""

                receiverImage =
                    conversation.Image

                println(
                    "CHAT HEADER NAME = ${conversation.name}"
                )

                println(
                    "CHAT HEADER IMAGE = ${conversation.Image}"
                )
            }

        } catch (e: Exception) {
            println(
                "CHAT SCREEN ERROR = ${e.message}"
            )

            e.printStackTrace()
        }
    }

    val task by viewMode
        .getConversation(conversationId)
        .collectAsState(
            initial = emptyList()
        )

    val lastSentMessageId =
        task.lastOrNull {
            it.sender_Id == currentUserId
        }?.id

    LaunchedEffect(task.size) {
        if (currentUserId.isNotBlank()) {
            msg.markMessagesAsRead(
                conversationId,
                currentUserId
            )
        }
    }

    var textingg by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChatBlack)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = {
                            change,
                            dragAmount ->

                        change.consume()

                        if (
                            dragAmount > 0 &&
                            !hasNavigated
                        ) {
                            dragDistance +=
                                dragAmount
                        }

                        if (dragDistance >= dragThreshold
                        ) {
                            hasNavigated = true
                            navControl.popBackStack(
                                "Home",
                                false
                            )
                        }
                    },

                    onDragEnd = {
                        dragDistance = 0f
                    },

                    onDragCancel = {
                        dragDistance = 0f
                    }
                )
            }
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            ChatHeader(
                name =
                    if (
                        receiverName.isBlank()
                    ) {
                        "User"
                    } else {
                        receiverName
                    },

                role = receiverRole,
                image = receiverImage,
                onBackClick = {
                    navControl.popBackStack()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                contentAlignment = Alignment.BottomEnd
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    reverseLayout = true,

                    contentPadding =
                        PaddingValues(
                            top = 12.dp,
                            bottom = 12.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),

                    verticalArrangement = Arrangement.spacedBy(8.dp,Alignment.Bottom)
                ) {
                    items(task.reversed()) { ele ->

                        if (
                            ele.sender_Id == currentUserId
                        ) {

                            SentMessageBubble(
                                message = ele.message,

                                if (
                                    ele.id == task
                                        .lastOrNull { it.sender_Id == currentUserId }
                                        ?.id
                                ) {
                                    ele.status
                                } else {
                                    null
                                },
                                time = null,
                                onLongClick = {
                                    viewMode.delete(ele)
                                }
                            )

                        } else {
                            ReceivedMessageBubble(
                                message = ele.message,
                                time = null,
                                onLongClick = {
                                    viewMode.delete(ele)
                                }
                            )
                        }
                    }
                }
            }

            ChatInputBar(
                text = textingg,

                onTextChange = { textingg = it },

                onSend = {
                    if (textingg.isNotBlank()) {
                        msg.storeMsg(conversationId, textingg)
                        textingg = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatHeader(
    name: String,
    role: String,
    image: String?,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 24.dp
            ),

        color = ChatBlack,

        shadowElevation = 0.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0xFF111111)
                    )
                    .combinedClickable(
                        onClick = {
                            onBackClick()
                        }
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = ChatWhite,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            AsyncImage(
                model = image,
                contentDescription = "Profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = ChatBlue,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = name,
                    color = ChatWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (role.isNotBlank()
                ) {

                    Text(
                        text = role,

                        color =
                            ChatMuted,

                        fontSize =
                            12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SentMessageBubble(
    message: String,
    status: String?,
    time: String?,
    onLongClick: () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Surface(
                modifier = Modifier
                    .padding(
                        start = 60.dp,
                        end = 4.dp
                    )
                    .combinedClickable(
                        onClick = {},

                        onLongClick = {
                            onLongClick()
                        }
                    ),

                color = ChatBlue,

                shape =
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 3.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(
                            start = 14.dp,
                            top = 10.dp,
                            end = 12.dp,
                            bottom = 8.dp
                        )
                ) {

                    Text(
                        text = message,
                        color = ChatWhite,
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )

                    if (
                        !time.isNullOrBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text = time,

                            color = ChatWhite.copy(alpha = 0.70f),
                            fontSize = 10.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }

        if (
            !status.isNullOrBlank()
        ) {

            Text(
                text =
                    status,

                color =
                    ChatMuted,

                fontSize =
                    10.sp,

                fontStyle =
                    FontStyle.Italic,

                fontWeight =
                    FontWeight.Medium,

                modifier =
                    Modifier.padding(
                        top = 2.dp,
                        end = 8.dp
                    )
            )
        }
    }
}

@Composable
private fun ReceivedMessageBubble(
    message: String,
    time: String?,
    onLongClick: () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalAlignment =
            Alignment.Start
    ) {

        Surface(
            modifier = Modifier
                .padding(
                    start = 4.dp,
                    end = 60.dp
                )
                .combinedClickable(
                    onClick = {},

                    onLongClick = {
                        onLongClick()
                    }
                ),

            color =
                ChatIncoming,

            shape =
                RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomStart = 3.dp,
                    bottomEnd = 10.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(
                        start = 14.dp,
                        top = 10.dp,
                        end = 12.dp,
                        bottom = 8.dp
                    )
            ) {

                Text(
                    text =
                        message,

                    color =
                        Color.Black,

                    fontSize =
                        16.sp,

                    lineHeight =
                        22.sp
                )

                if (
                    !time.isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            time,

                        color =
                            Color.Gray,

                        fontSize =
                            10.sp,

                        modifier =
                            Modifier.align(
                                Alignment.End
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                ChatBlack
            )
            .imePadding()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 8.dp,
                bottom = 12.dp
            )
    ) {

        Surface(
            modifier =
                Modifier.fillMaxWidth(),

            color =
                ChatInput,

            shape =
                RoundedCornerShape(20.dp)
        ) {

            Box(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                TextField(
                    value =
                        text,

                    onValueChange =
                        onTextChange,

                    placeholder = {

                        Text(
                            text =
                                "Write a message...",

                            color =
                                Color.Gray
                        )
                    },

                    singleLine = false,

                    maxLines = 4,

                    colors =
                        TextFieldDefaults.colors(

                            focusedTextColor =
                                Color.Black,

                            unfocusedTextColor =
                                Color.Black,

                            focusedContainerColor =
                                Color.Transparent,

                            unfocusedContainerColor =
                                Color.Transparent,

                            focusedIndicatorColor =
                                Color.Transparent,

                            unfocusedIndicatorColor =
                                Color.Transparent
                        ),

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                end = 58.dp
                            )
                )

                Button(
                    onClick =
                        onSend,

                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomEnd
                            )
                            .padding(
                                end = 6.dp,
                                bottom = 6.dp
                            )
                            .size(48.dp),

                    shape =
                        CircleShape,

                    contentPadding =
                        PaddingValues(0.dp),

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    ChatBlue
                            )
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Send,

                        contentDescription =
                            "Send",

                        tint =
                            ChatWhite,
                        modifier =
                            Modifier.size(21.dp)
                    )
                }
            }
        }
    }
}