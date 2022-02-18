package com.bassem.shoopinguser.models

import android.widget.ImageView

data class CartClass(
    val title: String?=null,
    val price: String?=null,
    var amount: Int = 1,
    val photo:String?=null,
    val id :String?=null
)