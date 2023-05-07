package com.example.demo_mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demo_mvvm.model.APIResponse
import com.example.demo_mvvm.model.LoginRequest
import com.example.demo_mvvm.repositories.MainRepository

class LoginViewModel :ViewModel () {
    var emailaddress : MutableLiveData<String> = MutableLiveData()
    var passwordaddress : MutableLiveData<String> = MutableLiveData()


    suspend fun apilogin() : LiveData<APIResponse> = MainRepository(getLoginData()).doTaskAndReturnResponse()

    private fun getLoginData() : LoginRequest {
        return LoginRequest(
            email_id = emailaddress.value.toString().trim(),
            password = passwordaddress.value.toString().trim(),
            user_device_id = "1",
            fcm_token = "1",
            user_type = "user",
            branch_id = "",
            restaurant_id = "289",
            is_student = "N"
        )
    }
}