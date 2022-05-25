package com.example.shalldo.logic.model

data class Account(val userId: Int, val username: String, val password: String)
data class LoginResponse(val resultCode: Int, val resultMsg: String, val account: Account)
data class RegisterResponse(val resultCode: Int, val resultMsg: String)
data class Task(val id: Int, val name: String, val last: Int, val state: Int)
data class Sit(val finishedList: ArrayList<Int>, val unfinishedList: ArrayList<Int>, val dateList: ArrayList<String>)

object User {
    private var userId = -1
    private var username = ""
    private var password = ""
    fun setUser(userId: Int, username: String, password: String) {
        this.userId = userId
        this.username = username
        this.password = password
    }

    fun getId(): Int {
        return userId
    }

    fun getName(): String {
        return username
    }
}