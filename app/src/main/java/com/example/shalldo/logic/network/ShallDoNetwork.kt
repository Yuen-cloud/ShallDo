package com.example.shalldo.logic.network

import com.example.shalldo.logic.model.Account
import com.example.shalldo.logic.model.LoginResponse
import com.example.shalldo.logic.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ShallDoNetwork {
    private val loginService = ServiceCreator.create(LoginService::class.java)
    suspend fun login(username: String, password: String): LoginResponse =
        loginService.login("login", username, password).await()

    private val registerService = ServiceCreator.create(RegisterService::class.java)
    suspend fun register(username: String, password: String): RegisterResponse =
        registerService.register("register", username, password).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null)
                        continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}