package com.example.booktogo.adapter

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
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.example.booktogo.customView.RoundRectCornerImageView
import com.example.booktogo.model.PlaceExplore
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_explore.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExploreAdapter(var list: ArrayList<PlaceExplore>, var context: Context) :
    RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {
    lateinit var listener: ExplorerListener

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var img_place: RoundRectCornerImageView
        var tv_placeName: TextView

        constructor(itemView: View) : super(itemView) {
            img_place = itemView.img_place
            tv_placeName = itemView.tv_placeName
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClick(list[bindingAdapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_explore,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val explore : PlaceExplore = list[position]
        decodedBitmap(explore.image,holder)
        holder.tv_placeName.text = explore.title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setExploreClickListener(listener: ExplorerListener) {
        this.listener = listener
    }

    interface ExplorerListener {
        fun onClick(placeExplore: PlaceExplore)
    }
     fun decodedBitmap(source : String, holder : ViewHolder){
         GlobalScope.launch(Dispatchers.IO){
             val decodedString = Base64.decode(source, Base64.DEFAULT)
             val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
             withContext(Dispatchers.Main){
                 holder.img_place.setImageBitmap(decodedByte)
             }
         }
    }
}