package com.bassem.shoopinguser.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.ItemsClass
import com.bumptech.glide.Glide

class HomeRecycleAdapter(
    var itemsList: MutableList<ItemsClass>,
    val context: Context,
    val expandListner: expandInterface,
) : RecyclerView.Adapter<HomeRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val image = itemview.findViewById<ImageView>(R.id.itemImg)
        val title = itemview.findViewById<TextView>(R.id.itemTitle)
        val price = itemview.findViewById<TextView>(R.id.itemPrice)
        val favorite = itemview.findViewById<ImageView>(R.id.favoriteItemView)
        val cartImag = itemview.findViewById<ImageView>(R.id.cartItemView)
        val favCard = itemview.findViewById<CardView>(R.id.favCart)
        val cartCard = itemview.findViewById<CardView>(R.id.cartCard)

        val sold = itemview.findViewById<ImageView>(R.id.soldImg)

        init {

            favCard.setOnClickListener {
                val positionFavorite = adapterPosition
                val id = itemsList[positionFavorite].id
                val item = itemsList[positionFavorite]
                expandListner.makeFavorite(id!!, positionFavorite, true, item)
            }
            itemview.setOnClickListener {
                val position = adapterPosition
                val item = itemsList[position].id
                expandListner.viewItem(item!!)
            }
            cartCard.setOnClickListener {
                val position = adapterPosition
                val id = itemsList[position].id
                val item = itemsList[position]
                expandListner.addCart(id!!, position, item)

            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        val itemPostien = position
        holder.title.text = item.title
        holder.price.text = "${item.price} EGP"
        val favorite = getDrawable(context, R.drawable.favoritered)
        val unfavorite = getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
        val addcart = getDrawable(context, R.drawable.ic_baseline_add_shopping_cart_24)
        val incart = getDrawable(context, R.drawable.addedcart)

        if (item.favorite) {
            holder.favorite.setImageDrawable(favorite)
        } else {
            holder.favorite.setImageDrawable(unfavorite)

        }
        if (item.cart) {
            holder.cartCard.setCardBackgroundColor(Color.parseColor("#FFA56D"))
            holder.cartImag.setImageDrawable(incart)
        } else {
            holder.cartCard.setCardBackgroundColor(Color.WHITE)
            holder.cartImag.setImageDrawable(addcart)
        }
        if (item.amount!! <= 0) {
            holder.itemView.isClickable = false
            holder.itemView.alpha = .5F
            holder.sold.visibility = View.VISIBLE
        }
        if (item.visible!!) {
            holder.itemView.visibility = View.VISIBLE
        } else {
            holder.itemView.visibility = View.GONE


        }
        val url = item.photo
        Glide.with(context).load(url).into(holder.image)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun filter(filterList: MutableList<ItemsClass>) {
        itemsList = filterList
        notifyDataSetChanged()
    }

    interface expandInterface {
        fun makeFavorite(id: String, position: Int, fav: Boolean, item: ItemsClass)
        fun viewItem(item: String)
        fun addCart(id: String, position: Int, item: ItemsClass)


    }


}