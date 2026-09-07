package com.example.chatapplication.ui.Screen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import kotlin.math.roundToInt

private val HomeBlack = Color(0xFF000000)
private val HomeWhite = Color(0xFFFFFFFF)
private val HomeBlue = Color(0xFF3B82F6)

private val HomeTile = Color(0xFF111111)
private val HomeMuted = Color(0xFF9CA3AF)
private val HomeBorder = Color(0xFF242424)

@Composable
 fun HomeBottomNavigation(
    onGroupsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHomeClick:() ->Unit,
    onSetting :()->Unit,
    id: TakingUsernameResponse?
) {
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    val minHeight = 66.dp
    val maxHeight = 380.dp
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 12.dp
            )
    ) {
        if (isExpanded) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight)
                    .align(Alignment.BottomCenter)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = offsetY.roundToInt()
                        )
                    }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->

                            offsetY += delta

                            if (offsetY <= -124f) {
                                offsetY = -124f
                                isExpanded = true
                            }

                            if (offsetY >= 0f) {
                                offsetY = 0f
                                isExpanded = false
                            }
                        }
                    )
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),

                color = HomeWhite,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp
                )
            ) {

                // ====================================================
                // EXPANDED CONTENT
                // ====================================================

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp, bottom = 70.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(modifier=Modifier.fillMaxWidth().padding(top=10.dp,start=15.dp).clickable{ onProfileClick()}, verticalAlignment = Alignment.CenterVertically){
                        AsyncImage(
                            model=id?.photo_url,
                            contentDescription = "profile image",
                            contentScale=ContentScale.Crop,
                            modifier=Modifier.size(60.dp).clip(CircleShape).border(1.dp,Color.LightGray,CircleShape)

                        )
//                            Box(modifier=Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(modifier=Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {

                            Text(
                                text =id?.name?.replaceFirstChar { it.uppercase() } ?: "",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold

                            )

                            Text(
                                text = id?.role ?: "", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color=HomeBlue
                            )
                        }
                    }


                    //}
                    HorizontalDivider(modifier=Modifier.fillMaxWidth(),color=Color.LightGray)
                    Row(modifier=Modifier.fillMaxWidth().padding(top=10.dp,start=15.dp).clickable{onGroupsClick()},verticalAlignment =Alignment.CenterVertically){

                        HomeNavigationButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = "Groups",
                                    tint = HomeBlack,
                                    modifier = Modifier.size(30.dp)
                                )
                            },
                            onClick = onGroupsClick
                        )
                        Text(text="Group")
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(top=10.dp,start=15.dp).clickable{onSetting()},verticalAlignment =Alignment.CenterVertically)
                    {
                        HomeNavigationButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Logout",
                                    tint = HomeBlack,
                                    modifier = Modifier.size(30.dp)
                                )
                            },
                            onClick = onSetting
                        )
                        Text(text="Seeting")
                    }
                    Row(modifier=Modifier.fillMaxWidth()
                        .padding(top=10.dp,start=15.dp,end=15.dp)
                        .clip(shape=RoundedCornerShape(20.dp))
                        .background(Color.Red),
                        verticalAlignment =Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        HomeNavigationButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "Logout",
                                    tint = HomeBlack,
                                    modifier = Modifier.size(30.dp)
                                )
                            },
                            onClick = onLogoutClick
                        )

                    }
                }
            }
        }else{

            Surface(

                modifier = Modifier
                    .fillMaxWidth()
//                            .offset {
//                            IntOffset(
//                                x = 0,
//                                y = offsetY.roundToInt()
//                            )
//                        }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            offsetY += delta

                            if (offsetY <= -124f) {
                                offsetY = -124f
                                isExpanded = true
                            }
                            if (offsetY >= 0f) {
                                offsetY = 0f
                                isExpanded = false
                            }

                        }
                    )

                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    )
                    .height(if (isExpanded) maxHeight else minHeight)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),

                color = HomeWhite,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                Box(modifier=Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter){
                    Box(modifier=Modifier.height(6.dp).width(50.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.Gray)

                    ){}
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // ==================================================
                    // LOGOUT
                    // ==================================================
                    HomeNavigationButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Logout",
                                tint = HomeBlack,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        onClick = onHomeClick
                    )
                    // ==================================================
                    // GROUPS
                    // ==================================================
                    HomeNavigationButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Groups",
                                tint = HomeBlack,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        onClick = onGroupsClick
                    )
                    // ==================================================
                    // PROFILE
                    // ==================================================
                    AsyncImage(
                        model=id?.photo_url,
                        contentDescription ="profile Pic",
                        contentScale = ContentScale.Crop,
                        modifier=Modifier.size(30.dp)
                            .clip(CircleShape)
                            .border(1.dp,Color.Green,CircleShape)
                            .clickable{
                                onProfileClick()
                            }
                    )

                }
            }
        }
    }
}
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
