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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.local.tables.GroupInfo
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupName(
    nav: NavController,
    save: GroupChatVM,
    infoo: UserInfo
) {
    var Gname by rememberSaveable { mutableStateOf("") }
    var Gbio by rememberSaveable { mutableStateOf("") }

    var popupMessage by remember { mutableStateOf<String?>(null) }
    var Showp by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(popupMessage){
        delay(3000)
        popupMessage = null
        Showp=false

    }

    LaunchedEffect(Unit) {
        infoo.getCompanyUsers()
    }

    LaunchedEffect(save.groupCreated) {
        if (save.groupCreated) {
            save.resetGroupCreated()
            nav.popBackStack(
                "GroupPage",
                inclusive = false
            )
        }
    }

    val selectedUsers = remember(
        save.selectedUserId,
        infoo.companyUsers
    ) {
        save.selectedUserId.mapNotNull { id ->
            infoo.companyUsers.find { user ->
                user.id == id
            }
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        text = "Create Group",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
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
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3B82F6))
                            .border(
                                1.dp,
                                Color(0xFF3B82F6),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GroupAdd,
                            contentDescription = "Create Group",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF111111)
                    ) {

                        TextField(
                            value = Gname,
                            onValueChange = {
                                Gname = it
                            },
                            singleLine=true,
                            placeholder = {
                                Text(
                                    text = "Enter group name",
                                    color = Color.Gray
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                unfocusedTextColor = Color.White,
                                focusedTextColor = Color.White,
                                cursorColor = Color(0xFF3B82F6)
                            )
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF111111)
                ) {

                    TextField(
                        value = Gbio,
                        onValueChange = {
                            Gbio = it
                        },
                        placeholder = {
                            Text(
                                text = "Describe the purpose of this group",
                                color = Color.Gray
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            cursorColor = Color(0xFF3B82F6)
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {

                    Button(
                        onClick = {
                            if (Gname.isBlank()) {
                                popupMessage = "Group name is required"
                                print("it is working")
                                Showp = true
                            } else {
                                save.createGroup(
                                    Gname,
                                    Gbio
                                )
                                print("not woorking")
                            }
                        }
                    ) {
                        Text(
                            text = "Create",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF222222)
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(
                        items = save.selectedUserId,
                        key = { id ->
                            id
                        }
                    ) { id ->

                        val user = selectedUsers.find {
                            it.id == id
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp
                                )
                                .height(80.dp)
                                .clickable { },
                            color = Color(0xFF111111),
                            shape = RoundedCornerShape(35.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF3B82F6))
                                        .border(
                                            1.dp,
                                            Color(0xFF3B82F6),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    if (!user?.photo_url.isNullOrBlank()) {

                                        AsyncImage(
                                            model = user?.photo_url,
                                            contentDescription = "Profile image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                        )

                                    } else {

                                        Text(
                                            text = user?.name
                                                ?.firstOrNull()
                                                ?.uppercase()
                                                ?: "?",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {

                                    Text(
                                        text = user?.name ?: id,
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = user?.role ?: "No designation",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = Showp,
                enter = fadeIn(
                    animationSpec = tween(250)
                ) + scaleIn(
                    initialScale = 0.85f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + slideInVertically(
                    initialOffsetY = { -80 },
                    animationSpec = tween(450)
                ),
                exit = fadeOut(
                    animationSpec = tween(180)
                ) + scaleOut(
                    targetScale = 0.9f,
                    animationSpec = tween(180)
                ) + slideOutVertically(
                    targetOffsetY = { +80 },
                    animationSpec = tween(450)
                )
            ) {

                if (popupMessage != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                        color = Color.White,
                        shape = RoundedCornerShape(14.dp),
                        shadowElevation = 8.dp
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ------------------------------------------
                            // WARNING ICON
                            // -----------------------------------------
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = Color.Red,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            // ------------------------------------------
                            // ERROR MESSAGE
                            // ------------------------------------------
                            Text(
                                text = popupMessage ?: "",
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}