package com.example.firebasecloudmessaging.firebase

import android.Manifest
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.firebasecloudmessaging.dataStore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.firebasecloudmessaging.MainActivity
import com.example.firebasecloudmessaging.R


class FirebaseCloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message != null){
                message.notification?.title?.let {
                    Log.d("Test0" ,it)
                }
                message.notification?.body?.let {
                    Log.d("Test0" ,it)
                }
                message.notification?.channelId?.let {
                Log.d("Test0" ,it)
            }
        }


        message.data.let {
            createNotification(it)
        }

        if (message.notification != null) {

            Log.v("CloudMessage", "Notification ${message.notification}")
            Log.v("CloudMessage", "Notification Title ${message.notification!!.title}")
            Log.v("CloudMessage", "Notification Body ${message.notification!!.body}")

        }

    }

    private fun createNotification(data: Map<String, String>) {

        val intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        intent.putExtra("title",data["title"])
        intent.putExtra("body",data["body"])

        val deepLink = Intent(ACTION_VIEW , ("deepLink://"+data["page"]).toUri(),
            this , MainActivity::class.java)

        val requestCode = System.currentTimeMillis().toInt()

        val pendingIntent : PendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(deepLink).getPendingIntent(
                    requestCode ,
                    FLAG_MUTABLE
                )
            }
        }else{
            TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(deepLink).getPendingIntent(
                    requestCode ,
                    FLAG_CANCEL_CURRENT
                )
            }
        }

        val builder = NotificationCompat.Builder(this,"Global").setAutoCancel(true)
            .setContentTitle(data["title"])
            .setContentText(data["body"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText((data["body"])))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)


        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(
                    baseContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(requestCode,builder.build())
        }

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM",token)
        GlobalScope.launch {
            saveToken(token)
        }
    }

    private suspend fun saveToken(token:String) {
        baseContext.dataStore.edit {
            it[stringPreferencesKey("gcm_token")] = token
        }
    }

}