package com.example.booktogo.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Hotel::class), version = 1)
abstract class HotelDatabase : RoomDatabase() {
    abstract fun hotelDAO() : HotelDAO
}