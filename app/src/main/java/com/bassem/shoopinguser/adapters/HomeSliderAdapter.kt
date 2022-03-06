package com.bassem.shoopinguser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bassem.shoopinguser.R
import com.smarteist.autoimageslider.SliderViewAdapter

class HomeSliderAdapter(
    val listofimages: List<Int>
) : SliderViewAdapter<HomeSliderAdapter.ViewHolder>() {

    inner class ViewHolder(itemview: View) : SliderViewAdapter.ViewHolder(itemview) {
        val img = itemview.findViewById<ImageView>(R.id.slider_img)


    }


    override fun getCount(): Int {
        return listofimages.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.slider_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder!!.img.setImageResource(listofimages[position])
    }
}