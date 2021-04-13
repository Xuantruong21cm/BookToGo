package com.example.booktogo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.booktogo.R
import com.example.booktogo.room.Hotel
import kotlinx.android.synthetic.main.item_hotel_favourite.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class HotelFavouriteAdapter(var list : List<Hotel>, var context: Context) :
    RecyclerView.Adapter<HotelFavouriteAdapter.ViewHolder>() {
    lateinit var listener: HotelViewListener

    inner class ViewHolder : RecyclerView.ViewHolder,View.OnClickListener{
        var img_thumnail_explore : ImageView
        var tv_hotelName_explore : TextView
        var tv_level_hotel_explore : TextView
        var tv_price_explore : TextView
        var img_remove_favourite : ImageView

        constructor(itemView: View) : super(itemView){
            img_thumnail_explore = itemView.img_thumnail_explore
            tv_hotelName_explore = itemView.tv_hotelName_explore
            tv_level_hotel_explore = itemView.tv_level_hotel_explore
            tv_price_explore = itemView.tv_price_explore
            img_remove_favourite = itemView.img_remove_favourite
            img_remove_favourite.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v){
               itemView -> {
                   listener.HotelViewwOnClick(list[adapterPosition])
                }
                img_remove_favourite -> {
                    listener.DeleteFavourite(list[adapterPosition],adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_favourite,parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return  list.size
    }
    fun decodedBitmap(source : String, holder : ViewHolder){
        GlobalScope.launch(Dispatchers.IO){
            val decodedString = Base64.decode(source, Base64.DEFAULT)
            val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            withContext(Dispatchers.Main){
                holder.img_thumnail_explore.setImageBitmap(decodedByte)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotel : Hotel = list[position]
        decodedBitmap(hotel.details1!!,holder)
        holder.tv_hotelName_explore.text = hotel.nameHotel
        holder.tv_level_hotel_explore.text = hotel.levelHotel
        val decimalFormat = DecimalFormat("###,###,###")
        holder.tv_price_explore.text =decimalFormat.format(hotel.priceRange!!.toInt()) + " đ"
    }
    fun setHotelViewAdapter(listener : HotelViewListener){
        this.listener = listener
    }

    interface HotelViewListener{
        fun HotelViewwOnClick(hotelExplore: Hotel)
        fun DeleteFavourite(hotel : Hotel,position: Int)
    }
}