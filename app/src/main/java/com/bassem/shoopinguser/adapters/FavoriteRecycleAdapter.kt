package com.bassem.shoopinguser.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.FavoriteClass
import com.bumptech.glide.Glide

class FavoriteRecycleAdapter(
    val favoriteList: MutableList<FavoriteClass>,
    val removelistner: removeInterface,
    val context: Context
) : RecyclerView.Adapter<FavoriteRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val title = itemview.findViewById<TextView>(R.id.favoriteTitle)
        val price = itemview.findViewById<TextView>(R.id.favoritePrice)
        val remove = itemview.findViewById<ImageView>(R.id.favoriteRemove)
        val photo = itemview.findViewById<ImageView>(R.id.favoriteImg)
        val addCart= itemview.findViewById<Button>(R.id.addCartFav)

        init {
            remove.setOnClickListener {
                val i: Int = adapterPosition
                removelistner.remove(i)
            }
            addCart.setOnClickListener {
                val i: Int = adapterPosition
                removelistner.addtoCart(i)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteItem = favoriteList[position]
        holder.price.text = favoriteItem.price.toString() + " EGP"
        holder.title.text = favoriteItem.title
        val url = favoriteItem.photo
        Glide.with(context).load(url).into(holder.photo)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    interface removeInterface {
        fun remove(position: Int)
        fun addtoCart(position: Int)
    }
}