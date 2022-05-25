package com.example.shalldo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shalldo.logic.dao.MyDatabaseHelper
import com.example.shalldo.logic.model.User
import com.example.shalldo.ui.doTasks.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class MenuActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        val dbHelper = MyDatabaseHelper(ShallDoApplication.context, User.getId().toString() + "_ShallDo.db", 2)
        val db = dbHelper.writableDatabase
        val INTENT_ALARM_ALERT = "intent_alarm_alert"
        val INTENT_ALARM_RESET = "intent_alarm_reset"
    }

    private var exitTime: Long = 0
    private lateinit var receiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_todolist, R.id.navigation_do_tasks, R.id.navigation_my_info
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setReceiver()
        setAlarm()
    }

    private fun setReceiver() {
        receiver = AlarmReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(INTENT_ALARM_ALERT)
        intentFilter.addAction(INTENT_ALARM_RESET)
        registerReceiver(receiver, intentFilter)
        receiver.setOnDealListener(object : AlarmReceiver.OnDealListener {
            override fun dealAlert() {
                val cursor = db.rawQuery("select count(*) from tasks where state = 0", null)
                cursor.moveToFirst()
                val sum = cursor.getLong(0)
                setNotification(sum)
            }

            @SuppressLint("Range")
            override fun dealReset() {
                db.beginTransaction()
                val cursor = db.rawQuery("select * from tasks", null)
                var sum = 0L
                val date = Date(System.currentTimeMillis() - 24 * 3600 * 1000)
                val format = SimpleDateFormat("yyyy-MM-dd")
                val dateStr = format.format(date)
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(cursor.getColumnIndex("id"))
                        val state = cursor.getInt(cursor.getColumnIndex("state"))
                        db.execSQL("insert into sit(id, time, state) values(?, ?, ?)", arrayOf(id, dateStr, state))
                        sum++
                    } while (cursor.moveToNext())
                }
                db.execSQL("update tasks set state = 0")
                db.setTransactionSuccessful()
                db.endTransaction()
                setNotification(sum)
            }
        })
    }

    private fun setNotification(sum: Long) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, "normal")
            .setContentTitle("今日还有${sum}个任务待完成")
            .setSmallIcon(R.drawable.alert)
            .build()
        manager.notify(1, notification)
    }

    private fun setAlarm() {
        val alarmAlert = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmReset = getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val timeAlert = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val timeReset = calendar.timeInMillis
        val intentAlert = Intent(INTENT_ALARM_ALERT)
        val intentReset = Intent(INTENT_ALARM_RESET)
        val pendingIntentAlert = PendingIntent.getBroadcast(this, 0, intentAlert, 0)
        val pendingIntentReset = PendingIntent.getBroadcast(this, 0, intentReset, 0)
        val gap: Long = 24 * 60 * 60 * 1000
        alarmAlert.setRepeating(AlarmManager.RTC_WAKEUP, timeAlert, gap, pendingIntentAlert)
        alarmReset.setRepeating(AlarmManager.RTC_WAKEUP, timeReset, gap, pendingIntentReset)
    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}