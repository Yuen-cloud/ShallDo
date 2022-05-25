package com.example.shalldo.logic.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createTasks = "create table tasks ( " +
            "id integer primary key autoincrement, " +
            "name text, " +
            "last integer, " +
            "state integer)"
    private val createSit = "create table sit ( " +
            "sitId integer primary key autoincrement, " +
            "id integer, " +
            "time date, " +
            "state integer)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTasks)
        db.execSQL(createSit)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {
            db.execSQL("drop table if exists sit")
            db.execSQL(createSit)
        }
    }
}