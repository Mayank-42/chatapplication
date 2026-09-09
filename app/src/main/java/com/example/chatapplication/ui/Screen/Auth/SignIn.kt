package com.example.chatapplication.ui.Screen.Auth

import android.R.attr.bottom
import android.R.attr.font
import android.R.attr.fontStyle
import android.R.attr.password
import android.R.attr.text
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.Viewmodel.loginVM
import com.example.chatapplication.Data.local.tables.userLoginInfo
import kotlinx.coroutines.delay

@Composable
fun ShowSignIn(navControler: NavController,viewMode : databaseVM,authVM: loginVM,onLoginSuccess: (String) -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var showErrorBox by rememberSaveable { mutableStateOf(false) }

    var popupMessage by remember { mutableStateOf<String?>(null) }

    var ShowpopUp by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(popupMessage) {
        delay(3000)
            popupMessage = null
        ShowpopUp=false
    }
    LaunchedEffect(email) {
        if (email.isNotBlank()) {
            delay(2000)
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                popupMessage = "Enter a Valid Email"
                ShowpopUp=true
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
//                .border(1.dp, MaterialTheme.colorScheme.primary,CircleShape),

            )
//            Spacer(modifier=Modifier.height(30.dp))
            Text(
                text = "YOKOSU",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(25.dp)
            )
            surface(
                navControl = navControler,
                task = "Enter your Email",
                viewMode = viewMode,
                words = email,
                onWordsChange = { email = it },
                icon={
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                isMandatory = true
                )
            Spacer(modifier = Modifier.height(30.dp))
            surface(
                navControl = navControler,
                task = "Enter your pasword ",
                viewMode = viewMode,
                words = pass,
                onWordsChange = { pass = it },
                icon={
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                isMandatory = true,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(30.dp))
            surface(
                navControl = navControler,
                80, "Sign In",
                false,
                viewMode = viewMode,
                "", {},
                onButtonClick = {
//                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
//                        popupMessage = "Enter a Valid Email"
//                    }

                    authVM.login(email, pass) { response, userId ->
                        var msg=isValid(email,pass)
                        if(msg!="good"){
                            popupMessage=msg
                            ShowpopUp=true
                        }
                       else if (response.isSuccessful && userId != null) {
                            onLoginSuccess(userId)
                        }
                        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()&&response.isSuccessful==false){
                            popupMessage = "Enter a Valid Email "
                            ShowpopUp=true
                        }
                        else {
                            popupMessage = """
                            Incorrect email or password.
                            Please check your credentials or register first.
                            """.trimIndent()
                            ShowpopUp=true
                        }
                    }
                },
                icon={}
                )
            //DialogBoxwe are not using
            if (showErrorBox) {
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

            val blue = Color(0xFF3B82F6)
            Row {
                Text(
                    text = "Not a User?",
                    color = Color.White,
                    modifier = Modifier.padding(top = 15.dp)
                )
                TextButton(onClick = { navControler.navigate("register") })
                {
                    Text(
                        text = "Register",
                        color = blue,
                        fontStyle = FontStyle.Italic,

                        )
                }
            }
        }

    }
    AnimatedVisibility(
        visible = ShowpopUp,
        enter = fadeIn(animationSpec = tween(250)
        ) + scaleIn(
            initialScale = 0.85f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + slideInVertically(
            initialOffsetY = { -80 },
            animationSpec = tween(450)
        ),
        exit = fadeOut(
            animationSpec = tween(180)
        ) + scaleOut(
            targetScale = 0.9f,
            animationSpec = tween(180)
        ) + slideOutVertically(
            targetOffsetY  = {+80 },
            animationSpec = tween(450)
        )
    ) {

    if (popupMessage != null) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 20.dp, end = 20.dp),
            color = Color.White,
            shape = RoundedCornerShape(14.dp),
            shadowElevation = 8.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ------------------------------------------
                // WARNING ICON
                // -----------------------------------------
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

@Composable
fun surface(
    navControl: NavController,
    size: Int = 60,
    task: String = "Enter Text here",
    wantTextField: Boolean = true,
    viewMode: databaseVM,
    words: String,
    onWordsChange: (String) -> Unit,
    onButtonClick: () -> Unit = {},
    icon: @Composable () -> Unit,
    isMandatory: Boolean = false,
    isPassword: Boolean = false
) {

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 35.dp,
                end = 35.dp
            )
            .height(size.dp),

        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {

        if (wantTextField) {

            OutlinedTextField(
                modifier = Modifier.fillMaxSize(),

                value = words,

                onValueChange = {
                    onWordsChange(it)
                },

                singleLine = true,

                placeholder = {
                    Text(
                        text = task,
                        fontSize = 20.sp
                    )
                },

                // -------------------------------------------------
                // ICON + PERMANENT *
                // -------------------------------------------------

                leadingIcon = {

                    Box() {

                        // YOUR NORMAL ICON
                        Box(
                            modifier = Modifier
                                .size(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            icon()
                        }

                        // PERMANENT *
                        if (isMandatory) {

                            Text(
                                text = "*",
                                color = Color.Red,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
//                                    .offset(
//                                        x = 2.dp,
//                                        y = (-3).dp
//                                    )
                            )
                        }
                    }
                },

                // -------------------------------------------------
                // PASSWORD
                // -------------------------------------------------

                visualTransformation =
                    if (isPassword && !passwordVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },

                keyboardOptions =
                    if (isPassword) {
                        androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        )
                    } else {
                        androidx.compose.foundation.text.KeyboardOptions.Default
                    },

                // -------------------------------------------------
                // PASSWORD EYE
                // -------------------------------------------------

                trailingIcon = {

                    if (isPassword) {

                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (passwordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },

                                contentDescription =
                                    if (passwordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    },

                                tint = Color.Gray
                            )
                        }
                    }
                },

                shape = RoundedCornerShape(16.dp),

                colors = OutlinedTextFieldDefaults.colors(

                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    focusedLeadingIconColor = Color.Black,
                    unfocusedLeadingIconColor = Color.Gray,

                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,

                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,

                    cursorColor = Color.Black,

                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Transparent
                )
            )

        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        onButtonClick()
                    },

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = task,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
        }
    }
}


fun isValid(email: String, pass: String): String {
    if (email.isBlank() && pass.isBlank())
        return "Please enter your email and password"

    else if (email.isNotBlank() && pass.isBlank())
        return "Please enter your password"

    else if (email.isBlank() && pass.isNotBlank())
        return "Please enter your email"

    else
        return "good"
}



//@Preview(showSystemUi = true,uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Composable
//fun show(){
//    ChatApplicationTheme {   // <-- Your app's theme name
//        ShowSignIn(navController : NavController)
//    }
//}
