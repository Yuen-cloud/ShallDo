package com.example.shalldo.logic.network

import android.provider.ContactsContract
import com.example.shalldo.logic.model.Account
import com.example.shalldo.logic.model.LoginResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface LoginService {
    @Headers("x-requested-with: XMLHttpRequest")
    @GET("deal_servlet_action")
    fun login(
        @Query("action") action: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<LoginResponse>
}