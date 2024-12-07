package com.example.gameproject.model

data class RegisterData(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var age: Int = 0,
    var parentCode: String = "" // Only needed if age < 18
)
