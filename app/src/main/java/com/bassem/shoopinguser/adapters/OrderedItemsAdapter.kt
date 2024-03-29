package com.bassem.shoopinguser.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.OrderedItem
import com.bumptech.glide.Glide

class OrderedItemsAdapter(
    val orderedList: MutableList<OrderedItem>,
    val context: Context,
    val countList: MutableList<String>,
    val lisnter: orderInterFace

) : RecyclerView.Adapter<OrderedItemsAdapter.ViewHolder>() {


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val photo = itemview.findViewById<ImageView>(R.id.orderedPhoto)
        val title = itemview.findViewById<TextView>(R.id.orderedTitle)
        val price = itemview.findViewById<TextView>(R.id.orderedPrice)
        val count = itemview.findViewById<TextView>(R.id.orderedCount)

        init {
            itemview.setOnClickListener {
                val position = adapterPosition
                lisnter.viewItem(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ordered_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ordered = orderedList[position]
        holder.title.text = ordered.title
        holder.price.text = ordered.price + " EGP"
        holder.count.text = countList[position]
        val url = ordered.photo
        Glide.with(context).load(url).into(holder.photo)
    }

    override fun getItemCount(): Int {
        return orderedList.size
    }

    interface orderInterFace {
        fun viewItem(position: Int)
    }


}