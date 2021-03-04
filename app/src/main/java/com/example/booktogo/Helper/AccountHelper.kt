package com.example.booktogo.Helper

class AccountHelper {
    var email: String? = null
    var userName: String? = null
    var passWord: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var age: Int? = null
    var gender: String? = null
    var phone : String? = null
    var codeOTP : String? = null

    constructor()

    companion object {
        var instance: AccountHelper = AccountHelper()

        @JvmName("getInstance1")
        fun getInstance(): AccountHelper {
            return instance
        }
    }

    @JvmName("getEmail1")
    fun getEmail(): String {
        return email!!
    }

    @JvmName("getUsername1")
    fun getUsername(): String {
        return userName!!
    }

    @JvmName("getPassword1")
    fun getPassword(): String {
        return passWord!!
    }

    @JvmName("getFirstname1")
    fun getFirstname(): String {
        return firstname!!
    }

    @JvmName("getLastname1")
    fun getLastname(): String {
        return lastname!!
    }

    @JvmName("getAge1")
    fun getAge(): Int {
        return age!!
    }

    @JvmName("getGender1")
    fun getGender(): String {
        return gender!!
    }

    @JvmName("getPhone1")
    fun getPhone() : String{
        return phone!!
    }
}