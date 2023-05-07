package com.example.demo_mvvm.repositories

import androidx.lifecycle.LiveData
import com.example.demo_mvvm.model.APIResponse

interface Repository {
    suspend fun doTaskAndReturnResponse() : LiveData<APIResponse>
}