package com.example.chatapplication.ui.Screen.GroupChat

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.Data.Viewmodel.UserInfo


private val GroupBlack = Color.Black
private val GroupWhite = Color.White
private val GroupBlue = Color(0xFF3B82F6)
private val GroupGrey = Color(0xFF9CA3AF)
private val GroupTile = Color(0xFF111111)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatSearch(
    nav: NavController,
    infoo: UserInfo,
    save: GroupChatVM
) {

    var username by rememberSaveable {
        mutableStateOf("")
    }

    var showIcon by remember {
        mutableStateOf(false)
    }

    var isSearched by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        infoo.getCompanyUsers()
    }

    showIcon = save.selectedUserId.isNotEmpty()

    Scaffold(
        containerColor = GroupBlack,
        topBar = {
            TopAppBar(
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
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = GroupWhite
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Add Group Member",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GroupBlack)
                .padding(paddingValues)
        ) {

            Column {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(50.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        ),
                    color = GroupTile
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        IconButton(
                            onClick = {
                                nav.popBackStack()
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = GroupWhite
                            )
                        ) {

                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }

                        TextField(
                            value = username,
                            onValueChange = {
                                username = it
                                isSearched = false
                            },
                            placeholder = {
                                Text(
                                    text = "Add Member",
                                    color = GroupGrey
                                )
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {

                                    if (username.isNotBlank()) {
                                        infoo.isExsist(username)
                                        isSearched = true
                                    }

                                }
                            ),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = GroupWhite,
                                unfocusedTextColor = GroupWhite,
                                cursorColor = GroupBlue
                            )
                        )
                    }
                }

                val ans = infoo.UserExsist

                if (isSearched) {

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    if (ans?.isExsist == true) {

                        val searchedUser = ans.data

                        if (searchedUser != null) {

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(
                                        start = 5.dp,
                                        end = 5.dp
                                    )
                                    .clip(
                                        RoundedCornerShape(8.dp)
                                    ),
                                color = GroupTile
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    UserProfileImage(
                                        imageUrl = searchedUser.photo_url,
                                        name = searchedUser.name
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = searchedUser.name,
                                            color = GroupWhite,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Text(
                                            text = searchedUser.role
                                                ?: "No designation",
                                            color = GroupGrey,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Checkbox(
                                        checked = searchedUser.id in save.selectedUserId,
                                        onCheckedChange = { checked ->

                                            if (checked) {
                                                save.addUser(
                                                    searchedUser.id
                                                )
                                            } else {
                                                save.removeUser(
                                                    searchedUser.id
                                                )
                                            }

                                        }
                                    )
                                }
                            }
                        }
                    } else {

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(
                                    start = 5.dp,
                                    end = 5.dp
                                )
                                .clip(
                                    RoundedCornerShape(8.dp)
                                ),
                            color = GroupTile
                        ) {

                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(GroupBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "?",
                                        color = GroupWhite,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "User not found",
                                    color = GroupWhite,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Your Colleague",
                        color = GroupGrey,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(
                            start = 10.dp
                        )
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = GroupTile
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                officeCoWorker(
                    userinfoo = infoo,
                    list = save.selectedUserId,
                    onChecked = { id, checked ->

                        if (checked) {
                            save.addUser(id)
                        } else {
                            save.removeUser(id)
                        }

                    }
                )
            }

            if (showIcon) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            end = 35.dp,
                            bottom = 60.dp
                        ),
                    contentAlignment = Alignment.BottomEnd
                ) {

                    FloatingActionButton(
                        onClick = {
                            nav.navigate("GroupName")
                        },
                        containerColor = GroupBlue,
                        contentColor = GroupBlack
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Continue",
                            tint = GroupBlack
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun officeCoWorker(
    userinfoo: UserInfo,
    list: List<String>,
    onChecked: (String, Boolean) -> Unit
) {

    LazyColumn {

        userinfoo.sortedCompanyUsers.forEach { (_, users) ->

            item {

                Text(
                    text = users
                        .firstOrNull()
                        ?.role
                        ?: "Other",
                    color = GroupGrey,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
            }

            items(
                items = users,
                key = { user ->
                    user.id
                }
            ) { ele ->

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 8.dp
                        )
                        .height(80.dp)
                        .clickable { },
                    color = GroupTile,
                    shape = RoundedCornerShape(35.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        UserProfileImage(
                            imageUrl = ele.photo_url,
                            name = ele.name
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {

                            Text(
                                text = ele.name,
                                color = GroupWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = ele.role ?: "No designation",
                                color = GroupGrey,
                                fontSize = 14.sp
                            )

                            Text(
                                text = ele.created_at ?: "",
                                color = GroupGrey,
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }

                        Checkbox(
                            checked = ele.id in list,
                            onCheckedChange = { checked ->

                                onChecked(
                                    ele.id,
                                    checked
                                )

                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileImage(
    imageUrl: String?,
    name: String
) {

    Box(
        modifier = Modifier
            .padding(start = 10.dp)
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

        if (!imageUrl.isNullOrBlank()) {

            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )

        } else {

            Text(
                text = name
                    .trim()
                    .firstOrNull()
                    ?.uppercase()
                    ?: "?",
                color = GroupWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}