package com.example.booktogo.Helper

class TripHelper {
    var city : String? = null
    var district : String? = null
    var days : String? = null
    var adults : String? = null
    var children : String? = null
    var cityHotel : String? = null
    companion object{
        var instance : TripHelper = TripHelper()
    }
}