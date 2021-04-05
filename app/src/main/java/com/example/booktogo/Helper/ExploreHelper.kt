package com.example.booktogo.Helper

class ExploreHelper {
    var exploreCity : String? = null
    var district : String? = null
    var imageDistrict : String? = null
    var title : String? = null

    constructor()

    companion object {
        var instance: ExploreHelper = ExploreHelper()

        @JvmName("getInstance1")
        fun getInstance(): ExploreHelper {
            return instance
        }
    }
}