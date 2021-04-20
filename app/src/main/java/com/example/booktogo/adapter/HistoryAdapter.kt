package com.example.booktogo.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktogo.R
import com.example.booktogo.customView.RoundRectCornerImageView
import com.example.booktogo.model.BookingHistory
import kotlinx.android.synthetic.main.item_booking_history.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class HistoryAdapter(var list : ArrayList<BookingHistory>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder : RecyclerView.ViewHolder{
        var img_avatar_history : RoundRectCornerImageView
        var tv_hotelName_history : TextView
        var tv_address_history : TextView
        var tv_date_history : TextView
        var tv_days_history : TextView
        var tv_price_history : TextView

        constructor(itemView : View) : super(itemView){
            img_avatar_history = itemView.img_avatar_history
            tv_hotelName_history = itemView.tv_hotelName_history
            tv_address_history = itemView.tv_address_history
            tv_date_history= itemView.tv_date_history
            tv_days_history = itemView.tv_days_history
            tv_price_history = itemView.tv_price_history
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_booking_history,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history : BookingHistory = list[position]
        decodedBitmap(history.avatarHotel,holder)
        holder.tv_hotelName_history.text = history.hotelName
        holder.tv_address_history.text = history.addressHotel
        holder.tv_date_history.text = history.date
        holder.tv_days_history.text = history.days + " days"
        val decimalFormat = DecimalFormat("###,###,###")
        holder.tv_price_history.text = decimalFormat.format(history.price.toInt()) + " đ"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun decodedBitmap(source : String, holder : HistoryAdapter.ViewHolder){
        GlobalScope.launch(Dispatchers.IO){
            val decodedString = Base64.decode(source, Base64.DEFAULT)
            val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            withContext(Dispatchers.Main){
                holder.img_avatar_history.setImageBitmap(decodedByte)
            }
        }
    }
}