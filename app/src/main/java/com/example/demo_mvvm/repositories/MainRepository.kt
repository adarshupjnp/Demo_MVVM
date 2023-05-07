package com.example.demo_mvvm.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.demo_mvvm.model.APIResponse
import com.example.demo_mvvm.model.LoginRequest
import com.example.demo_mvvm.retrofit.RetrofitRestClient
import kotlinx.coroutines.Dispatchers

class MainRepository(val loginData: LoginRequest) : Repository {
    override suspend fun doTaskAndReturnResponse(): LiveData<APIResponse> =
        liveData(Dispatchers.IO) {
            try {

                //(loginData are initial and carry request like email, password parameter)
                Log.e("logindata", loginData.toString())

                // Api call
                val response = RetrofitRestClient.getInstance().loginApi(loginData)

                if (response!!.code == 200){
                    emit(APIResponse.Success(response))
                }

//                else if (response.code == 201){
//                    emit(APIResponse.APIError(response.code, response.message!!))
//                }else if (response.code == 202){
//                    emit(APIResponse.APIError(response.code, response.message!!))
//                }else if (response.code == 999){
//                    emit(APIResponse.APIError(response.code, response.message!!))
//                }

                else{
                    emit(APIResponse.APIError(response.code!!, response.message!!))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(APIResponse.APIError(404, e.localizedMessage))
            }
        }
}
