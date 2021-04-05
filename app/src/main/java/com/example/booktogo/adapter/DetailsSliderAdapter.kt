package com.example.booktogo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.booktogo.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_image_slider_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsSliderAdapter(var list : ArrayList<String>, var context: Context) : SliderViewAdapter<DetailsSliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder : SliderViewAdapter.ViewHolder {
        var img_auto_image_slider : ImageView

        constructor(itemview : View) : super(itemview) {
            img_auto_image_slider = itemview.img_auto_image_slider
        }
    }


    override fun getCount(): Int {
        return  list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_image_slider_view,parent,false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        decodedBitmap(list[position], viewHolder!!)
    }

    fun decodedBitmap(source : String, holder : DetailsSliderAdapter.SliderViewHolder){
        GlobalScope.launch(Dispatchers.IO){
            val decodedString = Base64.decode(source, Base64.DEFAULT)
            val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            withContext(Dispatchers.Main){
                holder.img_auto_image_slider.setImageBitmap(decodedByte)
            }
        }
    }
}