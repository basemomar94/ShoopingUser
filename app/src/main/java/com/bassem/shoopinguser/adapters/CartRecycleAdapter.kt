package com.bassem.shoopinguser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.CartClass

class CartRecycleAdapter(
    val cartList: MutableList<CartClass>
) : RecyclerView.Adapter<CartRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val title = itemview.findViewById<TextView>(R.id.cartTitle)!!
        val price = itemview.findViewById<TextView>(R.id.cartPrice)!!
        val amount = itemview.findViewById<TextView>(R.id.cartAmount)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.title.text = cartItem.title
        holder.price.text = "${cartItem.price} EGP"
        holder.amount.text = cartItem.amount.toString()
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}