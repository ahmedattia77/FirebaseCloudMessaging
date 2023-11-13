package com.example.firebasecloudmessaging.broad_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getStringExtra("title")
        message?.let {
            Toast.makeText(
                context ,
                it ,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}