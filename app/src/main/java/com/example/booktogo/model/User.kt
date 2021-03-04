package com.example.booktogo.model

data class User(
    var email: String, var userName: String, var passWord: String, var firstname: String,
    var lastname: String,
    var age: Int,
    var gender: String,
    var phone: String
)