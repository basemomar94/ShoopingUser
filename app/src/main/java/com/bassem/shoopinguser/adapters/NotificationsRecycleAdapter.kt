package com.bassem.shoopinguser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.models.NotificationsClass

class NotificationsRecycleAdapter(
    val notificationsList: MutableList<NotificationsClass>
) : RecyclerView.Adapter<NotificationsRecycleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.titleNoti)
        val body = itemView.findViewById<TextView>(R.id.bodyNoti)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notificationsList[position]
        holder.title.text = item.title
        holder.body.text = item.body
    }

    override fun getItemCount(): Int {

        return notificationsList.size
    }
}