package com.example.herculeswallet.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.herculeswallet.R
import com.example.herculeswallet.databinding.WalletBinding
import com.example.herculeswallet.utils.ReminderBroadcast
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import java.util.concurrent.TimeUnit

class Wallet : AppCompatActivity() {

    private lateinit var binding: WalletBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val notificationId = 101
    private val channelId = "com.example.herculeswallet.view"
    private val description = "Test notification"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide();
        setContentView(R.layout.wallet)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.findNavController()

        navController?.let { bottomNavigationView.setupWithNavController(it) }

        createNotification()

        val firingCal: Calendar = Calendar.getInstance()
        firingCal.set(Calendar.HOUR_OF_DAY, 10)
        firingCal.set(Calendar.MINUTE, 0)
        firingCal.set(Calendar.SECOND, 0)

        if(Calendar.getInstance().after(firingCal)){
            firingCal.add(Calendar.DAY_OF_MONTH,1)
        }

        val intent = Intent(this,ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,firingCal.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,firingCal.timeInMillis,pendingIntent)
        }
    }

    private fun createNotification(){
        val name = "HerculesWallet"
        val descriptionText = "Torna a controllare le tue crypto ...."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId,name,importance).apply {
            description = descriptionText
        }
        notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

}

/*
val intent = Intent(this,MainActivity::class.java)
        val pedingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
        builder = Notification.Builder(this,channelId)
            .setContentTitle("HerculesWallet")
            .setContentText("Torna a controllare le crypto ....")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.mipmap.ic_launcher))
            .setContentIntent(pedingIntent)
        notificationManager.notify(1234,builder.build())
 */

