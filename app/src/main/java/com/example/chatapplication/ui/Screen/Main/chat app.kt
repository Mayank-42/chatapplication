package com.example.chatapplication.ui.Screen.Main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Viewmodel.MsgVM
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.Viewmodel.convoVM
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.ui.Screen.GroupChat.formatMessageTime
import io.ktor.client.utils.EmptyContent.status

private val ChatBlack = Color(0xFF000000)
private val ChatWhite = Color(0xFFFFFFFF)
private val ChatBlue = Color(0xFF3B82F6)
private val ChatMuted = Color(0xFF9CA3AF)
private val ChatIncoming = Color(0xFFF4F4F4)
private val ChatInput = Color(0xFFFFFFFF)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun chatScreen(
    navControl: NavController,
    viewMode: databaseVM,
    conversationId: String,
    msg: MsgVM,
    tokenManager: TokenManager,
    convo: convoVM,
    nav:NavController,
    realTime: RealTimeRepo

) {

    val otherUserTyping by msg.otherUserTyping.collectAsState()

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

    var receiverId by remember { mutableStateOf("") }

    val density = androidx.compose.ui.platform.LocalDensity.current

    val dragThreshold = with(density) {
        200.dp.toPx()
    }

    var hasNavigated by remember { mutableStateOf(false) }

    var isTyping by rememberSaveable{mutableStateOf(false)}

    LaunchedEffect(conversationId) {
        try {
            println("CHAT SCREEN: conversationId = $conversationId")
            currentUserId = tokenManager.getUserId() ?: ""
            println("CHAT SCREEN: currentUserId = $currentUserId")

            msg.insertingLocaly(conversationId)

            if (currentUserId.isNotBlank()) {
                msg.markMessagesAsRead(conversationId, currentUserId)
            }

            val conversation = convo.getConversationById(conversationId)

            if (conversation != null) {
                receiverName = conversation.name ?: ""

                receiverImage = conversation.Image

                 receiverId = conversation.receiver_id

                println("CHAT HEADER NAME = ${conversation.name}")
                println("CHAT HEADER IMAGE = ${conversation.Image}")
            }

        } catch (e: Exception) {
            println(
                "CHAT SCREEN ERROR = ${e.message}"
            )

            e.printStackTrace()
        }
        msg.setActiveConversation(conversationId)

    }

    val onlineUsers by realTime.onlineUsers.collectAsState()

    val isOnline = onlineUsers.contains(receiverId)
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
                            navControl.popBackStack("Home", false)
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
                },
                nav,
                receiverId,
                isOnline


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
                        PaddingValues(top = 12.dp, bottom = 12.dp, start = 10.dp, end = 10.dp),

                    verticalArrangement = Arrangement.spacedBy(8.dp,Alignment.Bottom)
                ) {
                    items(task.reversed()) { ele ->

                        if (
                            ele.sender_Id == currentUserId
                        ) {

                            SentMessageBubble(
                                message = ele.message,

                                status =
                                    if (ele.id == lastSentMessageId) {
                                        ele.status
                                    } else {
                                        null
                                    },
                                time =  formatMessageTime(ele.date),
                                onLongClick = {
                                    viewMode.delete(ele)
                                }
                            )

                        } else {
                            ReceivedMessageBubble(
                                message = ele.message,
                                time = formatMessageTime(ele.date),
                                onLongClick = {
                                    viewMode.delete(ele)
                                }
                            )
                        }
                    }
                }
            }
            if(otherUserTyping){
                Box(
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            bottom = 8.dp
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF1F1F5))
                        .padding(
                            horizontal = 14.dp,
                            vertical = 9.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "typing",
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF77777D),
                            fontWeight = FontWeight.Medium
                        )

                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8A8A91))
                            )
                        }
                    }
                }
            }

            ChatInputBar(
                text = textingg,
                onTextChange = { textingg = it;
                    msg.onTypingChanged(
                        conversationId = conversationId,
                        hasText = it.isNotBlank()
                    )
                    // typing started
                   },
                onSend = {
                    if (textingg.isNotBlank()) {
                        msg.onTypingChanged(
                             conversationId,
                             false
                        )
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
    onBackClick: () -> Unit,
    nav: NavController,
    reciver:String,
    isOnline:Boolean

) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{nav.navigate("profileScreen/$reciver")}
            .padding(top = 24.dp),

        color = ChatBlack,

        shadowElevation = 0.dp
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF111111))
                    .combinedClickable(
                        onClick = {
                            onBackClick()
                        }
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = ChatWhite,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            AsyncImage(
                model = image,
                contentDescription = "Profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color =if(isOnline)Color.Green else ChatBlue,
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
                        color = ChatMuted,
                        fontSize = 12.sp
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
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Surface(
                modifier = Modifier
                    .padding(start = 60.dp, end = 4.dp)
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

                val textMeasurer = rememberTextMeasurer()
                SubcomposeLayout { constraints ->
                    val timePlaceable =
                        if (!time.isNullOrBlank()) {
                            subcompose("time") {
                                Text(
                                    text = time,
                                    color = ChatWhite.copy(alpha = 0.70f),
                                    fontSize = 10.sp
                                )
                            }.first().measure(Constraints())
                        } else {
                            null
                        }
                    val spaceBetween = 8.dp.roundToPx()

                    val timeWidth = timePlaceable?.width ?: 0

                    /*
                     * First measure the complete message
                     */
                    val fullTextLayout =
                        textMeasurer.measure(
                            text = message,
                            style = TextStyle(
                                color = ChatWhite,
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            ),
                            constraints = constraints
                        )
                    val lastLineIndex = fullTextLayout.lineCount - 1

                    /*
                     * Get all lines before the last line
                     */
                    val previousLines = mutableListOf<String>()

                    for (lineIndex in 0 until lastLineIndex) {

                        val start = fullTextLayout.getLineStart(lineIndex)

                        val end = fullTextLayout.getLineEnd(lineIndex)

                        previousLines.add(message.substring(start, end))
                    }
                    /*
                     * Get the original last line
                     */
                    val lastStart =
                        fullTextLayout.getLineStart(lastLineIndex)

                    val lastEnd =
                        fullTextLayout.getLineEnd(lastLineIndex)

                    val originalLastLine = message.substring(lastStart, lastEnd)

                    /*
                     * Leave space for timestamp
                     */
                    val lastLineAvailableWidth =
                        if (timePlaceable != null) {
                            (constraints.maxWidth - timeWidth - spaceBetween).coerceAtLeast(0)

                        } else {
                            constraints.maxWidth
                        }

                    /*
                     * Measure last line again
                     */
                    val lastLineLayout =
                        textMeasurer.measure(
                            text = originalLastLine,

                            style = TextStyle(
                                color = ChatWhite,
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            ),

                            constraints = Constraints(
                                maxWidth = lastLineAvailableWidth
                            )
                        )

                    /*
                     * The reduced width can cause the
                     * original last line to wrap again.
                     */
                    val lastLineTexts = mutableListOf<String>()

                    for (
                    lineIndex
                    in 0 until lastLineLayout.lineCount
                    ) {
                        val start =
                            lastLineLayout.getLineStart(lineIndex)

                        val end =
                            lastLineLayout.getLineEnd(lineIndex)

                        lastLineTexts.add(originalLastLine.substring(start, end))
                    }
                    /*
                     * All message lines
                     */
                    val allLineTexts = previousLines + lastLineTexts
                    /*
                     * Measure every line separately
                     */
                    val textPlaceables =
                        allLineTexts.mapIndexed { index, line ->
                            subcompose("text_$index") {
                                Text(
                                    text = line,
                                    color = ChatWhite,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    softWrap = false
                                )

                            }.first().measure(
                                Constraints(
                                    maxWidth =
                                        constraints.maxWidth
                                )
                            )
                        }

                    /*
                     * Last line + timestamp width
                     */
                    val lastTextPlaceable = textPlaceables.lastOrNull()

                    val lastRowWidth =
                        if (
                            lastTextPlaceable != null &&
                            timePlaceable != null
                        ) {
                            lastTextPlaceable.width + spaceBetween + timePlaceable.width

                        } else {
                            lastTextPlaceable?.width ?: 0
                        }

                    /*
                     * Widest previous line
                     */
                    val previousLinesWidth =
                        textPlaceables
                            .dropLast(1)
                            .maxOfOrNull {
                                it.width
                            } ?: 0

                    /*
                     * Final content width
                     */
                    val contentWidth =
                        maxOf(
                            previousLinesWidth,
                            lastRowWidth
                        )

                    /*
                     * Height of all text
                     */
                    val textHeight =
                        textPlaceables.sumOf {
                            it.height
                        }

                    val contentHeight =
                        textHeight.coerceAtLeast(
                            timePlaceable?.height ?: 0
                        )

                    /*
                     * Same padding as your original Row
                     */
                    val horizontalPadding = 14.dp.roundToPx() + 12.dp.roundToPx()

                    val verticalPadding = 10.dp.roundToPx() + 8.dp.roundToPx()

                    layout(
                        width = contentWidth + horizontalPadding,
                        height = contentHeight + verticalPadding
                    ) {

                        var yPosition =
                            10.dp.roundToPx()

                        textPlaceables.forEachIndexed { index, placeable ->

                            val isLast = index == textPlaceables.lastIndex

                            if (!isLast) {
                                placeable.placeRelative(
                                    x = 14.dp.roundToPx(),
                                    y = yPosition
                                )

                                yPosition += placeable.height

                            } else {
                                /*
                                 * Last message line
                                 */
                                placeable.placeRelative(
                                    x = 14.dp.roundToPx(),
                                    y = yPosition
                                )

                                /*
                                 * Timestamp stays at
                                 * the right side.
                                 */
                                timePlaceable?.placeRelative(
                                    x = contentWidth + 14.dp.roundToPx() - timePlaceable.width,
                                    y = yPosition + placeable.height - timePlaceable.height
                                )
                            }
                        }
                    }
                }
            }
        }

        if (
            !status.isNullOrBlank()
        ) {

            Text(
                text = status,

                color = ChatMuted,
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,

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

    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
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

            color = ChatIncoming,

            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomStart = 3.dp,
                bottomEnd = 10.dp
            )
        ) {

            Box(
                modifier = Modifier.padding(
                    start = 14.dp,
                    top = 10.dp,
                    end = 12.dp,
                    bottom = 8.dp
                )
            ) {

                SubcomposeLayout { constraints ->

                    // -------------------------------------------------
                    // 1. Measure timestamp
                    // -------------------------------------------------

                    val timePlaceable =
                        if (!time.isNullOrBlank()) {

                            subcompose("time") {

                                Text(
                                    text = time,
                                    color = Color.Gray,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )

                            }.first().measure(
                                Constraints(
                                    minWidth = 0,
                                    maxWidth = Constraints.Infinity,
                                    minHeight = 0,
                                    maxHeight = Constraints.Infinity
                                )
                            )

                        } else {
                            null
                        }


                    // -------------------------------------------------
                    // 2. Measure complete message
                    // -------------------------------------------------

                    val fullTextLayout =
                        textMeasurer.measure(
                            text = message,
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            ),
                            constraints = constraints
                        )


                    val lastLineIndex =
                        fullTextLayout.lineCount - 1


                    // -------------------------------------------------
                    // 3. Previous lines
                    // -------------------------------------------------

                    val lineRanges =
                        mutableListOf<Pair<Int, Int>>()

                    for (
                    lineIndex in
                    0 until lastLineIndex
                    ) {

                        lineRanges.add(
                            fullTextLayout.getLineStart(
                                lineIndex
                            ) to
                                    fullTextLayout.getLineEnd(
                                        lineIndex
                                    )
                        )
                    }


                    // -------------------------------------------------
                    // 4. Last line
                    // -------------------------------------------------

                    val lastStart =
                        fullTextLayout.getLineStart(
                            lastLineIndex
                        )

                    val lastEnd =
                        fullTextLayout.getLineEnd(
                            lastLineIndex
                        )

                    val lastLineText =
                        message.substring(
                            lastStart,
                            lastEnd
                        )


                    val timeWidth =
                        timePlaceable?.width ?: 0

                    val spaceBetween =
                        if (timePlaceable != null) {
                            5.dp.roundToPx()
                        } else {
                            0
                        }


                    // -------------------------------------------------
                    // 5. Reserve timestamp space ONLY here
                    // -------------------------------------------------

                    val lastLineAvailableWidth =
                        if (timePlaceable != null) {

                            maxOf(
                                1,
                                constraints.maxWidth -
                                        timeWidth -
                                        spaceBetween
                            )

                        } else {

                            constraints.maxWidth
                        }


                    val lastLineLayout =
                        textMeasurer.measure(
                            text = lastLineText,

                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            ),

                            constraints =
                                constraints.copy(
                                    minWidth = 0,
                                    maxWidth =
                                        lastLineAvailableWidth
                                )
                        )


                    // -------------------------------------------------
                    // 6. Build all lines
                    // -------------------------------------------------

                    val allLineTexts =
                        mutableListOf<String>()

                    for ((start, end) in lineRanges) {

                        allLineTexts.add(
                            message.substring(
                                start,
                                end
                            )
                        )
                    }

                    for (
                    lineIndex in
                    0 until lastLineLayout.lineCount
                    ) {

                        val start =
                            lastLineLayout.getLineStart(
                                lineIndex
                            )

                        val end =
                            lastLineLayout.getLineEnd(
                                lineIndex
                            )

                        allLineTexts.add(
                            lastLineText.substring(
                                start,
                                end
                            )
                        )
                    }


                    // -------------------------------------------------
                    // 7. Measure each line
                    // -------------------------------------------------

                    val textPlaceables =
                        allLineTexts.mapIndexed {
                                index,
                                line ->

                            subcompose(
                                "line_$index"
                            ) {

                                Text(
                                    text = line,
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    softWrap = false
                                )

                            }.first().measure(
                                Constraints(
                                    minWidth = 0,
                                    maxWidth =
                                        constraints.maxWidth,
                                    minHeight = 0,
                                    maxHeight =
                                        Constraints.Infinity
                                )
                            )
                        }


                    // -------------------------------------------------
                    // 8. Bubble width
                    // -------------------------------------------------

                    val lastTextPlaceable =
                        textPlaceables.lastOrNull()

                    val lastRowWidth =
                        if (
                            timePlaceable != null &&
                            lastTextPlaceable != null
                        ) {

                            lastTextPlaceable.width +
                                    spaceBetween +
                                    timePlaceable.width

                        } else {

                            lastTextPlaceable?.width ?: 0
                        }


                    val contentWidth =
                        maxOf(
                            textPlaceables
                                .dropLast(1)
                                .maxOfOrNull {
                                    it.width
                                } ?: 0,

                            lastRowWidth
                        )


                    val contentHeight =
                        textPlaceables.sumOf {
                            it.height
                        }


                    // -------------------------------------------------
                    // 9. Place
                    // -------------------------------------------------

                    layout(
                        width = contentWidth,
                        height = contentHeight
                    ) {

                        var yPosition = 0


                        // Previous lines
                        textPlaceables
                            .dropLast(1)
                            .forEach { placeable ->

                                placeable.placeRelative(
                                    x = 0,
                                    y = yPosition
                                )

                                yPosition +=
                                    placeable.height
                            }


                        // Last line + timestamp
                        if (lastTextPlaceable != null) {

                            lastTextPlaceable.placeRelative(
                                x = 0,
                                y = yPosition
                            )

                            timePlaceable?.placeRelative(

                                // Fixed to right edge
                                x = contentWidth -
                                        timePlaceable.width,

                                y = yPosition +
                                        lastTextPlaceable.height -
                                        timePlaceable.height
                            )
                        }
                    }
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

            color = ChatInput,

            shape =
                RoundedCornerShape(20.dp)
        ) {

            Box(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                TextField(
                    value = text,

                    onValueChange = onTextChange,

                    placeholder = {
                        Text(
                            text = "Write a message...",
                            color = Color.Gray
                        )
                    },

//                    singleLine = false,
                   maxLines = 8,
                    colors =
                        TextFieldDefaults.colors(

                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,

                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,

                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),

                    modifier =
                        Modifier
                            .fillMaxWidth().padding(end = 58.dp)
                )

                Button(
                    onClick = onSend,

                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 6.dp, bottom = 6.dp)
                            .size(48.dp),

                    shape = CircleShape,

                    contentPadding = PaddingValues(0.dp),

                    colors = ButtonDefaults.buttonColors(containerColor = ChatBlue)
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