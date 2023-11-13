package com.example.firebasecloudmessaging

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.firebasecloudmessaging.ui.theme.FirebaseCloudMessagingTheme
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseCloudMessagingTheme {

                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "home" ) {
                    composable(
                        route = "home" ,
                        deepLinks = listOf(
                            NavDeepLink ("deeplink://home")
                        )
                    ){
                        Home(intent = intent , nav)
                    }
                    composable(
                        route = "anther_screen" ,
                        deepLinks = listOf(
                            NavDeepLink ("deeplink://anther_screen")
                        )
                    ){
                      anther_screen(intent)
                    }
                }

            }
        }
    }

}

@Composable
fun anther_screen(intent: Intent) {
    Box(
        modifier = Modifier.fillMaxSize() ,
        contentAlignment = Alignment.Center
        ){
        Text(text = "Anther Screen")
    }
}

@Composable
fun Home(intent: Intent, nav: NavHostController) {
    val context = LocalContext.current
    val tokenKey = stringPreferencesKey("gcm_token")

    val fcmToken = flow<String> {
        context.dataStore.data.map { pref ->
            pref[tokenKey]
        }.collect(collector = {
            if (it != null){
                Log.d("FCM" , it)
                this.emit(it)
            }
        })
    }.collectAsState(initial = "")

    val title = remember {
        mutableStateOf(
            if (intent.hasExtra("title")) {
                intent.getStringExtra("title")
            } else {
                ""
            }
        )
    }

    val body = remember {
        mutableStateOf(
            if (intent.hasExtra("body")) {
                intent.getStringExtra("body")
            } else {
                ""
            }
        )
    }

    Column (modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = { nav.navigate("anther_screen") }) {
            Text(text = "To Anther Screen")
        }
    }
}