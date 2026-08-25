    package com.example.chatapplication.ui.Screen.Main



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
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.AccountBox
    import androidx.compose.material.icons.filled.Groups
    import androidx.compose.material.icons.filled.Logout
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material3.BottomAppBar
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.NavigationBar
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TextFieldDefaults
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.produceState
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.draw.scale
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavController
    import com.example.chatapplication.Data.local.TokenManager
    import com.example.chatapplication.Data.Viewmodel.UserInfo
    import com.example.chatapplication.Data.Viewmodel.convoVM
    import com.example.chatapplication.Data.local.tables.userInfo
    import com.example.chatapplication.R
    import kotlinx.coroutines.launch

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(navControl: NavController,
                   tokenManager: TokenManager,
                   userinfoo: UserInfo,
                   onLoginSuccess: () -> Unit,
                   conversationInfo: convoVM
    ){


        val scope=rememberCoroutineScope()
    //    var userName by rememberSaveable { mutableStateOf("") }
        LaunchedEffect(Unit) {
            userinfoo.getinfo()
        }

        var id by rememberSaveable {mutableStateOf("") }
        LaunchedEffect(Unit) { id=tokenManager.getUserId()?:""}

        val conversations by conversationInfo.gettingConvoInfo.collectAsState(initial = emptyList())

            Scaffold(topBar = {
                    TopAppBar(

                        title = { Text(text = "mai kyaa mewo") },

                    modifier= Modifier.clip(shape=RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                    )

                }, bottomBar = {
                NavigationBar(
                    modifier = Modifier/*.padding(bottom= 20.dp,start=10.dp,end=10.dp)*/.clip(
                        RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
                ) {
                    Row(modifier=Modifier.fillMaxWidth()
                        .padding(start=5.dp, end = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
    //                    horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier=Modifier.width(40.dp))
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        modifier = Modifier
    //                        .padding(end=170.dp)
                            .size(40.dp)
                            .clickable {
                                navControl.navigate("GroupPage")
    //                            tokenManager.clearTokens()
    //                            onLoginSuccess()

                        }
                    )
                    Spacer(modifier=Modifier.width(100.dp))
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).clickable {
                                    scope.launch {
                                        tokenManager.clearTokens()
    //                                    navControl.navigate("SignIn")
                                        onLoginSuccess()
                                    }
                                }
                            )

                        Spacer(modifier=Modifier.width(100.dp))
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp).clickable {
                                scope.launch {
                                    if (id.length != 0) {
                                        navControl.navigate("profileScreen/$id")
                                    }
                                }
                            }
                        )
    //
               }

            }

                }){paddingValues ->
           Box(modifier=Modifier.fillMaxSize().background(Color.Black).padding(paddingValues)) {
               Column() {
               Surface(
                   color=Color.White,
                   modifier = Modifier.fillMaxWidth()
                   .padding(start=10.dp,end=10.dp,top=4.dp)
                   .clip(shape=RoundedCornerShape(30.dp))
                   .background(Color.White)
                       .height(52.dp)
                   .clickable{ navControl.navigate("SearchBarPage")}
               ) {
                           Row(modifier = Modifier.fillMaxSize().padding(start=10.dp), verticalAlignment = Alignment.CenterVertically){
                               Icon(
                                   imageVector = Icons.Default.Search,
                                   contentDescription = null,
                                   tint =Color.Black
                               )
                               Spacer(modifier = Modifier.width(15.dp))
                               Text(text="Search user by UserName ('')")
                           }
                       }



                   LazyColumn() {
    //                   items(userinfoo.userInfo) { ele ->
                           items(conversations) { ele ->
                               println(
                                   "HOME UI: conversation=${ele.conversationId}, " +
                                           "SERVER UNREAD=${ele.unread_count}"
                               )
                               val unreadCount by produceState(initialValue = ele.unread_count, key1 = ele.conversationId, key2 = id
                               ) {
                                   if (id.isNotBlank()) {
                                       conversationInfo.getUnreadCount(conversationId = ele.conversationId, myUserId = id)
                                           .collect {
                                               println(
                                                   "LOCAL UNREAD: conversation=${ele.conversationId}, count=$it"
                                               )
                                               value = it
                                           }
                                   }
                               }
                           Surface(
                               modifier = Modifier.fillMaxWidth().padding(start = 10.dp, 10.dp).height(80.dp)
                                   .clickable {navControl.navigate("chatScreen/${ele.conversationId}")},
                               color = Color.White,
                               shape = RoundedCornerShape(35)

                           ) {
                               Row(
                                   modifier = Modifier.fillMaxSize(),
                                   verticalAlignment = Alignment.CenterVertically
                               ) {
                                   Image(
                                       painter = painterResource(R.drawable.example),
                                       contentDescription = "profile image",
                                       modifier = Modifier.size(50.dp).clip(CircleShape)
                                           .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)

                                   )
                                   Box(contentAlignment = Alignment.CenterStart) {
                                       Column() {
                                           Text(
                                               text =ele.name?:"",
                                               fontSize = 25.sp,
                                               fontWeight = FontWeight.SemiBold
                                           )
                                           Text(
                                               text = ele.lastMessage?:"",
                                               fontSize = 16.sp
                                           )
                                       }
                                   }
                               }
                               Box(
                                   modifier = Modifier.fillMaxSize().padding(end = 40.dp),
                                   contentAlignment = Alignment.TopEnd
                               ) {

                                   Text(text = ele.lastTime?:"", fontStyle = FontStyle.Italic)
                               }
                               if(unreadCount!=0)
                               Box(
                                   modifier = Modifier.fillMaxSize().padding(end = 40.dp),
                                   contentAlignment = Alignment.CenterEnd
                               ) {
                                   Text(text=unreadCount.toString(),
                                       fontStyle = FontStyle.Italic,
                                       fontWeight = FontWeight.SemiBold,
                                       modifier=Modifier
    //                    .size(20.dp)
                                           .clip(CircleShape)
                                           .border(1.dp,Color.Green,CircleShape)
                                           .background(Color.Red)
                                           .padding(5.dp)
                                   )
                               }
                           }
                       }
                   }


           }
            }

        }
    }


    //@Preview
    //@Composable
    //fun show(){
    //    HomeScreen()
    //}
