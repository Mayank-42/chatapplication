package com.example.chatapplication.ui.Screen.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face6
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.chatapplication.Data.Viewmodel.loginVM
import kotlinx.coroutines.delay


@Composable
fun ShowShinUp(
    navControler: NavController,
    authVM: loginVM
) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var pass by rememberSaveable {
        mutableStateOf("")
    }

    // Message displayed in the top popup
    var popupMessage by remember {
        mutableStateOf<String?>(null)
    }

    // Scroll state for keyboard
    val scrollState = rememberScrollState()
        //valid character
    val passwordPattern = Regex("^[A-Za-z0-9!@#\\$%^&*()_+\\-=\\[\\]{};':\"|<>?,./`~]+$")
    // --------------------------------------------------
    // REMOVE POPUP AFTER 2000 MILLISECONDS
    // --------------------------------------------------
    LaunchedEffect(popupMessage) {
        if (popupMessage != null) {
            delay(2000)
            popupMessage = null
        }
    }
    // --------------------------------------------------
    // MAIN SCREEN
    // --------------------------------------------------
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // ==================================================
        // SCROLLABLE SIGNUP CONTENT
        // ==================================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(
                    top = 40.dp,
                    bottom = 30.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --------------------------------------------------
            // LOGO
            // --------------------------------------------------
            Icon(
                imageVector = Icons.Default.Face6,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            )
            // --------------------------------------------------
            // TITLE
            // --------------------------------------------------
            Text(
                text = "Set up your Account",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    top = 15.dp,
                    bottom = 30.dp
                )
            )
            // ==================================================
            // EMAIL
            // ==================================================
            signupTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = "Enter your Email",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            // ==================================================
            // PASSWORD
            // ==================================================
            signupTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = "Enter your Password",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            // ==================================================
            // CONFIRM PASSWORD
            // ==================================================

            signupTextField(
                value = pass,
                onValueChange = {
                    pass = it
                },
                placeholder = "Confirm your Password",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirm Password"
                    )
                }
            )
            Spacer(modifier = Modifier.height(25.dp))
            // ==================================================
            // CONTINUE BUTTON
            // ==================================================

            Surface(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 35.dp,
                        end = 35.dp
                    )
                    .height(70.dp),

                color = Color.White,

                shape = RoundedCornerShape(16.dp)
            ) {

                Box(

                    modifier = Modifier.fillMaxSize(),

                    contentAlignment = Alignment.Center
                ) {

                    TextButton(

                        onClick = {

                            // ------------------------------------------
                            // 1. CHECK EMPTY FIELDS
                            // ------------------------------------------

                            if (
                                email.isBlank() ||
                                password.isBlank() ||
                                pass.isBlank()
                            ) {
                                popupMessage = "Missing credentials"
                            }
                            // ------------------------------------------
                            // 2. CHECK EMAIL FORMAT
                            // ------------------------------------------
                            else if (!email.contains("@")) {
                                popupMessage = "Wrong format"
                            }
                            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
                                popupMessage="Invalid email id "
                            }
                            // ------------------------------------------
                            // 3. CHECK PASSWORD LENGTH
                            // ------------------------------------------
                            else if (password.length < 6) {
                                popupMessage = "Password should be at least 6 characters"
                            }
                                else if(password.length>12){
                                    popupMessage="reduce the ${password.length-12} charecter for valid password"
                            }
                            // ------------------------------------------
                            // 4. CHECK PASSWORD MATCH
                            // ------------------------------------------
                            else if (password != pass) {
                                popupMessage = "Password mismatch"
                            }
                             else if(!passwordPattern.matches(password)){
                                popupMessage = "Password carry invalid character"
                            }
                            // ------------------------------------------
                            // 5. EVERYTHING IS VALID
                            // ------------------------------------------
                            else {
                                authVM.email = email
                                authVM.password = password
                                navControler.navigate("UserInfo")
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Continue",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(10.dp))
            // ==================================================
            // SIGN IN
            // ==================================================
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Already a User?",
                    color = Color.White
                )


                TextButton(
                    onClick = {
                        navControler.popBackStack()
                    }
                ) {

                    Text(
                        text = "Sign In",
                        color = Color(0xFF3B82F6),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }


        // ==================================================
        // FIXED TOP POPUP
        // ==================================================

        if (popupMessage != null) {

            Surface(

                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(
                        top = 40.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),

                color = Color.White,

                shape = RoundedCornerShape(14.dp),

                shadowElevation = 8.dp
            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),

                    verticalAlignment = Alignment.CenterVertically
                ) {


                    // ------------------------------------------
                    // WARNING ICON
                    // ------------------------------------------

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color.Red,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    // ------------------------------------------
                    // ERROR MESSAGE
                    // ------------------------------------------

                    Text(
                        text = popupMessage ?: "",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


// ==========================================================
// REUSABLE SIGNUP TEXT FIELD
// ==========================================================

@Composable
fun signupTextField(

    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: @Composable () -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 35.dp,
                end = 35.dp
            ),

        singleLine = true,

        placeholder = {

            Text(
                text = placeholder,
                fontSize = 18.sp
            )
        },

        leadingIcon = icon,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(

            // ------------------------------------------
            // CONTAINER
            // ------------------------------------------

            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            // ------------------------------------------
            // TEXT
            // ------------------------------------------
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            // ------------------------------------------
            // PLACEHOLDER
            // ------------------------------------------
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray,
            // ------------------------------------------
            // ICON
            // ------------------------------------------
            focusedLeadingIconColor = Color.Black,
            unfocusedLeadingIconColor = Color.Gray,
            // ------------------------------------------
            // CURSOR
            // ------------------------------------------
            cursorColor = Color.Black,
            // ------------------------------------------
            // BORDER
            // ------------------------------------------
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Transparent
        )
    )
}