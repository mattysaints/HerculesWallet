package com.example.herculeswallet.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.herculeswallet.R
import com.example.herculeswallet.view.MainActivity
import okhttp3.internal.notify

class ReminderBroadcast : BroadcastReceiver() {

    private lateinit var builder: NotificationCompat.Builder
    private val notificationId = 101
    private val channelId = "com.example.herculeswallet.view"

    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        builder = NotificationCompat.Builder(context!!,channelId)
            .setContentTitle("HerculesWallet")
            .setContentText("Torna a controllare le tue crypto ....")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager : NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId,builder.build())


    }
}