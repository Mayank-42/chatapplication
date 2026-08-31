package com.example.chatapplication.ui.Screen.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.Viewmodel.loginVM

@Composable
fun ShowSignIn(
    navControler: NavController,
    viewMode: databaseVM,
    authVM: loginVM,
    onLoginSuccess: (String) -> Unit
) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var pass by rememberSaveable {
        mutableStateOf("")
    }

    var showErrorBox by rememberSaveable {
        mutableStateOf(false)
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val blue = Color(0xFF3B82F6)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            // App name
            Text(
                text = "YOKOSU",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(25.dp)
            )

            // =========================
            // EMAIL
            // =========================

            AuthTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = "Enter your Email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color.Gray
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // =========================
            // PASSWORD
            // =========================

            AuthTextField(
                value = pass,
                onValueChange = {
                    pass = it
                },
                placeholder = "Enter your password",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color.Gray
                    )
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {

                    TextButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Text(
                            text = if (passwordVisible) {
                                "HIDE"
                            } else {
                                "SHOW"
                            },
                            color = blue,
                            fontSize = 12.sp
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            // =========================
            // SIGN IN BUTTON
            // =========================

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 35.dp,
                        end = 35.dp
                    )
                    .height(80.dp),
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {

                            // YOUR EXISTING LOGIN LOGIC
                            authVM.login(email, pass) {
                                    response, userId ->

                                if (
                                    response.isSuccessful &&
                                    userId != null
                                ) {

                                    onLoginSuccess(userId)

                                } else {

                                    showErrorBox = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                }
            }

            // =========================
            // ERROR DIALOG
            // =========================

            if (showErrorBox) {

                AlertDialog(
                    onDismissRequest = {
                        showErrorBox = false
                    },

                    title = {
                        Text(
                            text = "Wrong"
                        )
                    },

                    text = {
                        Text(
                            text = "Something is wrong"
                        )
                    },

                    confirmButton = {

                        TextButton(
                            onClick = {
                                showErrorBox = false
                            }
                        ) {

                            Text(
                                text = "OK"
                            )
                        }
                    }
                )
            }

            // =========================
            // REGISTER
            // =========================

            Row {

                Text(
                    text = "Not a User?",
                    color = Color.White,
                    modifier = Modifier.padding(top = 15.dp)
                )

                TextButton(
                    onClick = {
                        navControler.navigate("register")
                    }
                ) {

                    Text(
                        text = "Register",
                        color = blue,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}


// =====================================================
// REUSABLE AUTH TEXT FIELD
// =====================================================

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation =
        VisualTransformation.None
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 35.dp,
                end = 35.dp
            )
            .height(65.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {

        OutlinedTextField(
            value = value,

            onValueChange = {
                onValueChange(it)
            },

            modifier = Modifier.fillMaxSize(),

            singleLine = true,

            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            },

            leadingIcon = leadingIcon,

            trailingIcon = trailingIcon,

            visualTransformation = visualTransformation,

            colors = TextFieldDefaults.colors(

                // Background
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,

                // Remove border
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,

                // Text
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                // Placeholder
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,

                // Cursor
                cursorColor = Color(0xFF3B82F6)
            )
        )


    }
}

