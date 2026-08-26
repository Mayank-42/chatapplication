package com.example.chatapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.Data.DAO.conversationId
import com.example.chatapplication.Data.Repo.AuthReposatory
import com.example.chatapplication.Data.Repo.GroupRepo
import com.example.chatapplication.Data.Repo.MessageRepo
import com.example.chatapplication.Data.Repo.RealTimeRepo
import com.example.chatapplication.Data.Repo.UserInfoReposatory
import com.example.chatapplication.Data.Repo.convoInfoRepo
import com.example.chatapplication.Data.Viewmodel.dataBaseVMfacrory
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.Repo.reposatory
import com.example.chatapplication.Data.Viewmodel.AuthViewModelFactory
import com.example.chatapplication.Data.Viewmodel.ConvoVMFactory
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.Data.Viewmodel.GroupChatVMfacrory
import com.example.chatapplication.Data.Viewmodel.MsgVM
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.Viewmodel.UserInfoFactory
import com.example.chatapplication.Data.Viewmodel.convoVM
import com.example.chatapplication.Data.Viewmodel.loginVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.network.clients.AuthRetroFitClient
import com.example.chatapplication.Data.network.clients.SupaBaseClient
import com.example.chatapplication.Data.network.clients.retroFitClient
import com.example.chatapplication.ui.Screen.Auth.ShowShinUp
import com.example.chatapplication.ui.Screen.Auth.ShowSignIn
import com.example.chatapplication.ui.Screen.Auth.UserInfo
import com.example.chatapplication.ui.Screen.ChekUserState
import com.example.chatapplication.ui.Screen.GroupChat.GroupChatScreen
import com.example.chatapplication.ui.Screen.GroupChat.GroupChatSearch
import com.example.chatapplication.ui.Screen.GroupChat.GroupName
import com.example.chatapplication.ui.Screen.GroupChat.GroupPage
import com.example.chatapplication.ui.Screen.Main.HomeScreen
import com.example.chatapplication.ui.Screen.Main.SearchBarPage
import com.example.chatapplication.ui.Screen.Main.chatScreen
import com.example.chatapplication.ui.Screen.Main.profileScreen
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import kotlinx.coroutines.delay
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ComposableDestinationInComposeScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(this)
        enableEdgeToEdge()
        setContent {

            val now = java.time.Instant.now()

            println("ANDROID CURRENT TIME = $now")
            println("ANDROID CURRENT MILLIS = ${System.currentTimeMillis()}")

            println("========== TIME DEBUG ==========")
            println("ANDROID INSTANT = ${java.time.Instant.now()}")
            println("ANDROID MILLIS = ${System.currentTimeMillis()}")
            println("ANDROID EPOCH SECOND = ${System.currentTimeMillis() / 1000}")
            println("================================")

            var userId by rememberSaveable { mutableStateOf("") }

            var application = application as dataBaseBuilder
            retroFitClient.initialize(applicationContext)

            var repo = reposatory(
                application.database.dataBaseCall()
            )

            var viewModel: databaseVM = viewModel(factory = dataBaseVMfacrory(repo))

            var authRepo = AuthReposatory(AuthRetroFitClient.AuthApiService)

            var authVM: loginVM = viewModel(
                factory = AuthViewModelFactory(
                    authRepo,
                    tokenManager
                )
            )

            var authState by rememberSaveable {
                mutableStateOf(
                    ChekUserState.cheking
                )
            }

            LaunchedEffect(Unit) {

                val refreshToken = tokenManager.getRefreshToken()

                if (refreshToken.isNullOrBlank()) {
                    authState = ChekUserState.unAuthenticated

                } else {
                    val response = authRepo.refreshToken(refreshToken)
                    if (response.isSuccessful) {

                        response.body()?.let {
                            tokenManager.saveTokens(
                                it.access_token,
                                it.refresh_token
                            )
                            delay(2000)
                            SupaBaseClient.initialize(
                                tokenManager
                            )

                            userId = tokenManager.getUserId() ?: ""

                            authState = ChekUserState.authenticated

                        } ?: run {
                            tokenManager.clearTokens()
                            authState =
                                ChekUserState.unAuthenticated
                        }
                    } else {
                        tokenManager.clearTokens()
                        authState = ChekUserState.unAuthenticated
                    }
                }
            }

            val navController =
                rememberNavController()

            when (authState) {

                ChekUserState.cheking -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ChekUserState.authenticated -> {

                    var GroupRepo =
                        GroupRepo(
                            application.database.Groupcall(),
                            retroFitClient.apiService
                        )

                    var save: GroupChatVM =
                        viewModel(
                            factory =
                                GroupChatVMfacrory(
                                    GroupRepo
                                )
                        )

                    val realtimeRepo = RealTimeRepo()

                    val convoRepo =
                        convoInfoRepo(
                            application.database.ConvoInfo(),
                            retroFitClient.apiService
                        )

                    val convoInfoVM: convoVM =
                        viewModel(
                            factory =
                                ConvoVMFactory(
                                    convoRepo,
                                    tokenManager,
                                    repo,
                                    realtimeRepo
                                )
                        )

                    val messageInfoRepo =
                        MessageRepo(
                            retroFitClient.apiService,
                            application.database.dataBaseCall()
                        )

                    val messageInfoVM: MsgVM =
                        viewModel(
                            factory =
                                MsgVM.MsgVMFactory(
                                    messageInfoRepo,
                                    realtimeRepo,
                                    repo,
                                    tokenManager,
                                    convoRepo
                                )
                        )
                    val userInfoRepo =
                        UserInfoReposatory(retroFitClient.apiService, SupaBaseClient.supabase)

                    val userInfovm: UserInfo =
                        viewModel(
                            key = "UserInfo-$userId",
                            factory = UserInfoFactory(userInfoRepo, messageInfoRepo)
                        )

                    LaunchedEffect(Unit) {
                        println("========== TIME DEBUG ==========")
                        println("ANDROID INSTANT = ${java.time.Instant.now()}")
                        println("ANDROID EPOCH = ${System.currentTimeMillis() / 1000}")
                        println("================================")

                        convoInfoVM.startConversationRealtime()
                        convoInfoVM.syncConversations()
                        messageInfoVM.startRealtime()
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "Home"
                    ) {

                        composable("Home") {
                            HomeScreen(
                                navController,
                                tokenManager,
                                userInfovm,
                                onLoginSuccess = {
                                    userId = ""
                                    authState =
                                        ChekUserState.unAuthenticated
                                },
                                convoInfoVM
                            )
                        }

                        composable(
                            "ChatScreen/{conversationId}"
                        ) { backStackEntry ->
                            val conversationId =
                                backStackEntry.arguments
                                    ?.getString(
                                        "conversationId"
                                    )
                            println("NAVIGATION: conversationId = $conversationId")
                            if (conversationId != null) {

                                chatScreen(
                                    navController,
                                    viewModel,
                                    conversationId =
                                        conversationId,
                                    messageInfoVM,
                                    tokenManager
                                )
                            }
                        }

                        composable("SearchBarPage") {

                            SearchBarPage(
                                navController,
                                userInfovm
                            )
                        }

                        composable(
                            "profileScreen/{userId}"
                        ) { backStackEntry ->

                            val profileUserId =
                                backStackEntry.arguments
                                    ?.getString(
                                        "userId"
                                    )

                            if (profileUserId != null) {

                                profileScreen(
                                    navController,
                                    userInfovm,
                                    tokenManager,
                                    profileUserId
                                )
                            }
                        }

                        composable("GroupPage") {
                            GroupPage(
                                navController,
                                save,
                                convoInfoVM
                            )
                        }

                        composable("GropChatSearch") {
                            GroupChatSearch(
                                navController,
                                userInfovm,
                                save
                            )
                        }

                        composable("GroupName") {

                            GroupName(
                                navController,
                                save
                            )
                        }

                        composable("GroupChatScreen/{conversationId}") { backStackEntry ->
                            val conversationId =
                                backStackEntry.arguments
                                    ?.getString(
                                        "conversationId"
                                    )
                            println("NAVIGATION: conversationId = $conversationId")
                            if (conversationId != null) {
                                GroupChatScreen(
                                    navController,
                                    viewModel,
                                    conversationId,
                                    messageInfoVM,
                                    tokenManager,
                                    save
                                )
                            }
                        }
                    }
                }

                ChekUserState.unAuthenticated -> {

                    NavHost(
                        navController = navController,
                        startDestination = "SignIn"
                    ) {

                        composable("SignIn") {

                            ShowSignIn(
                                navController,
                                viewModel,
                                authVM,

                                onLoginSuccess = { id ->

                                    userId = id

                                    authState =
                                        ChekUserState.authenticated
                                }
                            )
                        }

                        composable("register") {
                            ShowShinUp(navController, authVM)
                        }
                        composable("UserInfo") {
                            UserInfo(navController, viewModel, authVM,

                                onLoginSuccess = {

                                    authState =
                                        ChekUserState.authenticated
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
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {
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

//package com.example.chatapplication
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.chatapplication.Data.Repo.AuthReposatory
//import com.example.chatapplication.Data.Repo.GroupRepo
//import com.example.chatapplication.Data.Repo.MessageRepo
//import com.example.chatapplication.Data.Repo.RealTimeRepo
//import com.example.chatapplication.Data.Repo.UserInfoReposatory
//import com.example.chatapplication.Data.Repo.convoInfoRepo
//import com.example.chatapplication.Data.Viewmodel.dataBaseVMfacrory
//import com.example.chatapplication.Data.Viewmodel.databaseVM
//import com.example.chatapplication.Data.Repo.reposatory
//import com.example.chatapplication.Data.Viewmodel.AuthViewModelFactory
//import com.example.chatapplication.Data.Viewmodel.ConvoVMFactory
//import com.example.chatapplication.Data.Viewmodel.GroupChatVM
//import com.example.chatapplication.Data.Viewmodel.GroupChatVMfacrory
//import com.example.chatapplication.Data.Viewmodel.MsgVM
////import com.example.chatapplication.Data.Viewmodel.MsgVMFactory
//import com.example.chatapplication.Data.Viewmodel.UserInfo
//import com.example.chatapplication.Data.Viewmodel.UserInfoFactory
//import com.example.chatapplication.Data.Viewmodel.convoVM
//import com.example.chatapplication.Data.Viewmodel.loginVM
//import com.example.chatapplication.Data.local.TokenManager
//import com.example.chatapplication.Data.network.clients.AuthRetroFitClient
//import com.example.chatapplication.Data.network.clients.SupaBaseClient
//import com.example.chatapplication.Data.network.clients.retroFitClient
//import com.example.chatapplication.ui.Screen.Auth.ShowShinUp
//import com.example.chatapplication.ui.Screen.Auth.ShowSignIn
//import com.example.chatapplication.ui.Screen.Auth.UserInfo
//import com.example.chatapplication.ui.Screen.ChekUserState
//import com.example.chatapplication.ui.Screen.GroupChat.GroupChatScreen
//import com.example.chatapplication.ui.Screen.GroupChat.GroupChatSearch
//import com.example.chatapplication.ui.Screen.GroupChat.GroupName
//import com.example.chatapplication.ui.Screen.GroupChat.GroupPage
//import com.example.chatapplication.ui.Screen.Main.HomeScreen
//import com.example.chatapplication.ui.Screen.Main.SearchBarPage
//import com.example.chatapplication.ui.Screen.Main.chatScreen
//import com.example.chatapplication.ui.Screen.Main.profileScreen
//import com.example.chatapplication.ui.theme.ChatApplicationTheme
//import retrofit2.Retrofit
//
//class MainActivity : ComponentActivity() {
//    @SuppressLint("ComposableDestinationInComposeScope")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//            val tokenManager = TokenManager(this)
//        enableEdgeToEdge()
//        setContent {
//
//
//            var userId by rememberSaveable { mutableStateOf("") }
//            var application = application as dataBaseBuilder
//            retroFitClient.initialize(applicationContext)
//
//
//            var repo = reposatory(application.database.dataBaseCall())
//            var viewModel: databaseVM = viewModel(
//                factory = dataBaseVMfacrory(repo)
//            )
//
//            var authRepo = AuthReposatory(AuthRetroFitClient.AuthApiService)
//            var authVM: loginVM = viewModel(
//                factory = AuthViewModelFactory(authRepo, tokenManager)
//            )
//
////            var userInfoRepo= UserInfoReposatory(retroFitClient.apiService,supa)
////            var userInfovm: UserInfo =viewModel(
////                factory = UserInfoFactory(userInfoRepo)
////            )
//
//
//
//
////            var firstPage by rememberSaveable {
////                mutableStateOf<String?>(null)
////            }
////            var isAuthenticated by rememberSaveable {
////                mutableStateOf(false)
////            }
//            var authState by rememberSaveable {
//                mutableStateOf(ChekUserState.cheking)
//            }
//
//            LaunchedEffect(Unit) {
//                val refreshToken = tokenManager.getRefreshToken()
//                if (refreshToken != null) {
//                    val response = authRepo.refreshToken(refreshToken)
//                    if (response.isSuccessful) {
//                        response.body()?.let {
//                            tokenManager.saveTokens(
//                                it.access_token,
//                                it.refresh_token
//                            )
//                            SupaBaseClient.initialize(
//                                it.access_token
//                            )
//                            userId=tokenManager.getUserId()?:""
//                        }
//                        authState = ChekUserState.authenticated
//                    } else {
//                        tokenManager.clearTokens()
//                        authState = ChekUserState.unAuthenticated
//                    }
//                }
//            }
//
////            var temp=if(!authVM.userLoggedIn) "SignIn" else "Home";
////            if (firstPage != null) {
//
//            when (authState){
//                ChekUserState.cheking->{
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//            val navController = rememberNavController()
//
//                if (isAuthenticated) {
//
//                    var GroupRepo= GroupRepo(application.database.Groupcall(), retroFitClient.apiService )
//                    var save: GroupChatVM= viewModel(factory = GroupChatVMfacrory(GroupRepo))
//                    // These are created ONLY after SupabaseClient is initialized
//                    val realtimeRepo = RealTimeRepo(tokenManager)
//
//                    val convoRepo= convoInfoRepo(application.database.ConvoInfo(), retroFitClient.apiService)
//                    val convoInfoVM: convoVM=viewModel(
//                        factory= ConvoVMFactory(convoRepo,tokenManager,repo)
//                    )
//                    val messageInfoRepo = MessageRepo(
//                        retroFitClient.apiService,
//                        application.database.dataBaseCall()
//                    )
//
//                    val messageInfoVM: MsgVM = viewModel(
//                        factory = MsgVM.MsgVMFactory(
//                            messageInfoRepo,
//                            realtimeRepo,
//                            repo,
//                            tokenManager,
//                            convoRepo
//                        )
//                    )
//
//                    val userInfoRepo = UserInfoReposatory(
//
//                        retroFitClient.apiService,
//                        SupaBaseClient.supabase
//                    )
//
//                    val userInfovm: UserInfo = viewModel(
//                        key = "UserInfo-$userId",
//                        factory = UserInfoFactory(userInfoRepo, messageInfoRepo)
//                    )
//
//                    LaunchedEffect(Unit) {
//                        convoInfoVM.syncConversations()
//                        messageInfoVM.startRealtime()
//                    }
//
//                    NavHost(
//                        navController = navController,
//                        startDestination = "Home"
//                    ) {
//
//                        composable("Home") {
//
//
//                            HomeScreen(
//                                navController,
//                                tokenManager,
//                                userInfovm,
//                                onLoginSuccess = {
//                                    userId=""
//                                    isAuthenticated = false
//                                },
//                                convoInfoVM
//
//                            )
//                        }
//
//                        composable("ChatScreen/{conversationId}") { backStackEntry ->
//
//                            val conversationId =
//                                backStackEntry.arguments?.getString("conversationId")
//                            println("NAVIGATION: conversationId = $conversationId")
//                            if (conversationId != null) {
//                                chatScreen(
//                                    navController,
//                                    viewModel,
////                                    userId,
//                                    conversationId = conversationId,
//                                    messageInfoVM,
//                                    tokenManager
//                                )
//                            }
//                        }
//
//                        composable("SearchBarPage") {
//                            SearchBarPage(
//                                navController,
//                                userInfovm
//                            )
//                        }
//
//                        composable("profileScreen/{userId}") { backStackEntry ->
//                            val profileUserId =
//                                backStackEntry.arguments?.getString("userId")
//                            if (profileUserId != null) {
//                                profileScreen(
//                                    navController,
//                                    userInfovm,
//                                    tokenManager,
//                                    profileUserId
//                                )
//                            }
//                        }
//                            composable("GroupPage"){
//                                GroupPage(navController,save)
//                            }
//                            composable("GropChatSearch"){
//                                GroupChatSearch(navController,userInfovm,save)
//                            }
//                        composable("GroupName"){
//                            GroupName(navController,save)
//                        }
//                        composable("GroupChatScreen"){
//                            GroupChatScreen(
//                                navController,
//                                viewModel,
//                                userId,
//                                messageInfoVM,
//                                tokenManager,
//                                save
//                            )
//                        }
//                    }
//
//                } else {
//
//                    NavHost(
//                        navController = navController,
//                        startDestination = "SignIn"
//                    ) {
//
//                        composable("SignIn") {
//                            ShowSignIn(
//                                navController,
//                                viewModel,
//                                authVM,
//                                onLoginSuccess = {id ->
//                                    userId=id
//                                    isAuthenticated = true
//                                }
//                            )
//                        }
//
//                        composable("register") {
//                            ShowShinUp(
//                                navController,
//                                authVM,
//
//                            )
//                        }
//
//                        composable("UserInfo") {
//                            UserInfo(
//                                navController,
//                                viewModel,
//                                authVM,
//                                onLoginSuccess = {
//                                    isAuthenticated = true
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ChatApplicationTheme {
//        Greeting("Android")
//    }
//}