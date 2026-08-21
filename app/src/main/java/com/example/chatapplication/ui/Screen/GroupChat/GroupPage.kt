package com.example.chatapplication.ui.Screen.GroupChat

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.R
import com.example.chatapplication.ui.Screen.Main.SearchBarPage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupPage(nav: NavController,save: GroupChatVM){
    val groups by save.gettingGroupinfo.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                    ),
                navigationIcon = {
                    IconButton(onClick = {nav.popBackStack()}) {
                    Icon(
                        imageVector=Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                    }
                },
                title = {Text(text="Group Chat")},
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }


                },

                modifier=Modifier.fillMaxWidth().background(Color.Black)

            )

        }

    ) {paddingValues ->
        Box(modifier=Modifier.padding(paddingValues).fillMaxSize().background(Color.Black)){
            Column(modifier = Modifier.fillMaxSize()){
                LazyColumn() {
                    items(groups) { ele ->

                GropChatTile(ele.GropName,nav)
                    }
                }
            Box(modifier=Modifier.fillMaxSize()
                .padding(end=35.dp, bottom = 60.dp),
                contentAlignment = Alignment.BottomEnd
            ){
                FloatingActionButton(onClick = {nav.navigate("GropChatSearch")}){
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = null,
                        tint=Color.Black
                    )
                }
            }

            }
        }

    }
}
@Composable
fun GropChatTile(Gname:String,nav: NavController){
    Surface(modifier=Modifier.fillMaxWidth().height(70.dp).clickable(onClick = {nav.navigate("GroupChatScreen")})){
        Row(verticalAlignment = Alignment.CenterVertically){
            Image(
                painter= painterResource(R.drawable.example),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp,Color.White,CircleShape)

            )
            Column(modifier = Modifier.padding(start=10.dp)){
             Text(text=Gname, fontSize = 25.sp)
                Text(text="this is goup is created by somone",modifier=Modifier.padding(start=10.dp))
            }

        }
            Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
            Text(text="date/date/date",modifier=Modifier.padding(start=50.dp))
            }
    }

}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun showScrenn(){
//    GroupPage()
//}
