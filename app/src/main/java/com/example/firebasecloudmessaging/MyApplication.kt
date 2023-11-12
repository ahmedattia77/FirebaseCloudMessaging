package com.example.firebasecloudmessaging

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "LocalDatabase")
class MyApplication : Application(){


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        notificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notificationChannel(){
        val channel = NotificationChannel(
            "Global" ,
             "NotificationNameChannel" ,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        channel.description = "NotificationDescriptionChannel"

        val notificationManager: NotificationManager
                = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

}