package com.example.chatapplication.ui.Screen.Auth

import android.R.attr.fontStyle
import android.graphics.Color.blue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face6
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.loginVM

@Composable
fun ShowShinUp(navControler: NavController,authVM: loginVM){
    var email by rememberSaveable{mutableStateOf("")}
    var password by rememberSaveable{mutableStateOf("")}
    var pass by rememberSaveable{mutableStateOf("")}
    var showAlertBox by rememberSaveable{mutableStateOf(false)}
    Box(modifier=Modifier.fillMaxSize().background(Color.Black),contentAlignment= Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(
                imageVector= Icons.Default.Face6,
                contentDescription=null,
                tint=Color.White,
                modifier=Modifier.fillMaxWidth().height(100.dp)
            )
            Text(text="Set up your Acount",color=Color.White,fontSize=36.sp, fontWeight = FontWeight.SemiBold,modifier=Modifier.padding(25.dp))

            surf(navControl=navControler,task = "Enter Your Email",words=email, onWordChange = {email=it})
            Spacer(modifier=Modifier.height(30.dp))
            surf(navControl=navControler,task = "Enter Your Pasword",words=password, onWordChange = {password=it})
            Spacer(modifier=Modifier.height(30.dp))
            surf(navControl=navControler,task="Confirm your pasword",words=pass, onWordChange = {pass=it})
            Spacer(modifier=Modifier.height(30.dp))
            surf(navControl=navControler,
                size=80,
                task="Sign Up",
                wantTextField=false,
                "",{},
                onButtonClick = {
                    if(password==pass){
//                        authVM.sigUp(email=email,password=password)
                        authVM.email=email
                        authVM.password=password

                    navControler.navigate("UserInfo")
                    }
                    else{
                        showAlertBox=true
                    }
                })
            if (showAlertBox) {

                AlertDialog(
                    onDismissRequest = {
                        showAlertBox = false
                    },

                    title = {
                        Text(text="Password Error")
                    },

                    text = {
                        Text(text="Passwords do not match.")
                    },

                    confirmButton = {
                        TextButton(
                            onClick = {
                                showAlertBox = false
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }


            Row {
                Text(
                    text = "Already a User?",
                    color = Color.White,
                    modifier = Modifier.padding(top=16.dp)
                )
                TextButton(onClick = {navControler.navigate("SignIn")})
                {Text(text="Sign In",
                    color=Color(0xFF3B82F6),
                    fontStyle = FontStyle.Italic
                ) }
            }
        }
    }
}
@Composable
fun surf(navControl:NavController ,
         size:Int=60,
         task:String="Enter Text here",
         wantTextField:Boolean=true,
         words:String,
         onWordChange:(String)->Unit,onButtonClick:()-> Unit = {}
) {

    Surface(
        modifier = Modifier.fillMaxWidth().padding(start = 35.dp, end = 35.dp)
            .height(size.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)

    ) {
        if(wantTextField) {
            OutlinedTextField(
                value = words,
                onValueChange = { onWordChange(it) },
                placeholder = { Text(text = "$task", fontSize = 20.sp) },
                colors = TextFieldDefaults.colors(

                    // Removes the gray background
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    // Removes the bottom indicator line
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,

                    // Text colors
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,

                    // Placeholder color
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,

                    // Cursor
                    cursorColor = Color.Black
                )
            )
        }
        else{
            Box(modifier=Modifier.fillMaxSize().clickable{onButtonClick()}, contentAlignment = Alignment.Center){
                Text(text="$task", fontWeight = FontWeight.Bold, fontSize = 30.sp)

            }

        }
    }

}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun shw(){
//    ShowShinUp()
//}