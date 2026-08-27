package com.example.chatapplication.ui.Screen.GroupChat

import android.R.attr.checked
import android.R.attr.text
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.local.tables.userInfo
import com.example.chatapplication.R
import com.example.chatapplication.ui.Screen.Auth.surface
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatSearch(nav: NavController,infoo: UserInfo,save: GroupChatVM) {

    var username by rememberSaveable { mutableStateOf("") }
    var showIcon by remember { mutableStateOf(false) }



//    var checked by remember { mutableStateOf(false) }
// var  selectedId=rememberSaveable {mutableStateOf<List<String>>(emptyList())}
    var selectedId by rememberSaveable {
        mutableStateOf(emptyList<String>())
    }
    for(i in selectedId){
        println("the id sar {$i}")
    }
    Scaffold(

        topBar={
            TopAppBar(
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                ),
                title={Text(text="Add Group Member")}
            )
        }
    ) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().background(Color.Black).padding(paddingValues)) {

        Column() {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { nav.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent)
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,

                            )
                    }
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = { Text(text = "Add Member") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )

                    )
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Your Colleague",
                    color = Color.Gray,
                    fontSize = 15.sp,
                )
            }
            infoo.companyUsers
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            officeCoWorker(infoo,save.selectedUserId, onChecked = {id,checked ->
                 if(checked)  save.addUser(id) else  save.removeUser(id);
                showIcon=true})
//            infoo
        }
        if(showIcon) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(end = 35.dp, bottom = 60.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(onClick = { nav.navigate("GroupName") }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }

    }
}
}

@Composable
fun officeCoWorker(userinfoo: UserInfo, list:List<String>,onChecked:(String,Boolean)->Unit) {
    LazyColumn() {
        userinfoo.sortedCompanyUsers.forEach { (priority, users) ->

            item {
                Text(
                    text = "ROLE HERE",
                    color = Color.Gray,
                    modifier = Modifier.padding(10.dp)
                )
            }
            items(users) { ele ->
                var checked by remember { mutableStateOf(false) }
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp, 10.dp).height(80.dp)
                        .clickable {/*navControl.navigate("chatScreen/${ele.id}")*/ },
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
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = ele.name,
//                                text ="name",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "this the part which will shouw beloww name",
                                fontSize = 16.sp
                            )
                        }
                        Checkbox(
                            checked = ele.id in list,
                            onCheckedChange = { checked -> onChecked(ele.id, checked) }
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxSize().padding(end = 40.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {

//                    Text(text = ele.created_at.toString(), fontStyle = FontStyle.Italic)
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = false)
//@Composable
//fun show(){
//    officeCoWorker()
//}
