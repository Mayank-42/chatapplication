package com.example.chatapplication.ui.Screen.Auth

import android.R.attr.password
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.AuthViewModelFactory
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.Viewmodel.loginVM
import com.example.chatapplication.Data.local.tables.userInfo




@Composable
fun UserInfo(navControl: NavController,viewMode: databaseVM,authVM: loginVM,onLoginSuccess: () -> Unit){
    var name by rememberSaveable{mutableStateOf("")}
    var username by rememberSaveable{mutableStateOf("")}
    Box(modifier=Modifier.fillMaxSize().background(Color.Black)){
        Column(){

        Spacer(modifier=Modifier.padding(top=70.dp))
        help("Enter the  Name",input= name, onWordsChange = {name=it})
        help("Enter the UserName",
            true,
            input=username,
            onWordsChange = {username=it},
            onButtonClick={
                viewMode.userinsert(userInfo(0,name,username))
                authVM.sigUp(authVM.email ,authVM.password,name,username){
                    succses->if(succses){
                   onLoginSuccess()
                  }
                }
            },
            )
        }

    }
}

@Composable
fun help(
         name:String,
         wantPrefix:Boolean=false,
//         TakingToHomePage:NavController,
         input:String,
         onWordsChange:(String)->Unit,
         onButtonClick:()->Unit={}
){
//    var input by rememberSaveable{mutableStateOf("")}
    Surface(modifier=Modifier.fillMaxWidth().height(90.dp).padding(top=15.dp),color=Color.White,
        shape= RoundedCornerShape(16.dp)){
        Box(modifier=Modifier.fillMaxWidth().padding(start = 19.dp),contentAlignment = Alignment.CenterStart){
                if(wantPrefix) {
                    TextField(
                        value = input,
                        onValueChange = { onWordsChange(it)},
                        placeholder = { Text(text = "$name") },
                        prefix = { Text(text = "@ ") },
                        keyboardOptions = KeyboardOptions(
                            imeAction=ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend={onButtonClick()}
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent

                        ),
                        modifier=Modifier.fillMaxWidth()
                    )
                }
            else{
                    TextField(
                        value = input,
                        onValueChange = { onWordsChange(it) },
                        placeholder = { Text(text = "$name") },
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent

                        ),
                        modifier=Modifier.fillMaxWidth()
                    )
                }

        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun showw(){
//    UserInfo()
//}