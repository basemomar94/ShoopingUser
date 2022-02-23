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
    val expandListner: expandInterface,
) : RecyclerView.Adapter<HomeRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val image = itemview.findViewById<ImageView>(R.id.itemImg)
        val title = itemview.findViewById<TextView>(R.id.itemTitle)
        val price = itemview.findViewById<TextView>(R.id.itemPrice)
        val favorite = itemview.findViewById<ImageView>(R.id.favoriteItemView)
        val sold = itemview.findViewById<ImageView>(R.id.soldImg)

        init {

            favorite.setOnClickListener {
                val positionFavorite = adapterPosition

                expandListner.makeFavorite(positionFavorite)
            }
            itemview.setOnClickListener {
                val position = adapterPosition

                expandListner.viewItem(position)
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
        if (item.amount!! <= 0) {
            holder.itemView.isClickable = false
            holder.itemView.alpha = .5F
            holder.sold.visibility = View.VISIBLE
        }
        val url = item.photo
        Glide.with(context).load(url).into(holder.image)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    interface expandInterface {
        fun makeFavorite(postion: Int)
        fun viewItem(position: Int)


    }


}