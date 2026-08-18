package com.example.chatapplication.ui.Screen.Main

import android.provider.CalendarContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.ModifierLocalProvider
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.R

@Composable
fun profileScreen(nav: NavController){
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            println("Selected image: $uri")
        }
    }
    Box(modifier = Modifier.background(Color.Black).fillMaxSize()){
        Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment =Alignment.CenterHorizontally) {
            Box() {
            Image(
                painter = painterResource(R.drawable.example),
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
//                    .padding(bottom = 60.dp)
                    .size(180.dp).clip(CircleShape)
                    .fillMaxSize()
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            IconButton(onClick = {
                imagePicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }, modifier = Modifier.align(Alignment.BottomEnd)) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier=Modifier.size(270.dp)
                )
            }
        }
            tile()
            tile()
            tile()

        }

    }
}
@Composable
fun tile(){
    Surface(modifier = Modifier.fillMaxWidth()
        .height(60.dp)
        .padding(start=10.dp,end=10.dp,top=10.dp)
        .clip(shape= RoundedCornerShape(10.dp))
    ){
        Box(contentAlignment = Alignment.Center) {
            Text(text = "text will share over here ")
        }
    }
}
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ShowProfile(){
//    profileScreen()
//}