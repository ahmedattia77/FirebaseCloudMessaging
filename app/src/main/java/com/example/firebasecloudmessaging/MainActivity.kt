package com.example.firebasecloudmessaging

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.firebasecloudmessaging.ui.theme.FirebaseCloudMessagingTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseCloudMessagingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun Home (){
    val context = LocalContext.current
    val tokenKey = stringPreferencesKey("gcm_token")

    val fcmToken = flow<String> {

        context.dataStore.data.map { pref ->
            pref[tokenKey]
        }.collect(collector = {
            if (it != null){
                this.emit(it)
            }
        })
    }.collectAsState(initial = "")

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Notifications") }) }
    ) {
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) 
        {
            Log.i("FCM" , fcmToken.value)
            Text(text = "FCM Token")
            Text(text = fcmToken.value)
        }
    }


}