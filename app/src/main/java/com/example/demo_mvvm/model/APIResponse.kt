package com.example.demo_mvvm.model


//create sealed class for base response
sealed class APIResponse{

    object Loading : APIResponse()

    data class Success<T>(val data: T?) : APIResponse()

    data class APIError( val errorCode : Int, val message : String) : APIResponse()

}
