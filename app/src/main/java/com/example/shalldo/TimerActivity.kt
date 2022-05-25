package com.example.shalldo

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.lang.StringBuilder
import kotlin.concurrent.thread

class TimerActivity : AppCompatActivity() {
    private lateinit var timerText: TextView
    private var isPause = true
    private var timer: Long = 0L
    private var id: Int = 0
    private var name: String? = null
    private var timerStr: String = ""
    private var used: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        timer = (intent.getIntExtra("last", 0) * 60 * 1000).toLong()
        id = intent.getIntExtra("id", -1)
        timerText = findViewById(R.id.timer)
        name = intent.getStringExtra("name")
        timerText.text = format()
        isPause = false
        thread {
            while (timer != 0L) {
                Thread.sleep(1000)
                if (!isPause) {
                    timer -= 1000
                    timerStr = format()
                    timerText.text = timerStr
                }
            }
            isPause = true
            finishTask()
        }
    }

    private fun finishTask() {
        if (id != -1)
            MenuActivity.db.execSQL("update tasks set state = 1 where id = ?", arrayOf(id))
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, "normal")
            .setContentTitle("\"${name}\"已经完成")
            .setSmallIcon(R.drawable.finished)
            .build()
        manager.notify(1, notification)
        finish()
    }

    private fun format(): String {
        val seconds: Int = (timer / 1000).toInt()
        val hour: Int = seconds / 3600
        val minute: Int = (seconds - hour * 3600) / 60
        val second: Int = seconds - hour * 3600 - minute * 60
        var str = ""
        str += if (hour < 10)
            "0$hour"
        else
            hour.toString()
        str += ":"
        str += if (minute < 10)
            "0$minute"
        else
            minute.toString()
        str += ":"
        str += if (second < 10)
            "0$second"
        else
            second.toString()
        return str
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!used) {
            val action = event?.action
            if (action == 2) {
                setDialog()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        setDialog()
    }

    private fun setDialog() {
        used = true
        isPause = true
        val dialog = AlertDialog.Builder(this)
        val text: TextView = TextView(this)
        text.text = "真的不要再坚持下去吗இ௰இ"
        dialog.setView(text)
        dialog.setPositiveButton("确定",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                finish()
            }
        )
        dialog.setNegativeButton("取消",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                isPause = false
                used = false
            }
        )
        dialog.show()
    }
}