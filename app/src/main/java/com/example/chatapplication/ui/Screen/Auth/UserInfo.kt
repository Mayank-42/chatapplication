package com.example.chatapplication.ui.Screen.Auth

import android.R.attr.contentDescription
import android.R.attr.password
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

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
    var role by rememberSaveable { mutableStateOf("Choose your role")}
        var showAlert by rememberSaveable{ mutableStateOf(false) }
        var alertMessage by rememberSaveable { mutableStateOf("") }

    Box(modifier=Modifier.fillMaxSize().background(Color.Black)){
        Column(){

        Spacer(modifier=Modifier.padding(top=70.dp))
        help("Enter the  Name",input= name, onWordsChange = {name=it})
            dropBox(role,onRoleChange = {
                role = it
            })
            help(
                "Enter the UserName",
                true,
                input = username,
                onWordsChange = { username = it },
                onButtonClick = {

                    if (username.isBlank()) {
                        alertMessage = "Please enter a username"
                        showAlert = true
                    } else if (role.isBlank() || role == "Choose your role") {

                        alertMessage = "Please choose a role"
                        showAlert = true

                    } else {
                        viewMode.userinsert(
                            userInfo(0, name, username)
                        )
                        authVM.sigUp(
                            authVM.email,
                            authVM.password,
                            name,
                            username,
                            role
                        ) { success ->
                            if (success) {
                                onLoginSuccess()
                            }
                        }
                    }
                }
            )
            if (showAlert) {
                AlertDialog(
                    onDismissRequest = {
                        showAlert = false
                    },

                    title = { Text(text = "Something is missing") },

                    text = { Text(text = alertMessage)  },

                    confirmButton = {
                        TextButton(
                            onClick = {
                                showAlert = false
                            }
                        ) {
                            Text(text = "OK")
                        }
                    }
                )
            }

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
@Composable
fun dropBox(
    role: String,
    onRoleChange: (String) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {

        // Main dropdown tile
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            color = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 19.dp)
                    .clickable {
                        expanded = !expanded
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = role,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select role",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        // Dropdown options
        if (expanded) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {

                jobRole("Principal Engineer") {
                    onRoleChange(it)
                    expanded = false
                }

                jobRole("Manager") {
                    onRoleChange(it)
                    expanded = false
                }

                jobRole("SDE") {
                    onRoleChange(it)
                    expanded = false
                }

                jobRole("QA") {
                    onRoleChange(it)
                    expanded = false
                }

                jobRole("Product Manager") {
                    onRoleChange(it)
                    expanded = false
                }

                jobRole("Intern") {
                    onRoleChange(it)
                    expanded = false
                }
            }
        }
    }
}
@Composable
fun jobRole(
    role: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                onClick(role)
            }
            .padding(horizontal = 19.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = role,
            color = Color.Black
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun showw() {
//    dropBox()
//}