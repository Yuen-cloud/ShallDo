package com.example.shalldo.logic

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.liveData
import com.example.shalldo.LoginActivity
import com.example.shalldo.MenuActivity
import com.example.shalldo.ShallDoApplication
import com.example.shalldo.logic.model.Account
import com.example.shalldo.logic.model.Sit
import com.example.shalldo.logic.model.Task
import com.example.shalldo.logic.network.ShallDoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.Dispatcher
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Repository {
    fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        val result = try {
            val loginResponse = ShallDoNetwork.login(username, password)
            Log.d("Login", loginResponse.toString())
            if (loginResponse.resultCode == 0) {
                val account = loginResponse.account
                Result.success(account)
            } else {
                Result.failure(RuntimeException(loginResponse.resultMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun register(username: String, password: String) = liveData(Dispatchers.IO) {
        val result = try {
            val registerResponse = ShallDoNetwork.register(username, password)
            Log.d("Register", registerResponse.toString())
            if (registerResponse.resultCode == 0) {
                Result.success(registerResponse.resultMsg)
            } else {
                Result.failure(RuntimeException(registerResponse.resultMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    @SuppressLint("Range")
    fun queryAllTasks() = liveData(Dispatchers.IO) {
        val result = try {
            val cursor = MenuActivity.db.rawQuery("select * from tasks", null)
            val taskList = ArrayList<Task>()
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("id"))
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val last = cursor.getInt(cursor.getColumnIndex("last"))
                    val state = cursor.getInt(cursor.getColumnIndex("state"))
                    taskList.add(Task(id, name, last, state))
                } while (cursor.moveToNext())
            }
            cursor.close()
            Result.success(taskList)
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun querySit() = liveData(Dispatchers.IO) {
        val result = try {
            val currentTime = System.currentTimeMillis()
            val dateFirst = Date(currentTime - 24 * 3600 * 1000)
            val dateLast = Date(currentTime - 24 * 3600 * 1000 * 7)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val dateFirstStr = format.format(dateFirst)
            val dateLastStr = format.format(dateLast)
            val finishedList = ArrayList<Int>()
            val unfinishedList = ArrayList<Int>()
            val dateList = ArrayList<String>()
            for (i in 7 downTo 1 step 1) {
                dateList.add(format.format(Date(currentTime - 24 * 3600 * 1000 * i)))
            }
            MenuActivity.db.beginTransaction()
            val cursor = MenuActivity.db.rawQuery(
                "select count(*), time from sit where state = 1 and time between ? and ? group by time order by time",
                arrayOf(dateFirstStr, dateLastStr)
            )
            if (cursor.moveToFirst()) {
                do {
                    val num = cursor.getInt(0)
                    val date = cursor.getString(1)
                    for (sDate in dateList) {
                        if (sDate != date)
                            finishedList.add(0)
                        else
                            finishedList.add(num)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            val cursor2 = MenuActivity.db.rawQuery(
                "select count(*), time from sit where state = 0 and time between ? and ? group by time order by time",
                arrayOf(dateFirstStr, dateLastStr)
            )
            if (cursor2.moveToFirst()) {
                do {
                    val num = cursor2.getInt(0)
                    val date = cursor2.getString(1)
                    for (sDate in dateList) {
                        if (sDate != date)
                            unfinishedList.add(0)
                        else
                            unfinishedList.add(num)
                    }
                } while (cursor2.moveToNext())
            }
            while (finishedList.size != dateList.size)
                finishedList.add(0)
            while (unfinishedList.size != dateList.size)
                unfinishedList.add(0)
            var index = 0
            while (index < dateList.size) {
                dateList[index] = dateList[index].substring(5)
                index++
            }
            cursor2.close()
            MenuActivity.db.setTransactionSuccessful()
            MenuActivity.db.endTransaction()
            Result.success(Sit(finishedList, unfinishedList, dateList))
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}