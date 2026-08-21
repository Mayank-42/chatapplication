package com.example.chatapplication.ui.Screen.Main

import android.R.attr.text
import android.R.id.message
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.R

@Composable
fun SearchBarPage(navControl: NavController,userEsist: UserInfo){
    var userName by rememberSaveable { mutableStateOf("") }
    var isSearched by rememberSaveable{mutableStateOf(false)}

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

        Box(modifier= Modifier.fillMaxSize().background(Color.Black)){

                Column(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 40.dp)
                            .clip(shape = RoundedCornerShape(30.dp))
                            .background(Color.White)
                            .height(52.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.padding(start = 25.dp)
//                                    .clickable { navControl.navigate("Home") } this is good example to not use navigate evrywhere
                                    .clickable { navControl.popBackStack()}
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            TextField(
                                value = userName,
                                onValueChange = { userName = it;isSearched = false },
                                modifier = Modifier.focusRequester(focusRequester),
                                placeholder = {
                                    Text(text = "Search user by UserName ('')")
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = { userEsist.isExsist(userName);isSearched = true }
                                ),

                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    unfocusedTextColor = Color.Gray
                                )

                            )
                        }


                    }

                    val ans = userEsist.UserExsist
                    Spacer(modifier = Modifier.height(20.dp))
                    if(isSearched==true) {
                        if (ans?.isExsist == true) {

                            Surface(
                                modifier = Modifier.fillMaxWidth()
                                    .height(60.dp)
                                    .padding(start = 5.dp, end = 5.dp)
                                    .clickable { ans?.data?.id?.let { otherUserId ->

                                        userEsist.openConversation(
                                            otherUserId = otherUserId
                                        ) { conversationId ->

                                            navControl.navigate(
                                                "ChatScreen/$conversationId"
                                            )
                                        }
                                    } }
                                    .clip(shape = RoundedCornerShape(8.dp)),
                                color = Color.White
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.example),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .border(3.dp, Color.Transparent, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Column() {
                                        Text(text = ans.data?.name ?: userName, fontSize = 20.sp)
                                        Text(text = "designation")
                                    }
                                    Spacer(modifier = Modifier.width(80.dp))
                                    Text(
                                        text = """some message
                            | common for all""".trimMargin()
                                    )
                                }
                            }

                        } else {
                            Surface(
                                modifier = Modifier.fillMaxWidth()
                                    .height(60.dp)
                                    .padding(start = 5.dp, end = 5.dp)
                                    .clickable { navControl.navigate("ChatScreen") }
                                    .clip(shape = RoundedCornerShape(8.dp)),
                                color = Color.White
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.example),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .border(3.dp, Color.Transparent, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Column() {
                                        Text(text = "UnownUser", fontSize = 20.sp)
                                        Text(text = "designation")
                                    }
                                    Spacer(modifier = Modifier.width(80.dp))
                                    Text(
                                        text = """some message
                            | common for all""".trimMargin()
                                    )
                                }
                            }
                        }
                    }



            }
        }

}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ShowScreen(){
//    SearchBarPage()
//}