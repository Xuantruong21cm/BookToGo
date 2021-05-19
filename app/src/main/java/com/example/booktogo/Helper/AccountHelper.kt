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
    var avatar : String? = null


    constructor()

    companion object {
        var instance: AccountHelper = AccountHelper()
    }
}