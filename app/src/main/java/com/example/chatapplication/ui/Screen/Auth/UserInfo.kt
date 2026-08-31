package com.example.chatapplication.ui.Screen.Auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face6
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Person

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.Viewmodel.loginVM
import com.example.chatapplication.Data.local.tables.userInfo

import kotlinx.coroutines.delay


@Composable
fun UserInfo(
    navControl: NavController,
    viewMode: databaseVM,
    authVM: loginVM,
    onLoginSuccess: () -> Unit
) {

    var name by rememberSaveable {
        mutableStateOf("")
    }

    var username by rememberSaveable {
        mutableStateOf("")
    }

    var role by rememberSaveable {
        mutableStateOf("Choose your role")
    }

    // ---------------------------------------------------------
    // POPUP STATE
    // ---------------------------------------------------------

    var showPopup by rememberSaveable {
        mutableStateOf(false)
    }

    var popupMessage by rememberSaveable {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()




    // =========================================================
    // SIGN UP FUNCTION
    // =========================================================
    //
    // Both:
    // 1. Keyboard Send
    // 2. Sign Up button
    //
    // call this SAME function.
    //
    // =========================================================

    fun signUpUser() {
        // -----------------------------------------------------
        // NAME
        // -----------------------------------------------------
        if (name.isBlank()) {
            popupMessage = "Name is required"
            showPopup = true
            return
        }
        // -----------------------------------------------------
        // USERNAME
        // -----------------------------------------------------
        if (username.isBlank()) {
            popupMessage = "Username is required"
            showPopup = true
            return
        }
        // -----------------------------------------------------
        // ROLE
        // -----------------------------------------------------
        if (
            role.isBlank() || role == "Choose your role"
        ) {
            popupMessage = "Please choose your role"
            showPopup = true
            return
        }
        // -----------------------------------------------------
        // EVERYTHING IS VALID
        // -----------------------------------------------------
        viewMode.userinsert(
            userInfo(
                0,
                name,
                username
            )
        )
        authVM.sigUp(
            authVM.email,
            authVM.password,
            name,
            username,
            role
        ) { success ,responseCode ->
            if (success) {
                onLoginSuccess()
            }else if (responseCode == 500) {
                popupMessage = "Username already exists"
                showPopup = true
            }
        }
    }


    // =========================================================
    // MAIN SCREEN
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {


        // =====================================================
        // SCROLLABLE CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(
                    start = 35.dp,
                    end = 35.dp,
                    top = 45.dp,
                    bottom = 35.dp
                ),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // =================================================
            // ICON
            // =================================================
            Icon(
                imageVector = Icons.Default.Face6,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(85.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            // =================================================
            // TITLE
            // =================================================
            Text(
                text = "Complete your profile",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tell us a little about yourself",
                color = Color.LightGray,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(30.dp))
            // =================================================
            // NAME
            // =================================================
            userInfoField(
                value = name,
                onValueChange = {
                    name = it
                },
                placeholder = "Enter your Name",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Name"
                    )
                }
            )


            Spacer(modifier = Modifier.height(18.dp))
            // =================================================
            // ROLE
            // =================================================
            roleDropBox(
                role = role,
                onRoleChange = {
                    role = it
                }
            )
            Spacer(modifier = Modifier.height(18.dp))
            // =================================================
            // USERNAME
            // =================================================
            userInfoField(
                value = username,

                onValueChange = {
                    username = it
                },

                placeholder = "Enter your Username",
                showPrefix = true,
                imeAction = ImeAction.Send,
                onImeAction = {
                    // Keyboard Send
                    signUpUser()
                },

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Username"
                    )
                }
            )


            Spacer(modifier = Modifier.height(28.dp))
            // =================================================
            // SIGN UP BUTTON
            // =================================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .clickable {

                        // Button does the EXACT
                        // same operation as keyboard Send.

                        signUpUser()
                    },

                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Sign Up",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Complete all fields to continue",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
        // =====================================================
        // TOP WARNING POPUP
        // =====================================================

        if (showPopup) {

            LaunchedEffect(popupMessage) {
                delay(2000)
                showPopup = false
            }


            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 45.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .fillMaxWidth(),

                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 8.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 13.dp
                        ),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // -----------------------------------------
                    // CAUTION ICON
                    // -----------------------------------------

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Invalid",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // -----------------------------------------
                    // MESSAGE
                    // -----------------------------------------

                    Text(
                        text = popupMessage,
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
// =================================================================
// USER INFO TEXT FIELD
// =================================================================
@Composable
fun userInfoField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    showPrefix: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {}
) {

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },

        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),

        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 17.sp,
                color = Color.Gray
            )
        },
        leadingIcon = leadingIcon,
        // -----------------------------------------------------
        // USERNAME PREFIX
        // -----------------------------------------------------
        prefix = {
            if (showPrefix) {
                Text(
                    text = "@ ",
                    color = Color.Black,
                    fontSize = 17.sp
                )
            }
        },
        // -----------------------------------------------------
        // KEYBOARD
        // -----------------------------------------------------

        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                onImeAction()
            }
        ),


        // -----------------------------------------------------
        // UI
        // -----------------------------------------------------

        shape = RoundedCornerShape(16.dp),

        colors = TextFieldDefaults.colors(

            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedLeadingIconColor = Color.Black,
            unfocusedLeadingIconColor = Color.Gray
        )
    )
}


// =================================================================
// ROLE DROPDOWN
// =================================================================

@Composable
fun roleDropBox(
    role: String,
    onRoleChange: (String) -> Unit
) {

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    // Small arrow rotation animation
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "roleArrowRotation"
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // =====================================================
        // ROLE BOX
        // =====================================================
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clickable {
                    expanded = !expanded
                },

            color = Color.White,

            shape = RoundedCornerShape(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Role",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = role,
                    color =
                        if (role == "Choose your role") {
                            Color.Gray
                        } else {
                            Color.Black
                        },

                    fontSize = 17.sp,
                    modifier = Modifier.weight(1f)
                )

                // =================================================
                // ANIMATED ARROW
                // =================================================

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select role",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(arrowRotation)
                )
            }
        }


        // =====================================================
        // ANIMATED DROPDOWN
        // =====================================================

        AnimatedVisibility(
            visible = expanded,
            enter =
                expandVertically() + fadeIn(),

            exit =
                shrinkVertically() + fadeOut()
        ) {

            Column {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Surface(

                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column {

                        roleItem(
                            role = "Principal Engineer",

                            onClick = {
                                onRoleChange(
                                    "Principal Engineer"
                                )
                                expanded = false
                            }
                        )

                        roleItem(
                            role = "Manager",

                            onClick = {
                                onRoleChange(
                                    "Manager"
                                )
                                expanded = false
                            }
                        )

                        roleItem(
                            role = "SDE",
                            onClick = {

                                onRoleChange(
                                    "SDE"
                                )

                                expanded = false
                            }
                        )

                        roleItem(
                            role = "QA",
                            onClick = {
                                onRoleChange("QA")
                                expanded = false
                            }
                        )

                        roleItem(
                            role = "Product Manager",

                            onClick = {
                                onRoleChange("Product Manager")
                                expanded = false
                            }
                        )

                        roleItem(
                            role = "Intern",
                            onClick = {
                                onRoleChange("Intern")
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


// =================================================================
// ROLE ITEM
// =================================================================

@Composable
fun roleItem(
    role: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = role,
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}