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
import androidx.recyclerview.widget.RecyclerView
import com.example.booktogo.R
import com.example.booktogo.model.HotelExplore
import kotlinx.android.synthetic.main.item_hotel_explore.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HotelViewAdapter(var list: ArrayList<HotelExplore>, var context: Context) :
    RecyclerView.Adapter<HotelViewAdapter.ViewHolder>() {
    lateinit var listener: HotelViewListener

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var img_thumnail_explore: ImageView
        var tv_hotelName_explore: TextView
        var tv_level_hotel_explore: TextView
        var tv_price_explore: TextView

        constructor(itemView: View) : super(itemView) {
            img_thumnail_explore = itemView.img_thumnail_explore
            tv_hotelName_explore = itemView.tv_hotelName_explore
            tv_level_hotel_explore = itemView.tv_level_hotel_explore
            tv_price_explore = itemView.tv_price_explore
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.HotelViewwOnClick(list[bindingAdapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_explore, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun decodedBitmap(source: String, holder: ViewHolder) {
        GlobalScope.launch(Dispatchers.IO) {
            val decodedString = Base64.decode(source, Base64.DEFAULT)
            val decodedByte: Bitmap =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            withContext(Dispatchers.Main) {
                holder.img_thumnail_explore.setImageBitmap(decodedByte)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotelExplore: HotelExplore = list[position]
        decodedBitmap(hotelExplore.details1, holder)
        holder.tv_hotelName_explore.text = hotelExplore.nameHotel
        holder.tv_level_hotel_explore.text = hotelExplore.levelHotel
        val decimalFormat = DecimalFormat("###,###,###")
        holder.tv_price_explore.text = decimalFormat.format(hotelExplore.priceRange.toInt()) + " đ"

    }

    fun setHotelViewAdapter(listener: HotelViewListener) {
        this.listener = listener
    }

    interface HotelViewListener {
        fun HotelViewwOnClick(hotelExplore: HotelExplore)
    }
}