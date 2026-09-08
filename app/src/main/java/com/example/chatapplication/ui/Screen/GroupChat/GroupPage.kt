package com.example.chatapplication.ui.Screen.GroupChat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.Viewmodel.convoVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.ui.Screen.HomeBottomNavigation
import kotlinx.coroutines.launch

private val GroupBlack = Color.Black
private val GroupWhite = Color.White
private val GroupBlue = Color(0xFF3B82F6)
private val GroupGrey = Color(0xFF9CA3AF)
private val GroupTile = Color(0xFF111111)
private val HomeMuted = Color(0xFF9CA3AF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupPage(
    nav: NavController,
    save: GroupChatVM,
    convoInfo: convoVM,
    token: TokenManager,
    user: UserInfo,
    onLoginSuccess:() ->Unit

){
    val groups by save.gettingGroupinfo.collectAsState(initial = emptyList())
    val conversations by convoInfo.groupConversations.collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()
    var id by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) { id = token.getUserId() ?: "" }

    val currentUser =
        user.userInfo.firstOrNull {
            it.id == id
        }

    Scaffold(
        containerColor = GroupBlack,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GroupBlack,
                    titleContentColor = GroupWhite,
                    navigationIconContentColor = GroupWhite,
                    actionIconContentColor = GroupWhite
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            nav.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = "Group Chat",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
//                actions = {
//                    IconButton(
//                        onClick = { }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Search,
//                            contentDescription = "Search",
//                            tint = GroupWhite
//                        )
//                    }
//                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar={
            HomeBottomNavigation(
                onGroupsClick = {
                    nav.navigate("GroupPage")
                },
                onLogoutClick = {
                    scope.launch {
                        token.clearTokens()
                        onLoginSuccess()
                    }
                },
                onProfileClick = {
                    scope.launch {
                        if (id.isNotBlank()) {
                            nav.navigate("profileScreen/$id")
                        }
                    }
                },
                onHomeClick={
                    nav .navigate("Home")
                },
                onSetting = {nav.navigate("SettingPage")},
                currentUser,
                nav
            )
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(GroupBlack)
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                if (conversations.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                modifier = Modifier.size(54.dp),
                                tint = GroupBlue
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "No groups yet",
                                color = GroupWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Create your  group using\nthe + button at the bottom right.",
                                color = GroupGrey,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 21.sp
                            )
                        }
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(conversations) { ele ->

                            GropChatTile(
                                Gname = ele.name ?: "",
                                nav = nav,
                                id = ele.conversationId,
                                lastMesage = ele.lastMessage ?: "",
                                time = ele.lastTime ?: ""
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        end = 25.dp,
                        bottom = 25.dp
                    ),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = {
                        nav.navigate("GropChatSearch")
                    },
                    containerColor = GroupBlue,
                    contentColor = GroupBlack,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = "Create Group",
                        tint = GroupBlack
                    )
                }
            }
        }
    }

}

@Composable
fun GropChatTile(
    Gname: String,
    nav: NavController,
    id: String,
    lastMesage: String = "this is goup is created by somone",
    time: String
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
            .clickable(
                onClick = {
                    nav.navigate("GroupChatScreen/${id}")
                }
            ),
        color = GroupTile,
        shape = RoundedCornerShape(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(GroupBlue)
                    .border(
                        width = 1.5.dp,
                        color = GroupBlue,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = Gname
                        .trim()
                        .firstOrNull()
                        ?.uppercase()
                        ?: "G",
                    color = GroupWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 13.dp)
            ) {

                Text(
                    text = Gname,
                    color = GroupWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )

                Text(
                    text = lastMesage,
                    color = GroupGrey,
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(
                        top = 4.dp,
                        start = 3.dp
                    )
                )
            }

            Box(
                modifier = Modifier
                    .width(55.dp)
                    .padding(start = 5.dp),
                contentAlignment = Alignment.TopEnd
            ) {

                Text(
                    text = time,
                    color = GroupGrey,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}