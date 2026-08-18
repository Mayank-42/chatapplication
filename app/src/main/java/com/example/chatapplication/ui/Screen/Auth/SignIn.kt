package com.example.chatapplication.ui.Screen.Auth

import android.R.attr.password
import android.R.attr.text
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
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.Viewmodel.loginVM
import com.example.chatapplication.Data.local.tables.userLoginInfo

@Composable
fun ShowSignIn(navControler: NavController,viewMode : databaseVM,authVM: loginVM,onLoginSuccess: () -> Unit){
    var email by rememberSaveable{mutableStateOf("")}
    var pass by rememberSaveable{mutableStateOf("")}
    var showErrorBox by rememberSaveable { mutableStateOf(false)}
    Box(modifier= Modifier.fillMaxSize()
                            .background(color=Color.Black),
        contentAlignment= Alignment.Center

     ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
        Icon(imageVector = Icons.Default.Face,
            contentDescription = null,
            tint=Color.White,
            modifier=Modifier
                .fillMaxWidth()
                .height(100.dp)
//                .border(1.dp, MaterialTheme.colorScheme.primary,CircleShape),

            )
//            Spacer(modifier=Modifier.height(30.dp))
            Text(text="YOKOSU",color=Color.White,fontSize=36.sp, fontWeight = FontWeight.SemiBold, modifier=Modifier.padding(25.dp))
            surface(navControl=navControler,task="Enter your Email", viewMode = viewMode,words=email, onWordsChange = {email=it})
            Spacer(modifier=Modifier.height(30.dp))
            surface(navControl=navControler,task="Enter your pasword ", viewMode = viewMode,words=pass, onWordsChange = {pass=it})
            Spacer(modifier=Modifier.height(30.dp))
            surface(navControl=navControler,
                80, "Sign In",
                false,
                viewMode = viewMode,
                "",{},
                onButtonClick = {

                    authVM.login(email, pass){ response,userId ->

                        if (response.isSuccessful && userId != null) {
                            onLoginSuccess()
                        } else {

                            showErrorBox = true
                        }
                    }

//                    if (authVM.userLoggedIn) {
//                        viewMode.logininsert(
//                            userLoginInfo(
//                                Email = email,
//                                password = pass
//                            )
//                        )
//                        navControler.navigate("Home")
//
//                    }
//                    else{
//                        showErrorBox=true
//                    }
                })
                    if(showErrorBox){
                        AlertDialog(
                            onDismissRequest = {
                                showErrorBox = false
                            },

                            title = {
                                Text(text = "Wrong")
                            },

                            text = {
                                Text(text = "Something is wrong")
                            },

                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showErrorBox = false
                                    }
                                ) {
                                    Text(text = "OK")
                                }
                            }
                        )
//                        AlertDialog(
//                            onDismissRequest ={ showErrorBox=false},
//                                    title={Text(text="wrong")},
//                            text={Text(text="Something is wrong")},
//                            confirmButton {
//                                TextButton(onClick={showErrorBox=true}) {Text(text="ok") }
//                            },
//                        )
                    }

            Row {
                Text(
                    text = "Not a User?",
                    color = Color.White,
                    modifier = Modifier.padding(top=15.dp)
                )
                TextButton(onClick = {navControler.navigate("register")})
                {Text(text="Register",
                    fontStyle = FontStyle.Italic,

                ) }
            }
        }

    }

}
@Composable
fun surface(navControl:NavController ,
            size:Int=60,
            task:String="Enter Text here",
            wantTextField:Boolean=true,
            viewMode : databaseVM,
            words :String,
            onWordsChange:(String)->Unit,
            onButtonClick: () -> Unit = {}
) {

    Surface(
        modifier = Modifier.fillMaxWidth().padding(start = 35.dp,   end = 35.dp)
            .height(size.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)

    ) {
        if(wantTextField) {
            OutlinedTextField(
                value = words,
                onValueChange = { onWordsChange(it)},
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

            //Box(modifier=Modifier.fillMaxSize().clickable{navControl.navigate("Home");viewMode.logininsert(userLoginInfo(Email=words, password = words))}, contentAlignment = Alignment.Center){
         Box(modifier=Modifier.fillMaxSize().clickable{onButtonClick()}, contentAlignment = Alignment.Center){
            Text(text="$task", fontWeight = FontWeight.Bold, fontSize = 30.sp )

            }

        }
    }

}


//@Preview(showSystemUi = true,uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Composable
//fun show(){
//    ChatApplicationTheme {   // <-- Your app's theme name
//        ShowSignIn(navController : NavController)
//    }
//}
