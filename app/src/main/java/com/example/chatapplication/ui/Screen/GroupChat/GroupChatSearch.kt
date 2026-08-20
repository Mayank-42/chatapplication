package com.example.chatapplication.ui.Screen.GroupChat

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.chatapplication.R
import com.example.chatapplication.ui.Screen.Auth.surface

@Composable
fun GroupChatSearch(nav: NavController,infoo: UserInfo){
    var username by rememberSaveable { mutableStateOf("") }
    Box(modifier=Modifier.fillMaxSize().background(Color.Black)){
        Column(){
        Surface(modifier=Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(50.dp)
            .clip(shape= RoundedCornerShape(20.dp))
        ){
            Row(modifier=Modifier.fillMaxWidth()){
             IconButton(onClick={nav.popBackStack()},
                 colors= IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent)
             ) {

            Icon(
                imageVector=Icons.Default.ArrowBack,
                contentDescription = null,

            )
             }
                TextField(
                    value=username,
                    onValueChange = {username=it},
                    placeholder = {Text(text="Add Member")},
                    modifier=Modifier.weight(1f),
                    colors= TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )

                )
            }
         }
            Box(modifier=Modifier.fillMaxWidth()){
            Text(
                text="Your Colleague",
                color=Color.Gray,
                fontSize = 15.sp,
            )
            }
                HorizontalDivider(
                    modifier=Modifier.fillMaxWidth()
                )
            Spacer(modifier=Modifier.height(20.dp))

            officeCoWorker(infoo)
        }

    }
}
@Composable
fun officeCoWorker(userinfoo: UserInfo){
    LazyColumn() {
        items(userinfoo.userInfo) { ele ->
            Surface(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, 10.dp).height(80.dp)
                    .clickable {/*navControl.navigate("chatScreen/${ele.id}")*/},
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
                                text =ele.name,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "this the part which will shouw beloww name",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize().padding(end = 40.dp),
                    contentAlignment = Alignment.TopEnd
                ) {

                    Text(text = ele.created_at.toString(), fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun show(){
//    GroupChatSearch()
//}
