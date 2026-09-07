package com.example.chatapplication.ui.Screen


import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import kotlinx.coroutines.launch
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
    id: TakingUsernameResponse?,
    nav: NavController
) {
     val scope=rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }
    val minHeight = 66.dp
    val maxHeight = 380.dp
    var isExpanded by remember { mutableStateOf(false) }

    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentPage=backStackEntry?.destination?.route

    val isHomeSelected = currentPage == "Home"

    val isGroupSelected = currentPage == "GroupPage"

    val isProfileSelected =
        currentPage?.startsWith("profileScreen/") == true

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
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
                            y = offsetY.value.roundToInt()
                        )
                    }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->

//                            offsetY += delta
                            scope.launch {
                                val newOffset = offsetY.value + delta

                                val clampedOffset = newOffset.coerceIn(
                                    -124f,
                                    0f
                                )

                                offsetY.snapTo(clampedOffset)
                            }

//                            if (offsetY <= -124f) {
//                                offsetY = -124f
//                                isExpanded = true
//                            }
//
//                            if (offsetY >= 0f) {
//                                offsetY = 0f
//                                isExpanded = false
//                            }
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                onProfileClick()
                            }
                            .background(HomeBlue.copy(alpha = 0.12f))
                            .padding(
                                horizontal = 16.dp,
                                vertical = 14.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Profile Image
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(HomeBlue),
                            contentAlignment = Alignment.Center
                        ) {

                            if (id?.photo_url.isNullOrBlank()) {

                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = HomeWhite,
                                    modifier = Modifier.size(34.dp)
                                )

                            } else {

                                AsyncImage(
                                    model = id?.photo_url,
                                    contentDescription = "Profile image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .border(
                                            width = 1.dp,
                                            color = HomeBlue,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // User Information
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = id?.name
                                    ?.replaceFirstChar { it.uppercase() }
                                    ?: "",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = HomeWhite,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = id?.role ?: "",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = HomeMuted,
                                maxLines = 1
                            )
                        }

                        // Small indication that this is clickable
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open profile",
                            tint = HomeMuted,
                            modifier = Modifier.size(22.dp)
                        )
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
                        .background(Color(0xFFEF4444)),
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
                            scope.launch {
                                val newOffset = offsetY.value + delta

                                val clampedOffset = newOffset.coerceIn(
                                    -124f,
                                    0f
                                )

                                offsetY.snapTo(clampedOffset)
                            }

//                            if (offsetY <= -124f) {
//                                offsetY = -124f
//                                isExpanded = true
//                            }
//                            if (offsetY >= 0f) {
//                                offsetY = 0f
//                                isExpanded = false
//                            }

                        }
                    )

                    .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
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
                        onClick = onHomeClick,
                        isHomeSelected
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
                        onClick = onGroupsClick,
                        isGroupSelected
                    )
                    // ==================================================
                    // PROFILE
                    // ==================================================
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (isProfileSelected) HomeBlue
                                else Color.Transparent
                            )
                            .clickable {
                                onProfileClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (id?.photo_url == null) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        } else {
                            AsyncImage(
                                model = id?.photo_url,
                                contentDescription = "Profile Pic",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .border(
                                        1.dp,
                                        Color.Green,
                                        CircleShape
                                    )
                            )
                        }
                    }

                }
            }
        }
    }
}
@Composable
private fun HomeNavigationButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    selected: Boolean=false
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                if (selected) HomeBlue
                else Color.Transparent
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}