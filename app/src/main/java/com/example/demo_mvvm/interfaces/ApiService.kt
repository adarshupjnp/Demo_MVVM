package com.example.demo_mvvm.interfaces

import com.example.demo_mvvm.model.LoginRequest
import com.example.demo_mvvm.model.LoginResponse
import com.example.demo_mvvm.retrofit.RestConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(RestConstant.LOGIN)
    suspend fun loginApi(@Body body: LoginRequest) : LoginResponse?
}