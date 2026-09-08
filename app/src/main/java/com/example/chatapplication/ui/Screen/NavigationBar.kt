package com.example.chatapplication.ui.Screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.network.response.TakingUsernameResponse
import kotlinx.coroutines.launch


private val HomeBlack = Color(0xFF000000)
private val HomeWhite = Color(0xFFFFFFFF)
private val HomeBlue = Color(0xFF3B82F6)
private val HomeTile = Color(0xFF111111)
private val HomeMuted = Color(0xFF9CA3AF)
private val HomeBorder = Color(0xFF242424)
private val LogoutRed = Color(0xFFEF4444)


@Composable
fun HomeBottomNavigation(
    onGroupsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSetting: () -> Unit,
    id: TakingUsernameResponse?,
    nav: NavController
) {

    val scope = rememberCoroutineScope()

    val minHeight = 66.dp
    val maxHeight = 380.dp

    /*
     * dragAmount:
     *
     * 0f                    = collapsed
     * -(maxHeight-minHeight) = expanded
     */
    val dragAmount = remember {
        Animatable(0f)
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }


    /*
     * Navigation state comes from NavController.
     */
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentPage = backStackEntry?.destination?.route

    val isHomeSelected =
        currentPage == "Home"

    val isGroupSelected =
        currentPage == "GroupPage"

    val isProfileSelected =
        currentPage?.startsWith("profileScreen/") == true


    /*
     * Total amount the sheet can grow.
     *
     * 380 - 66 = 314dp
     */
    val expandableHeight =
        maxHeight - minHeight


    /*
     * Current sheet height.
     *
     * When dragAmount = 0:
     *
     *     height = 66dp
     *
     * When dragAmount = -314:
     *
     *     height = 380dp
     */
    val currentHeight =
        minHeight + (-dragAmount.value).dp


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 12.dp
            ),
        contentAlignment = Alignment.BottomCenter
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(currentHeight)

                /*
                 * Drag the sheet vertically.
                 */
                .draggable(
                    orientation = Orientation.Vertical,

                    state = rememberDraggableState { delta ->

                        scope.launch {

                            val newValue =
                                (
                                        dragAmount.value + delta
                                        ).coerceIn(
                                        -expandableHeight.value,
                                        0f
                                    )

                            dragAmount.snapTo(newValue)


                            /*
                             * Update expanded state while dragging.
                             */
                            isExpanded =
                                newValue < -(expandableHeight.value * 0.5f)
                        }
                    },

                    /*
                     * When finger leaves screen,
                     * snap to either collapsed or expanded.
                     */
                    onDragStopped = {

                        scope.launch {

                            val middle =
                                -expandableHeight.value / 2f

                            if (dragAmount.value < middle) {

                                dragAmount.animateTo(
                                    targetValue = -expandableHeight.value,
                                    animationSpec = spring()
                                )

                                isExpanded = true

                            } else {

                                dragAmount.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring()
                                )

                                isExpanded = false
                            }
                        }
                    }
                )
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp
                    )
                ),

            color = HomeWhite,

            shape = RoundedCornerShape(
                topStart = 30.dp,
                topEnd = 30.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 14.dp,
                        vertical = 10.dp
                    )
            ) {

                /*
                 * =================================
                 * DRAG HANDLE
                 * =================================
                 */

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    contentAlignment = Alignment.TopCenter
                ) {

                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(HomeBlack)
                    )
                }


                /*
                 * =================================
                 * EXPANDED CONTENT
                 * =================================
                 *
                 * Only visible after the sheet
                 * becomes expanded.
                 */
                if (isExpanded) {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )


                    /*
                     * ===============================
                     * PROFILE
                     * ===============================
                     */

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                            .background(
                                HomeBlue.copy(alpha = 0.12f)
                            )
                            .clickable {
                                onProfileClick()
                            }
                            .padding(
                                horizontal = 14.dp,
                                vertical = 12.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(HomeBlue),
                            contentAlignment = Alignment.Center
                        ) {

                            if (id?.photo_url == null) {

                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = HomeWhite,
                                    modifier = Modifier.size(28.dp)
                                )

                            } else {

                                AsyncImage(
                                    model = id.photo_url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }


                        Spacer(
                            modifier = Modifier.width(14.dp)
                        )


                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = id?.name ?: "User",
                                color = HomeBlack,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = id?.role ?: "Profile",
                                color = HomeMuted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }


                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = HomeBlack
                        )
                    }


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    /*
                     * ===============================
                     * GROUPS
                     * ===============================
                     */

                    ExpandedNavigationItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                tint = HomeBlack
                            )
                        },
                        title = "Groups",
                        onClick = onGroupsClick
                    )


                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                    /*
                     * ===============================
                     * SETTINGS
                     * ===============================
                     */

                    ExpandedNavigationItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = HomeBlack
                            )
                        },
                        title = "Settings",
                        onClick = onSetting
                    )


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    HorizontalDivider(
                        color = HomeBorder
                    )


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    /*
                     * ===============================
                     * LOGOUT
                     * ===============================
                     */

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(18.dp)
                            )
                            .background(LogoutRed)
                            .clickable {
                                onLogoutClick()
                            }
                            .padding(
                                horizontal = 18.dp,
                                vertical = 15.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = HomeWhite,
                            modifier = Modifier.size(23.dp)
                        )


                        Spacer(
                            modifier = Modifier.width(14.dp)
                        )


                        Text(
                            text = "Logout",
                            color = HomeWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }


                /*
                 * =================================
                 * PUSH NAVIGATION ROW TO BOTTOM
                 * =================================
                 */

                Spacer(
                    modifier = Modifier.weight(1f)
                )


                /*
                 * =================================
                 * MAIN NAVIGATION ROW
                 * =================================
                 */

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(minHeight),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    /*
                     * HOME
                     */
                    HomeNavigationButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = HomeBlack
                            )
                        },
                        onClick = onHomeClick,
                        selected = isHomeSelected
                    )


                    /*
                     * GROUPS
                     */
                    HomeNavigationButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Groups",
                                tint = HomeBlack
                            )
                        },
                        onClick = onGroupsClick,
                        selected = isGroupSelected
                    )


                    /*
                     * PROFILE
                     */
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (isProfileSelected)
                                    HomeBlue
                                else
                                    Color.Transparent
                            )
                            .clickable {
                                onProfileClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        if (id?.photo_url == null) {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = HomeBlack,
                                modifier = Modifier.size(28.dp)
                            )

                        } else {

                            AsyncImage(
                                model = id.photo_url,
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}


/*
 * ============================================
 * COLLAPSED NAVIGATION BUTTON
 * ============================================
 */

@Composable
private fun HomeNavigationButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    selected: Boolean
) {

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                if (selected)
                    HomeBlue
                else
                    Color.Transparent
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        icon()
    }
}


/*
 * ============================================
 * EXPANDED MENU ITEM
 * ============================================
 */

@Composable
private fun ExpandedNavigationItem(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = HomeBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(HomeTile),
            contentAlignment = Alignment.Center
        ) {

            icon()
        }


        Spacer(
            modifier = Modifier.width(14.dp)
        )


        Text(
            text = title,
            color = HomeBlack,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )


        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = HomeBlack
        )
    }
}