package com.example.chatapplication.ui.Screen.Main


import android.R.attr.fontWeight
import android.R.attr.onClick
import android.R.attr.text
import android.R.attr.x
import android.os.Build
import android.text.style.UnderlineSpan
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.Viewmodel.convoVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.roundToInt


// ============================================================
// APP COLORS
// ============================================================
private val HomeBlack = Color(0xFF000000)
private val HomeWhite = Color(0xFFFFFFFF)
private val HomeBlue = Color(0xFF3B82F6)

private val HomeTile = Color(0xFF111111)
private val HomeMuted = Color(0xFF9CA3AF)
private val HomeBorder = Color(0xFF242424)


// ============================================================
// HOME SCREEN
// ============================================================

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navControl: NavController,
    tokenManager: TokenManager,
    userinfoo: UserInfo,
    onLoginSuccess: () -> Unit,
    conversationInfo: convoVM
) {

    val scope = rememberCoroutineScope()


    // ========================================================
    // EXISTING LOGIC
    // ========================================================


    LaunchedEffect(Unit) {
        conversationInfo.startConversationRealtime()
    }


    var id by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) { id = tokenManager.getUserId() ?: "" }

    LaunchedEffect(Unit) { userinfoo.getinfo() }

    val LogedInUser = userinfoo.userInfo.firstOrNull{it.id==id}



    // ========================================================
    // GREETING
    // ========================================================

    val currentTime = LocalTime.now()

    val greetingMessage = when {
        currentTime < LocalTime.NOON ->
            "Good Morning"
        currentTime < LocalTime.of(18, 0) ->
            "Good Afternoon"
        else ->
            "Good Evening"
    }


    // ========================================================
    // CONVERSATIONS
    // ========================================================

    val conversations by conversationInfo.privateConversations.collectAsState(initial = emptyList())

    LaunchedEffect(conversations) {

        val conversationIds = conversations.map { it.conversationId }

        if (conversationIds.isNotEmpty()) {
            conversationInfo.loadOtherUserIds(conversationIds)
        }
    }
    val conversationUsers by conversationInfo.conversationUsers.collectAsState()

    val onlineUsers by conversationInfo.onlineUsers.collectAsState()
    // ========================================================
    // COLLAPSING TOP BAR
    // ========================================================
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(

//        modifier = Modifier
//            .fillMaxSize()
//            .nestedScroll(
//                scrollBehavior.nestedScrollConnection
//            ),
        containerColor = HomeBlack,
        // ====================================================
        // TOP APP BAR
        // ====================================================
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        // BIG HI
                        Row(modifier=Modifier.fillMaxWidth(),verticalAlignment = Alignment.Bottom){
                            Text(
                                text = "HI ",
                                color = HomeBlue,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = LogedInUser?.name?:"",
                                color = HomeBlue,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        // GREETING
                        Text(
                            text = greetingMessage,
                            color = HomeWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = HomeBlack,
                        scrolledContainerColor = HomeBlack,
                        titleContentColor = HomeWhite
                    ),
//                scrollBehavior = scrollBehavior
            )
        },
        // ====================================================
        // BOTTOM NAVIGATION
        // ====================================================
        bottomBar = {
            HomeBottomNavigation(
                onGroupsClick = {
                    navControl.navigate("GroupPage")
                },
                onLogoutClick = {
                    scope.launch {
                        tokenManager.clearTokens()
                        onLoginSuccess()
                    }
                },
                onProfileClick = {
                    scope.launch {
                        if (id.isNotBlank()) {
                            navControl.navigate("profileScreen/$id")
                        }
                    }
                },
                onSetting = {navControl.navigate("SettingPage")},
                LogedInUser
            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HomeBlack)
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // ==================================================
                // SEARCH BAR
                // ==================================================
                HomeSearchBar(
                    onClick = {
                        navControl.navigate("SearchBarPage")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                // ==================================================
                // CONVERSATION LIST
                // ==================================================
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    items(
                        items = conversations,
                        key = {
                            it.conversationId
                        }
                    ) { ele ->
                        // ==================================================
                        // UNREAD COUNT
                        // ==================================================
                        val unreadCount by produceState(
                            initialValue = ele.unread_count,
                            key1 = ele.conversationId,
                            key2 = id
                        ) {

                            if (id.isNotBlank()) {
                                conversationInfo
                                    .getUnreadCount(ele.conversationId, id)
                                    .collect {
                                        value = it
                                    }
                            }
                        }
//                        val unreadCount = ele.unread_count
                        println(
                            "HOME TIME DEBUG: " +
                                    "conversation=${ele.conversationId}, " +
                                    "lastTime=${ele.lastTime}"
                        )
                        // ==================================================
                        // CONVERSATION TILE
                        // ==================================================
                        val otherUserId =
                            conversationUsers[ele.conversationId]

                        var isOnline =
                            otherUserId != null &&
                                    onlineUsers.contains(otherUserId)
                        println(
                            """
                ================= ONLINE DEBUG =================
                CONVERSATION ID = ${ele.conversationId}
                OTHER USER ID   = $otherUserId
                ONLINE USERS    = $onlineUsers
                IS ONLINE       = $isOnline
                =================================================
                 """.trimIndent()
                        )
                        println(
                            "HOME ONLINE DEBUG: " +
                                    "conversation=${ele.conversationId}, " +
                                    "otherUserId=$otherUserId, " +
                                    "onlineUsers=$onlineUsers"
                        )

                        ConversationTile(
                            convoId=isOnline,
                            name = ele.name ?: "",
                            image = ele.Image,
                            lastMessage = ele.lastMessage ?: "",
                            time =
                                formatConversationTime(ele.lastTime),
                            unreadCount = unreadCount,

                            onClick = {
                                navControl.navigate(
                                    "chatScreen/${ele.conversationId}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


// ================================================================
// SEARCH
// ================================================================

@Composable
private fun HomeSearchBar(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp
            )
            .height(56.dp)
            .clickable {
                onClick()
            },

        color = HomeTile,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(width = 1.dp, color = HomeBorder)
    ) {

        Row(

            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = HomeBlue,
                modifier = Modifier.size(23.dp)
            )
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Text(
                text = "Search user by username",
                color = HomeMuted,
                fontSize = 15.sp
            )
        }
    }
}
// ================================================================
// CONVERSATION TILE
// ================================================================
@Composable
private fun ConversationTile(
    convoId: Boolean,
    name: String,
    image: String?,
    lastMessage: String,
    time: String,
    unreadCount: Int,
    onClick: () -> Unit

) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .clickable {
                onClick()
            },
        color = HomeTile,
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(width = 1.dp, color = HomeBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ==================================================
            // PROFILE IMAGE
            // ==================================================
            Box(
                modifier = Modifier.size(56.dp)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(
                            width = 1.5.dp,
                            color= if(convoId) Color.Green else HomeBlue,
                            shape = CircleShape
                        )
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            // ==================================================
            // MESSAGE AREA
            // ==================================================
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = 2.dp
                    )
            ) {
                // ------------------------------------------------
                // USER NAME
                // SMALL
                // ------------------------------------------------
                Text(
                    text =
                        if (name.isBlank())
                            "Unknown"
                        else
                            name.replaceFirstChar { it.uppercase() } ,
                    color = HomeWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 2.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                // ------------------------------------------------
                // LAST MESSAGE
                // BIG
                // LADDER / INDENT
                // ------------------------------------------------
                Text(
                    text =
                        if (lastMessage.isBlank())
                            "No messages yet"
                        else
                            lastMessage,
                    color = HomeMuted,
                    fontSize = 12.sp,
                    fontWeight = if(unreadCount>0) FontWeight.SemiBold else FontWeight.Medium,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 10.dp, end = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // ==================================================
            // TIME + UNREAD
            // ==================================================
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // ------------------------------------------------
                // TIME
                // ------------------------------------------------
                Text(
                    text = time,
                    color =
                        if (unreadCount > 0)
                            HomeBlue
                        else
                            HomeMuted,
                    fontSize = 11.sp,
                    fontWeight =
                        if (unreadCount > 0)
                            FontWeight.SemiBold
                        else
                            FontWeight.Normal
                )
                // ------------------------------------------------
                // UNREAD BADGE
                // ------------------------------------------------
                if (unreadCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(HomeBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text =unreadCount.toString(),
                            color = HomeWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
// ================================================================
// BOTTOM NAVIGATION
// ================================================================


// ================================================================
// BOTTOM NAV ITEM
// ================================================================
@Composable
private fun HomeNavigationButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit

) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}


// ================================================================
// TIME FORMAT
// ================================================================

@RequiresApi(Build.VERSION_CODES.O)
fun formatConversationTime(
    timestamp: String?
): String {
    if (timestamp.isNullOrBlank()) {
        return ""
    }
    return try {
        val instant =
            OffsetDateTime
                .parse(timestamp)
                .toInstant()
        val localDateTime =
            instant.atZone(
                ZoneId.of("Asia/Kolkata")
            )
        val formatter =
            DateTimeFormatter.ofPattern(
                "hh:mm a"
            )
        localDateTime.format(formatter)
    } catch (e: DateTimeParseException) {
        println("TIME FORMAT ERROR = ${e.message}")
        ""
    }
}