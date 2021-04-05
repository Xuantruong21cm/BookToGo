package com.example.booktogo.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktogo.R
import kotlinx.android.synthetic.main.item_nearby.view.*

class NearbyAdapter(var list : ArrayList<String>) : RecyclerView.Adapter<NearbyAdapter.ViewHolder>() {
    inner class ViewHolder : RecyclerView.ViewHolder {
        var tv_location_NearBy : TextView
        constructor(itemView : View) : super(itemView){
            tv_location_NearBy = itemView.tv_location_NearBy
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_nearby,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_location_NearBy.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

}