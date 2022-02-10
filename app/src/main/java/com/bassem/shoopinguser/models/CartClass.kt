package com.bassem.shoopinguser.models

import android.widget.ImageView

data class CartClass(
    val title: String,
    val price: Int,
    var amount: Int = 1
   // val imageView: ImageView
)