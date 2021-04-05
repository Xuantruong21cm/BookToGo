package com.example.booktogo.Helper

class HotelHelper {
    var addressHotel : String? = null
    var idHotel : String? = null
    var lat : String? = null
    var lng : String? = null
    var levelHotel : String? = null
    var nameHotel : String? = null
    var priceRange : String? = null
    var details1 : String? =null
    var details2 : String? =null
    var details3 : String? =null
    var details4 : String? =null
    var details5 : String? =null
    var details6 : String? =null
    var details7 : String? =null
    var details8 : String? =null
    var details9 : String? =null
    var detailsList : ArrayList<String>? = null
    var detailsPotision : Int? = null
    var nearby1 : String? = null
    var nearby2 : String? = null
    var nearby3 : String? = null
    var nearby4 : String? = null
    var nearby5 : String? = null
    var clean_room : String? = null
    var elevator : String? = null
    var family : String? = null
    var free_wifi : String? = null
    var hot_tub : String? = null
    var laundry : String? = null
    var reception : String? = null
    var security_camera : String? = null
    var smoke : String? = null

    var myLat : Double? = null
    var myLng : Double? = null

    constructor()

    companion object{
        var instance : HotelHelper = HotelHelper()
    }
}