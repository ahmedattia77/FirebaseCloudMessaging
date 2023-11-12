package com.example.firebasecloudmessaging.firebase

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.firebasecloudmessaging.dataStore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirebaseCloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.i("messaging" , "From${message.from} + ID${message.messageId}")

        message.data.let {
           Log.i("messaging" , "Message: $it")
            Log.i("messaging" , "Message Notifi Body: ${it["body"]}")

        }


        if (message.notification != null){
            Log.i("messaging" , "${message.notification}")
            Log.i("messaging" , "${message.notification!!.title}")
            Log.i("messaging" , "${message.notification!!.body}")
        }
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GlobalScope.launch {
            saveToken(token)
        }
    }

    private suspend fun saveToken(token:String) {
        Log.i("token" , "token : $token")
        baseContext.dataStore.edit {
            it[stringPreferencesKey("gcm_token")] = token
        }
    }
}