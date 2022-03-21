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

class SimilarAdapter(
    var itemsList: MutableList<ItemsClass>,
    val context: Context,
    val expandListner: expandInterface,
) : RecyclerView.Adapter<SimilarAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val image = itemview.findViewById<ImageView>(R.id.similarImg)
        val title = itemview.findViewById<TextView>(R.id.similarTitle)
        val price = itemview.findViewById<TextView>(R.id.similarPrice)



        init {


            itemview.setOnClickListener {
                val position = adapterPosition
                val item = itemsList[position].id
                val category = itemsList[position].category
                expandListner.viewItem(item!!, category!!)
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_similar, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        holder.title.text = item.title
        holder.price.text = "${item.price} EGP"
        val url = item.photo
        Glide.with(context).load(url).into(holder.image)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }



    interface expandInterface {
        fun viewItem(item: String, category: String)


    }


}