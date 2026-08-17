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
import com.example.chatapplication.ui.theme.ChatApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            val tokenManager = TokenManager(this)
        SupaBaseClient.initialize(tokenManager)
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

            var userInfoRepo= UserInfoReposatory(retroFitClient.apiService)
            var userInfovm: UserInfo =viewModel(
                factory = UserInfoFactory(userInfoRepo)
            )
            val realtimeRepo = RealTimeRepo(tokenManager)
            var messageInfoRepo= MessageRepo(retroFitClient.apiService, application.database.dataBaseCall(),)
            var messageInfoVM: MsgVM=viewModel(
                factory= MsgVM.MsgVMFactory(messageInfoRepo, realtimeRepo, repo, tokenManager)
            )


            var firstPage by rememberSaveable {
                mutableStateOf<String?>(null)
            }

            LaunchedEffect(Unit) {

                val token = tokenManager.getAccessToken()

                if (token != null) {
                    firstPage = "Home"
                } else {
                    firstPage = "SignIn"
                }
            }



//            var temp=if(!authVM.userLoggedIn) "SignIn" else "Home";
            if (firstPage != null) {

                val navControler = rememberNavController()
                NavHost(
                    navController = navControler,
                    startDestination = "$firstPage"
                ) {
                    composable("SignIn") {
                        ShowSignIn(navControler, viewModel, authVM)
                    }
                    composable("register") {
                        ShowShinUp(navControler, authVM)
                    }
                    composable("UserInfo") {
                        UserInfo(navControler, viewModel, authVM)
                    }
                    composable("Home") {
                        HomeScreen(navControler,tokenManager,userInfovm)
                    }
                    composable("ChatScreen/{userId}") {backstackEntry->
                        val userId=backstackEntry.arguments?.getString("userId")
                        chatScreen(navControler, viewModel,userId,messageInfoVM,tokenManager)
                    }
                    composable("SearchBarPage"){
                        SearchBarPage(navControler,userInfovm)
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