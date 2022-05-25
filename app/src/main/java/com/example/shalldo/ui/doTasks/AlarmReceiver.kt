package com.example.shalldo.ui.doTasks

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.shalldo.MenuActivity
import com.example.shalldo.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == MenuActivity.INTENT_ALARM_ALERT) {
            mOnDealListener?.dealAlert()
        } else if (action == MenuActivity.INTENT_ALARM_RESET) {
            mOnDealListener?.dealReset()
        }
    }

    interface OnDealListener {
        fun dealReset()
        fun dealAlert()
    }

    private var mOnDealListener: OnDealListener? = null

    public fun setOnDealListener(onDealListener: OnDealListener) {
        mOnDealListener = onDealListener
    }
}