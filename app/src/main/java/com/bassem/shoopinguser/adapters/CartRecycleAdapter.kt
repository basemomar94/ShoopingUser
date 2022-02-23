package com.bassem.shoopinguser.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.CartClass
import com.bumptech.glide.Glide

class CartRecycleAdapter(
    val cartList: MutableList<CartClass>,
    val removedListener: removeInterface,
    val context: Context
) : RecyclerView.Adapter<CartRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val title = itemview.findViewById<TextView>(R.id.cartTitle)!!
        val price = itemview.findViewById<TextView>(R.id.cartPrice)!!
        val amount = itemview.findViewById<TextView>(R.id.cartAmount)!!
        val remove = itemview.findViewById<ImageView>(R.id.cartRemove)
        val add = itemview.findViewById<CardView>(R.id.addCart)
        val minus = itemview.findViewById<CardView>(R.id.minusCart)
        val photo = itemview.findViewById<ImageView>(R.id.cartPhoto)


        init {
            remove.setOnClickListener {
                val p = adapterPosition
                removedListener.remove(p)
            }
            add.setOnClickListener {
                val p = adapterPosition
                removedListener.add(p)

            }
            minus.setOnClickListener {
                val p = adapterPosition
                removedListener.minus(p)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.title.text = cartItem.title
        holder.amount.text = cartItem.numberOFItems.toString()
        cartItem.currentPrice=((cartItem.numberOFItems) * (cartItem.price)!!.toInt()).toString()
        holder.price.text=cartItem.currentPrice + " EGP"
        val url = cartItem.photo
        Glide.with(context).load(url).into(holder.photo)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    interface removeInterface {
        fun remove(position: Int)
        fun add(position: Int)
        fun minus(position: Int)
    }
}