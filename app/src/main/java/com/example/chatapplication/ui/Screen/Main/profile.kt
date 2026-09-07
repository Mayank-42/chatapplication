package com.example.chatapplication.ui.Screen.Main

import android.R.string.yes
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.ui.Screen.HomeBottomNavigation
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.SerialDescriptor

private val ProfileBlack = Color(0xFF000000)
private val ProfileWhite = Color(0xFFFFFFFF)
private val ProfileBlue = Color(0xFF3B82F6)
private val ProfileGrey = Color(0xFF9CA3AF)
private val ProfileTile = Color(0xFF111111)

@Composable
fun profileScreen(
    nav: NavController,
    user: UserInfo,
    token: TokenManager,
    id: String,
    realTime: RealTimeRepo,
    onLoginSuccess: () -> Unit,

) {

    val scope = rememberCoroutineScope()

    val currentUser =
        user.userInfo.firstOrNull {
            it.id == id
        }

    var userid by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        userid = token.getUserId() ?: ""
    }
    val onlineUsers by realTime.onlineUsers.collectAsState()
    val isOnline = onlineUsers.contains(currentUser?.id)

    val context = LocalContext.current

    val imagePicker =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri != null) {
                val inputStream = context.contentResolver.openInputStream(uri)

                val bytes = inputStream?.readBytes()

                if (bytes != null) {
                    user.uploadImg(userid, bytes)
                }

                println("Image bytes: ${bytes?.size}")
            }
        }
    Scaffold(
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
                    nav.navigate("Home")
                },
                onSetting = {nav.navigate("SettingPage")},
                currentUser
            )
        }
    ) {paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfileBlack)
                .padding(paddingValues)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier.size(170.dp)
                ) {

                    AsyncImage(
                        model = currentUser?.photo_url,
                        contentDescription = "profile image",
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = if (isOnline) Color.Green else ProfileBlue,
                                    shape = CircleShape
                                )
                    )
                    if (currentUser?.id == userid) {
                        Surface(
                            modifier =
                                Modifier
                                    .size(48.dp)
                                    .align(
                                        Alignment.BottomEnd
                                    )
                                    .clickable {

                                        imagePicker.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts
                                                    .PickVisualMedia
                                                    .ImageOnly
                                            )
                                        )
                                    },

                            color = ProfileBlue,
                            shape = CircleShape
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit profile image",
                                    tint = ProfileBlack,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = currentUser?.name ?: "Name",
                    color = ProfileWhite,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "@${currentUser?.username ?: "username"}",
                    color = ProfileGrey,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                profileTile(
                    title = "Name",
                    content = currentUser?.name
                )

                profileTile(
                    title = "Username",
                    content = currentUser?.username
                )

                profileTile(
                    title = "Role",
                    content = currentUser?.role ?: "No designation"
                )

                profileTile(
                    title = "Email",
                    content = currentUser?.email
                )
                if (currentUser?.id == userid) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color.Red)
                            .padding(10.dp)
                            .clickable {
                                scope.launch {
                                    token.clearTokens()
                                    onLoginSuccess()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "logOut",
                                tint = Color.Black
                            )
                            Text(text = "LogOut", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

            }
        }
    }
}

    @Composable
    fun profileTile(
        title: String,
        content: String?
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .height(68.dp),

            color = ProfileTile,

            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier =
                    Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 10.dp
                    ),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text = title,
                    color = ProfileGrey,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = content ?: "Not available",
                    color = ProfileWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
