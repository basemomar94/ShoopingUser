package com.bassem.shoopinguser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.OrderClass

class OrdersRecycleAdapter(
    val Orderslist: MutableList<OrderClass>,
    val lisnter: clickInterface
) : RecyclerView.Adapter<OrdersRecycleAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placedDate = itemView.findViewById<TextView>(R.id.dateOrder)
        val status = itemView.findViewById<TextView>(R.id.statusOrder)
        val total = itemView.findViewById<TextView>(R.id.totalOrder)
        val number = itemView.findViewById<TextView>(R.id.numOrder)

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                lisnter.click(position)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Orderslist[position]
        holder.placedDate.text = item.order_date.toString()
        holder.status.text = item.status
        holder.number.text = item.order_id
        holder.total.text = item.cost
    }

    override fun getItemCount(): Int {

        return Orderslist.size
    }

    interface clickInterface {
        fun click(position: Int)
    }
}