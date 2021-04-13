package com.example.booktogo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
 data class Hotel (
    @ColumnInfo(name = "addressHotel")var addressHotel: String?,
    @PrimaryKey var idHotel : String,
    @ColumnInfo(name = "levelHotel")var levelHotel : String?,
    @ColumnInfo(name = "nameHotel")var nameHotel : String?,
    @ColumnInfo(name = "priceRange")var priceRange : String?,
    @ColumnInfo(name = "lat")var lat : String?,
    @ColumnInfo(name = "lng")var lng : String?,
    @ColumnInfo(name = "details1")var details1 : String?,
    @ColumnInfo(name = "details2")var details2 : String?,
    @ColumnInfo(name = "details3")var details3 : String?,
    @ColumnInfo(name = "details4")var details4 : String?,
    @ColumnInfo(name = "details5")var details5 : String?,
    @ColumnInfo(name = "details6")var details6 : String?,
    @ColumnInfo(name = "details7")var details7 : String?,
    @ColumnInfo(name = "details8")var details8 : String?,
    @ColumnInfo(name = "details9")var details9 : String?,
    @ColumnInfo(name = "nearby1") var nearby1 : String?,
    @ColumnInfo(name = "nearby2")var nearby2 : String?,
    @ColumnInfo(name = "nearby3")var nearby3 : String?,
    @ColumnInfo(name = "nearby4")var nearby4 : String?,
    @ColumnInfo(name = "nearby5")var nearby5 : String?,
    @ColumnInfo(name = "clean_room")var clean_room : String?,
    @ColumnInfo(name = "elevator")var elevator : String?,
    @ColumnInfo(name = "family")var family : String?,
    @ColumnInfo(name = "free_wifi")var free_wifi : String?,
    @ColumnInfo(name = "hot_tub")var hot_tub : String?,
    @ColumnInfo(name = "laundry")var laundry : String?,
    @ColumnInfo(name = "reception")var reception : String?,
    @ColumnInfo(name = "security_camera")var security_camera : String?,
    @ColumnInfo(name = "smoke")var smoke : String?
)