package com.example.herculeswallet.view

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.herculeswallet.R
import com.example.herculeswallet.utils.ReminderBroadcast
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class Wallet : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "com.example.herculeswallet.view"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide();
        setContentView(R.layout.wallet)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        val navController =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.findNavController()

        navController?.let { bottomNavigationView.setupWithNavController(it) }

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.sendFragment, R.id.receiveFragment, R.id.cryptoListFragment, R.id.optionsFragment))
        if (navController != null) {
            setupActionBarWithNavController(navController,appBarConfiguration)
        }

        createNotification()

        val firingCal: Calendar = Calendar.getInstance()
        firingCal.set(Calendar.HOUR_OF_DAY, 10)
        firingCal.set(Calendar.MINUTE, 0)
        firingCal.set(Calendar.SECOND, 0)

        if (Calendar.getInstance().after(firingCal)) {
            firingCal.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(this, ReminderBroadcast::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            firingCal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                firingCal.timeInMillis,
                pendingIntent
            )
        }
    }


    private fun createNotification() {
        val name = "HerculesWallet"
        val descriptionText = getString(R.string.description_notification) + " ..."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
            enableVibration(true)
            vibrationPattern = longArrayOf(100,1000,200,340)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBackPressed(){
        return
    }

}


