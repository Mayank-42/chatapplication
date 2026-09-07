package com.example.chatapplication.ui.Screen.Main

import android.R.color.white
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingPage(){
    Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment=Alignment.Center){
        Box(modifier=Modifier.fillMaxWidth().padding(40.dp).background(Color.Gray)) {
            Column(modifier=Modifier.fillMaxWidth().padding(30.dp), horizontalAlignment =Alignment.CenterHorizontally) {
                Text(
                    text = "Curentlly in proress",
                    color = Color.White,
                    fontSize=24.sp
                )
                Icon(
                    imageVector = Icons.Default.Construction,
                    contentDescription = null,
                    tint=Color.DarkGray,
                    modifier=Modifier.size(80.dp)
                )
            }
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun show(){
    SettingPage()
}

