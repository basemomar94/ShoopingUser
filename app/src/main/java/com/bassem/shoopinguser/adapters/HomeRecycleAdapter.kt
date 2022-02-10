package com.bassem.shoopinguser.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.ItemsClass
import com.bumptech.glide.Glide

class HomeRecycleAdapter(
    val itemsList: MutableList<ItemsClass>,
    val context: Context,
    val favoriteListner: favoriteInterface,
    val viewListner: expandView
) : RecyclerView.Adapter<HomeRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val image = itemview.findViewById<ImageView>(R.id.itemImg)
        val title = itemview.findViewById<TextView>(R.id.itemTitle)
        val price = itemview.findViewById<TextView>(R.id.itemPrice)
        val favorite = itemview.findViewById<ImageView>(R.id.favoriteItemView)

        init {
            val position = adapterPosition

            favorite.setOnClickListener {
                val positionFavorite = adapterPosition

                favoriteListner.makeFavorite(positionFavorite)
            }
            itemview.setOnClickListener {
                viewListner.viewItem(position)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        holder.title.text = item.title
        holder.price.text = "${item.price} EGP"
        val favorite = getDrawable(context, R.drawable.favoritered)
        val unfavorite = getDrawable(context, R.drawable.ic_baseline_favorite_border_24)

        if (item.favorite) {
            holder.favorite.setImageDrawable(favorite)
        } else {
            holder.favorite.setImageDrawable(unfavorite)

        }
        //   val url = item.imageUrl
        // Glide.with(context).load(url).into(holder.image)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    interface favoriteInterface {
        fun makeFavorite(postion: Int)


    }

    interface expandView {
        fun viewItem(position: Int)
    }

}