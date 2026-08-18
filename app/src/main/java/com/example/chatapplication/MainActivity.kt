package com.example.chatapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Repo.UserInfoReposatory
import com.example.chatapplication.Data.Viewmodel.dataBaseVMfacrory
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.Viewmodel.AuthViewModelFactory
import com.example.chatapplication.Data.Viewmodel.MsgVM
//import com.example.chatapplication.Data.Viewmodel.MsgVMFactory
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.Viewmodel.UserInfoFactory
import com.example.chatapplication.Data.Viewmodel.loginVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.operation
import com.example.chatapplication.Data.network.clients.AuthRetroFitClient
import com.example.chatapplication.Data.network.clients.SupaBaseClient
import com.example.chatapplication.Data.network.clients.retroFitClient
import com.example.chatapplication.ui.Screen.Auth.ShowShinUp
import com.example.chatapplication.ui.Screen.Auth.ShowSignIn
import com.example.chatapplication.ui.Screen.Auth.UserInfo
import com.example.chatapplication.ui.Screen.Main.HomeScreen
import com.example.chatapplication.ui.Screen.Main.SearchBarPage
import com.example.chatapplication.ui.Screen.Main.chatScreen
import com.example.chatapplication.ui.Screen.Main.profileScreen
import com.example.chatapplication.ui.theme.ChatApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            val tokenManager = TokenManager(this)
        enableEdgeToEdge()
        setContent {

            var application = application as dataBaseBuilder
            retroFitClient.initialize(applicationContext)



            var repo = reposatory(application.database.dataBaseCall())
            var viewModel: databaseVM = viewModel(
                factory = dataBaseVMfacrory(repo)
            )

            var authRepo = AuthReposatory(AuthRetroFitClient.AuthApiService)
            var authVM: loginVM = viewModel(
                factory = AuthViewModelFactory(authRepo, tokenManager)
            )

//            var userInfoRepo= UserInfoReposatory(retroFitClient.apiService,supa)
//            var userInfovm: UserInfo =viewModel(
//                factory = UserInfoFactory(userInfoRepo)
//            )




            var firstPage by rememberSaveable {
                mutableStateOf<String?>(null)
            }
            var isAuthenticated by rememberSaveable {
                mutableStateOf(false)
            }

            LaunchedEffect(Unit) {
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken != null) {
                    val response = authRepo.refreshToken(refreshToken)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            tokenManager.saveTokens(
                                it.access_token,
                                it.refresh_token
                            )
                            SupaBaseClient.initialize(
                                it.access_token
                            )
                        }
                        isAuthenticated = true
                        firstPage = "Home"
                    } else {
                        tokenManager.clearTokens()
                        isAuthenticated = false
                        firstPage = "SignIn"
                    }
                } else {
                    isAuthenticated = false
                    firstPage = "SignIn"
                }
            }



//            var temp=if(!authVM.userLoggedIn) "SignIn" else "Home";
            if (firstPage != null) {

                val navController = rememberNavController()

                if (isAuthenticated) {

                    // These are created ONLY after SupabaseClient is initialized
                    val realtimeRepo = RealTimeRepo(tokenManager)

                    val messageInfoRepo = MessageRepo(
                        retroFitClient.apiService,
                        application.database.dataBaseCall()
                    )

                    val messageInfoVM: MsgVM = viewModel(
                        factory = MsgVM.MsgVMFactory(
                            messageInfoRepo,
                            realtimeRepo,
                            repo,
                            tokenManager
                        )
                    )

                    val userInfoRepo = UserInfoReposatory(
                        retroFitClient.apiService,
                        SupaBaseClient.supabase
                    )

                    val userInfovm: UserInfo = viewModel(
                        factory = UserInfoFactory(userInfoRepo)
                    )

                    NavHost(
                        navController = navController,
                        startDestination = "Home"
                    ) {

                        composable("Home") {
                            HomeScreen(
                                navController,
                                tokenManager,
                                userInfovm
                            )
                        }

                        composable("ChatScreen/{userId}") { backStackEntry ->

                            val userId =
                                backStackEntry.arguments?.getString("userId")

                            chatScreen(
                                navController,
                                viewModel,
                                userId,
                                messageInfoVM,
                                tokenManager
                            )
                        }

                        composable("SearchBarPage") {
                            SearchBarPage(
                                navController,
                                userInfovm
                            )
                        }

                        composable("profileScreen") {
                            profileScreen(
                                navController,
                                userInfovm,
                                tokenManager
                            )
                        }
                    }

                } else {

                    NavHost(
                        navController = navController,
                        startDestination = "SignIn"
                    ) {

                        composable("SignIn") {
                            ShowSignIn(
                                navController,
                                viewModel,
                                authVM,
                                onLoginSuccess = {
                                    isAuthenticated = true
                                }
                            )
                        }

                        composable("register") {
                            ShowShinUp(
                                navController,
                                authVM
                            )
                        }

                        composable("UserInfo") {
                            UserInfo(
                                navController,
                                viewModel,
                                authVM,
                                onLoginSuccess = {
                                    isAuthenticated = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatApplicationTheme {
        Greeting("Android")
    }
}