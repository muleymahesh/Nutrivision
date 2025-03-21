package com.maks.nutrivision.data.entities

data class LoginRequest(    val method: String, val email: String, val password: String)
data class ForgotPasswordRequest(val method: String, val email: String, val mobile: String)
data class SignupRequest(
    val method: String,
    val fname: String,
    val lname: String,
    val email: String,
    val password: String,
    val mobile: String,
    val device_token: String,
    val address: String,
    val pincode: String

)
data class AuthResponse(
    val result: String,
    val responseMessage: String,
    val fname: String,
    val lname: String,
    val mobile: String,
    val order_Count: Int,
    val user_email: String,
    val user_id: String,
    val address: String,
    val pincode: String
){
    fun toProfile(): Profile{
        return Profile(user_id,fname,lname,mobile,order_Count,user_email,address,pincode = pincode)
    }

}