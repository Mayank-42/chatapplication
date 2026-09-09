package com.example.chatapplication.ui.Screen.Main

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.R
import kotlinx.coroutines.delay


// ================================================================
// SEARCH COLORS
// ================================================================

private val SearchBlack = Color(0xFF000000)
private val SearchWhite = Color(0xFFFFFFFF)
private val SearchBlue = Color(0xFF3B82F6)
private val SearchTile = Color(0xFF111111)
private val SearchMuted = Color(0xFF9CA3AF)
private val SearchBorder = Color(0xFF242424)


// ================================================================
// SEARCH PAGE
// ================================================================

@Composable
fun SearchBarPage(
    navControl: NavController,
    userEsist: UserInfo
) {

    var userName by rememberSaveable {
        mutableStateOf("")
    }

    var isSearched by rememberSaveable {
        mutableStateOf(false)
    }

    val focusRequester = remember {
        FocusRequester()
    }


    // ============================================================
    // AUTO FOCUS
    // ============================================================

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


    // ============================================================
    // LIVE SEARCH
    // ============================================================

    LaunchedEffect(userName) {

        // Clear previous results when search is empty
        if (userName.isBlank()) {
            isSearched = false
            return@LaunchedEffect
        }

        // Wait until user stops typing
        delay(300)

        if (userName.isNotBlank()) {

            userEsist.isExsist(userName)

            isSearched = true
        }
    }


    // ============================================================
    // SCREEN
    // ============================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SearchBlack)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
        ) {


            // ====================================================
            // TOP SEARCH BAR
            // ====================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 35.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // =================================================
                // BACK BUTTON
                // =================================================

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {
                            navControl.popBackStack()
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = SearchWhite,
                        modifier = Modifier.size(27.dp)
                    )
                }


                Spacer(
                    modifier = Modifier.width(8.dp)
                )


                // =================================================
                // SEARCH FIELD
                // =================================================

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    color = SearchTile,
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = SearchBorder
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = SearchBlue,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(24.dp)
                        )


                        // =================================================
                        // TEXT FIELD
                        // =================================================

                        TextField(
                            value = userName,

                            onValueChange = {
                                userName = it
                                isSearched = false
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),

                            placeholder = {
                                Text(
                                    text = "Search people...",
                                    color = SearchMuted,
                                    fontSize = 16.sp
                                )
                            },

                            singleLine = true,

                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),

                            keyboardActions = KeyboardActions(
                                onSearch = {

                                    if (userName.isNotBlank()) {

                                        userEsist.isExsist(userName)

                                        isSearched = true
                                    }
                                }
                            ),

                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,

                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,

                                focusedTextColor = SearchWhite,
                                unfocusedTextColor = SearchWhite,

                                cursorColor = SearchBlue
                            )
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(32.dp)
            )


            // ====================================================
            // SEARCH RESULTS TITLE
            // ====================================================

            Text(
                text = "SEARCH RESULTS",
                color = SearchWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // ====================================================
            // SEARCH RESULTS
            // ====================================================

            val ans = userEsist.UserExsist

            if (isSearched) {

                if (ans?.isExsist == true && !ans.data.isNullOrEmpty()) {

                    // =================================================
                    // MULTIPLE USERS
                    // =================================================

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),

                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            items = ans.data!!
                        ) { user ->

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(92.dp)
                                    .clickable {

                                        user.id?.let { otherUserId ->

                                            userEsist.openConversation(
                                                otherUserId = otherUserId
                                            ) { conversationId ->

                                                navControl.navigate(
                                                    "ChatScreen/$conversationId"
                                                )
                                            }
                                        }
                                    },

                                color = SearchTile,

                                shape = RoundedCornerShape(
                                    22.dp
                                ),

                                border =
                                    androidx.compose.foundation.BorderStroke(
                                        width = 1.dp,
                                        color = SearchBorder
                                    )
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            horizontal = 14.dp
                                        ),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {


                                    // =================================
                                    // PROFILE IMAGE
                                    // =================================

                                    ImageProfile(user.photo_url?:"")


                                    Spacer(
                                        modifier = Modifier.width(14.dp)
                                    )


                                    // =================================
                                    // USER INFORMATION
                                    // =================================

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text =
                                                user.name
                                                    ?: "Unknown User",

                                            color = SearchWhite,

                                            fontSize = 18.sp,

                                            fontWeight =
                                                FontWeight.Bold,

                                            maxLines = 1
                                        )


                                        Spacer(
                                            modifier =
                                                Modifier.height(4.dp)
                                        )


                                        Text(
                                            text =
                                                "@${user.username ?: ""}",

                                            color = SearchMuted,

                                            fontSize = 14.sp
                                        )
                                    }


                                    // =================================
                                    // CHAT BUTTON
                                    // =================================

                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(SearchBlue),

                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector =
                                                Icons.Default.Send,

                                            contentDescription =
                                                "Start chat",

                                            tint = SearchWhite,

                                            modifier =
                                                Modifier.size(23.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                } else {

                    // =================================================
                    // UNKNOWN USER
                    // =================================================

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(92.dp),

                        color = SearchTile,

                        shape = RoundedCornerShape(
                            22.dp
                        ),

                        border =
                            androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = SearchBorder
                            )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    horizontal = 14.dp
                                ),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {


                            // =================================
                            // UNKNOWN USER AVATAR
                            // =================================

                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(CircleShape)
                                    .background(SearchBlack)
                                    .border(
                                        width = 1.dp,
                                        color = SearchBorder,
                                        shape = CircleShape
                                    ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(
                                    text = "?",

                                    color = SearchMuted,

                                    fontSize = 24.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }


                            Spacer(
                                modifier =
                                    Modifier.width(14.dp)
                            )


                            Column {

                                Text(
                                    text = "User not found",

                                    color = SearchWhite,

                                    fontSize = 17.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )


                                Spacer(
                                    modifier =
                                        Modifier.height(4.dp)
                                )


                                Text(
                                    text =
                                        "No account matches \"$userName\"",

                                    color = SearchMuted,

                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ================================================================
// PROFILE IMAGE
// ================================================================

@Composable
private fun ImageProfile(url:String?) {

    AsyncImage(
        model=url,
        contentDescription = "Profile image",
        contentScale = ContentScale .Crop,
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .border(
                width = 1.5.dp,
                color = SearchBorder,
                shape = CircleShape
            )
    )
}