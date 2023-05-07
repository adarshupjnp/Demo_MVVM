package com.example.demo_mvvm.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(

	@field:SerializedName("email_id")
	val email_id: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("user_device_id")
	val user_device_id: String? = null,

	@field:SerializedName("fcm_token")
	val fcm_token: String? = null,

	@field:SerializedName("user_type")
	val user_type: String? = null,

	@field:SerializedName("branch_id")
	val branch_id: String? = null,

	@field:SerializedName("restaurant_id")
	val restaurant_id: String? = null,

	@field:SerializedName("is_student")
	val is_student: String? = null
)
