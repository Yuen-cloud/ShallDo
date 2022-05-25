package com.example.shalldo.logic.network

import com.example.shalldo.logic.model.LoginResponse
import com.example.shalldo.logic.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RegisterService {
    @Headers("x-requested-with: XMLHttpRequest")
    @GET("deal_servlet_action")
    fun register(
        @Query("action") action: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<RegisterResponse>
}