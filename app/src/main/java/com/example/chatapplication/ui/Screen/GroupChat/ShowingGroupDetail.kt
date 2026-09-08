package com.example.chatapplication.ui.Screen.GroupChat

import android.R.attr.maxLines
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.local.tables.userInfo
import java.lang.reflect.Member

private val GroupDetailBlack = Color.Black
private val GroupDetailWhite = Color.White
private val GroupDetailBlue = Color(0xFF3B82F6)
private val GroupDetailGrey = Color(0xFF9CA3AF)
private val GroupDetailTile = Color(0xFF111111)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowingGroupDetail(
    nav: NavController,
    conversationId: String,
    groupVM: GroupChatVM,
    userVM: UserInfo
) {

//    var GInfo by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(conversationId) {
       groupVM.getingGroupInfo(conversationId)
    }
    val GInfo = groupVM.groupInfo



    val groups by groupVM.gettingGroupinfo.collectAsState(
        initial = emptyList()
    )

    val members by groupVM.gettingAllMember.collectAsState(
        initial = emptyList()
    )

    val group = groups.firstOrNull {
        it.GroupId == conversationId
    }

//    val groupMembers = members.filter {
//        it.GroupId == conversationId
//    }
    val groupMembers=GInfo?.member?: emptyList()

    Scaffold(
        containerColor = GroupDetailBlack,

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GroupDetailBlack,
                    titleContentColor = GroupDetailWhite,
                    navigationIconContentColor = GroupDetailWhite
                ),

                navigationIcon = {
                    IconButton(
                        onClick = {
                            nav.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                title = {
                    Text(
                        text = "Group Details",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        groupVM.deleteGroup(conversationId);
                        nav.popBackStack("GroupPage",false)
                    }) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription ="DeleteForever",
                            tint=Color.Red
                        )
                    }
                }

            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(GroupDetailBlack)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(GroupDetailBlue)
                        .border(
                            width = 2.dp,
                            color = GroupDetailBlue,
                            shape = CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = GInfo?.Gname
                            ?.trim()
                            ?.firstOrNull()
                            ?.uppercase()
                            ?: "G",

                        color = GroupDetailWhite,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = GInfo?.Gname ?: "Group",
                    color = GroupDetailWhite,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = group?.bio ?: "No group description",
                    color = GroupDetailGrey,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = GroupDetailTile,
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        Text(
                            text = "Created By",
                            color = GroupDetailGrey,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = GInfo?.createdByName?: "Unknown",
                            color = GroupDetailWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
//                        Text(
//                            text = GInfo?.createdByName?: "Unknown",
//                            color = GroupDetailWhite,
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Medium
//                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Members",
                        color = GroupDetailWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "(${groupMembers.size})",
                        color = GroupDetailGrey,
                        fontSize = 14.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            items(groupMembers) { member ->

//                val memberInfo = userVM.userInfo.firstOrNull {
//                    it.id == member.GroupMemberId
//                }
                val memberInfo=userVM.userInfo.firstOrNull{it.id==member}

                GroupMemberTile(
                    name = memberInfo?.name ?: "Unknown User",
                    username = memberInfo?.username ?: "username",
                    member,
                    userVM
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}

@Composable
private fun GroupMemberTile(
    name: String,
    username: String,
    member: String,
    useR: UserInfo
) {
    val user=useR.userInfo.firstOrNull{it.id==member}
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),

        color = GroupDetailTile,
        shape = RoundedCornerShape(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(GroupDetailBlue),

                contentAlignment = Alignment.Center
            ) {
                if (user?.photo_url == null) {
                    Text(
                        text = name
                            .trim()
                            .firstOrNull()
                            ?.uppercase()
                            ?: "U",

                        color = GroupDetailWhite,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    AsyncImage(
                        model = user.photo_url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.border(1.dp,Color.Transparent,CircleShape)

                    )
                }
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    color = GroupDetailWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "@$username",
                    color = GroupDetailGrey,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
                    Text(
                        text = user?.role?:"",
                        color = GroupDetailGrey,
                        fontSize = 13.sp,
                        maxLines = 1
                    )

        }
    }
}