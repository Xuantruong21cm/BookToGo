package com.example.booktogo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktogo.R
import kotlinx.android.synthetic.main.item_all_image.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllDetailsImageAdapter(var list : ArrayList<String>, var context: Context) : RecyclerView.Adapter<AllDetailsImageAdapter.ViewHolder>() {
    lateinit var listener : AllImageClickListener

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener{
        var img_details : ImageView
        constructor(itemView : View) : super(itemView){
            img_details = itemView.img_details
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClick(bindingAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_all_image,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        decodedBitmap(list[position],holder)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setAllImageOnClick(listener : AllImageClickListener){
        this.listener = listener
    }

    interface AllImageClickListener{
        fun onClick(position: Int)
    }

    fun decodedBitmap(source : String, holder : AllDetailsImageAdapter.ViewHolder){
        GlobalScope.launch(Dispatchers.IO){
            val decodedString = Base64.decode(source, Base64.DEFAULT)
            val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            withContext(Dispatchers.Main){
                holder.img_details.setImageBitmap(decodedByte)
            }
        }
    }
}