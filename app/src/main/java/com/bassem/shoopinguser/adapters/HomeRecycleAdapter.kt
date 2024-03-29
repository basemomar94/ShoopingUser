package com.bassem.shoopinguser.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
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
                val category = itemsList[position].category
                expandListner.viewItem(item!!, category!!, position)
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        val itemPostien = position
        holder.title.text = item.title
        holder.price.text = "${item.price} EGP"
        val favorite = getDrawable(context, R.drawable.favorange)
        val unfavorite = getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
        val addcart = getDrawable(context, R.drawable.ic_baseline_add_shopping_cart_24)
        val incart = getDrawable(context, R.drawable.addedcart)
        val green = R.color.greenPrimary


        if (item.favorite) {
            holder.favCard.setCardBackgroundColor(context.getColorStateList(green))
            holder.favorite.setImageDrawable(favorite)
        } else {
            holder.favorite.setImageDrawable(unfavorite)
            holder.favCard.setCardBackgroundColor(Color.WHITE)


        }
        if (item.cart) {
            holder.cartCard.setCardBackgroundColor(context.getColorStateList(green))
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

        val url = item.photo
        Glide.with(context).load(url)
            .placeholder(R.drawable.picture).into(holder.image)

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
        fun viewItem(item: String, category: String, position: Int)
        fun addCart(id: String, position: Int, item: ItemsClass)


    }


}