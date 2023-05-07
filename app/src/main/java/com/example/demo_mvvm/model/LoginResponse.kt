package com.example.demo_mvvm.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("contact_number")
	val contactNumber: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("user_device_id")
	val userDeviceId: String? = null,

	@field:SerializedName("fcm_token")
	val fcmToken: String? = null,

	@field:SerializedName("student_flag")
	val studentFlag: String? = null,

	@field:SerializedName("is_notification")
	val isNotification: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("country_code_name")
	val countryCodeName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("login_type")
	val login_type: String? = null,

	@field:SerializedName("refral_code")
	val refralCode: String? = null,

	)
