package com.example.booktogo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HotelDAO {
    @Query("SELECT * FROM favourite")
    fun getAll() : MutableList<Hotel>

    @Query("SELECT * FROM favourite WHERE idHotel = :idHotel")
    fun findById(idHotel : String) : Hotel

    @Insert
    fun insertAll(vararg hotel: Hotel)

    @Delete
    fun delete(hotel: Hotel)
}