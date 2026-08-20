package com.example.chatapplication.ui.Screen.GroupChat

import android.R.attr.text
import android.service.autofill.Validators.or
import android.text.ShowSecretsSetting
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.chatapplication.R
import com.example.chatapplication.ui.Screen.Auth.surface
import io.ktor.websocket.Frame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupName(nav: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                ),
                title = {Text(text="Create Group")},
                navigationIcon = {
                    IconButton(onClick = {nav.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )

                    }
                }
            )
        }
    ) {paddingValues ->
    Box(modifier=Modifier.fillMaxSize().background(Color.Black).padding(paddingValues)) {
        Box() {
            Column() {
                Row() {
                    Image(
                        painter = painterResource(R.drawable.example),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Green,CircleShape),

                        )
                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = {
                                Text(text = "Enter grop name ",color=Color.White)
                            },
                            colors= TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.DarkGray,
                                unfocusedTextColor = Color.Black,
                                focusedTextColor = Color.Black
                            )
                        )
                    }
                }
                Spacer(modifier=Modifier.height(20.dp))
                    Surface(modifier = Modifier.fillMaxWidth().clip(shape=RoundedCornerShape(10.dp))){
                        TextField(
                            value="",
                            onValueChange={},
                            placeholder = {
                                Text(text="Descripbe the pupose of This group ",color=Color.White)
                            },
                            colors= TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.DarkGray,
                                unfocusedTextColor = Color.Black,
                                focusedTextColor = Color.Black
                            )
                        )
                    }
                Box(modifier=Modifier.fillMaxSize().padding(end=30.dp), contentAlignment = Alignment.TopEnd){
                Button(onClick={nav.popBackStack("GroupPage",inclusive = false)}){ Text(text="Create")}
                }
        }
    }
    }
}
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun showScreen(){
//    GroupName()
//}
