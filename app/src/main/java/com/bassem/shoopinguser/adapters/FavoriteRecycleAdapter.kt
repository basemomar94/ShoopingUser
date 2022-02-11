package com.bassem.shoopinguser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.FavoriteClass

class FavoriteRecycleAdapter(
    val favoriteList: MutableList<FavoriteClass>,
    val removelistner: removeInterface
) : RecyclerView.Adapter<FavoriteRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val title = itemview.findViewById<TextView>(R.id.favoriteTitle)
        val price = itemview.findViewById<TextView>(R.id.favoritePrice)
        val remove = itemview.findViewById<ImageView>(R.id.favoriteRemove)

        init {
            remove.setOnClickListener {
                val i: Int = adapterPosition
                removelistner.remove(i)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteItem = favoriteList[position]
        holder.price.text = favoriteItem.price.toString()
        holder.title.text = favoriteItem.title
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    interface removeInterface {
        fun remove(position: Int)
    }
}