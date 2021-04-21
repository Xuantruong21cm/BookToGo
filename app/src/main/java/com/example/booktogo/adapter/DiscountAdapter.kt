package com.example.booktogo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktogo.R
import com.example.booktogo.model.Discount
import kotlinx.android.synthetic.main.item_discount.view.*

class DiscountAdapter(var list : ArrayList<Discount>) : RecyclerView.Adapter<DiscountAdapter.ViewHolder>() {

    inner class ViewHolder : RecyclerView.ViewHolder{
        var tv_discountName : TextView
        var tv_percentDiscount : TextView
        var img_pick_discount : ImageView

        constructor(itemView : View) : super(itemView){
            tv_discountName = itemView.tv_discountName
            tv_percentDiscount = itemView.tv_percentDiscount
            img_pick_discount = itemView.img_pick_discount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_discount,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_discountName.text = list[position].title
        holder.tv_percentDiscount.text = list[position].percent + "% OFF"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}